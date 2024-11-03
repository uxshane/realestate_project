package com.example.realestate_api.transaction.transaction_service;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.realestate_api.region.region_entity.RegionCode;
import com.example.realestate_api.region.region_repository.RegionCodeRepository;
import com.example.realestate_api.transaction.transaction_dto.XmlApiResponseDto;
import com.example.realestate_api.transaction.transaction_dto.XmlApiResponseDto.Item;
import com.example.realestate_api.transaction.transaction_entity.RealEstateTransaction;
import com.example.realestate_api.transaction.transaction_repository.RealEstateTransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class RealEstateTransactionServiceImpl implements RealEstateTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(RealEstateTransactionServiceImpl.class);
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final int MAX_RETRY_COUNT = 3;

    private final RegionCodeRepository regionCodeRepository;
    private final RealEstateTransactionRepository realEstateTransactionRepository;
    private final RealEstateApiService realEstateApiService;

    public RealEstateTransactionServiceImpl(RealEstateTransactionRepository realEstateTransactionRepository,        RealEstateApiService realEstateApiService, RegionCodeRepository regionCodeRepository) {
        this.regionCodeRepository = regionCodeRepository;
        this.realEstateTransactionRepository = realEstateTransactionRepository;
        this.realEstateApiService = realEstateApiService;
    }

    @Override
    public void fetchAndSaveAllRegionsTransactions() {
        long startTime = System.currentTimeMillis(); // 시작 시간 측정
        List<String> lawdCdList = regionCodeRepository.findAll()
                                                      .stream()
                                                      .map(RegionCode::getLawdCd)
                                                      .toList();

        // 모든 CompletableFuture 작업들을 모아서 allOf로 처리
        List<CompletableFuture<Void>> futures = lawdCdList.stream()
            .map(this::fetchAndSaveTransactionsAsync)
            .toList();

        // 모든 작업이 완료될 때까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 종료 시간 기록
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        logger.info("총 데이터 저장 소요 시간: {} ms", duration); // 시간 로깅
    }

    @Override
    public CompletableFuture<Void> fetchAndSaveTransactionsAsync(String lawdCd) {
        return CompletableFuture.runAsync(() -> {
            for (int month = 1; month <= 9; month++) {
                String dealYmd = String.format("2024%02d", month);
                try {
                    performWithRetry(() -> fetchAndSaveTransactions(lawdCd, dealYmd), lawdCd, dealYmd);
                } catch (Exception e) {
                    logger.error("데이터 저장 실패: lawdCd={}, dealYmd={}, 오류={}", lawdCd, dealYmd, e.getMessage());
                }
            }
        }, executorService);
    }

    private void performWithRetry(Runnable task, String lawdCd, String dealYmd) throws Exception {
        int retryCount = 0;
        boolean success = false;
        
        while (!success && retryCount < MAX_RETRY_COUNT) {
            try {
                task.run();
                success = true;
                logger.info("데이터 저장 성공: lawdCd={}, dealYmd={}", lawdCd, dealYmd);
            } catch (Exception e) {
                retryCount++;
                logger.warn("재시도 {}/{} 실패: lawdCd={}, dealYmd={}, 오류={}", retryCount, MAX_RETRY_COUNT, lawdCd, dealYmd, e.getMessage());

                if (retryCount >= MAX_RETRY_COUNT) {
                    logger.error("최대 재시도 횟수 초과: lawdCd={}, dealYmd={}", lawdCd, dealYmd);
                    throw e; // 최종 실패 시 예외 전파
                }
                try {
                    // 재시도 간 간격을 둠 (예: 2초)
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    logger.error("재시도 대기 중 인터럽트 발생: {}", ie.getMessage());
                    throw e;
                }
            }
        }
    }

    @Override
    @Transactional
    public void fetchAndSaveTransactions(String lawdCd, String dealYmd) {
        long startTime = System.currentTimeMillis(); // 시작 시간 측정

        final int numOfRows = 500;
        int pageNo = 1;
        boolean hasNextPage = true;

        while (hasNextPage) {
            XmlApiResponseDto response = realEstateApiService.fetchRealEstateData(lawdCd, dealYmd, pageNo, numOfRows);

            if (response != null) {
                int totalCount = response.getBody().getTotalCount();
                List<Item> items = response.getBody().getItems().getItemList();
                saveTransactions(items, lawdCd, dealYmd);

                if (pageNo * numOfRows < totalCount) {
                    pageNo++;
                } else {
                    hasNextPage = false;
                }
            } else {
                logger.error("API 응답 실패: lawdCd={}, dealYmd={}", lawdCd, dealYmd);
                hasNextPage = false;
            }
        }
        // 종료 시간 기록
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        logger.info("총 데이터 저장 소요 시간: {} ms", duration); // 시간 로깅
    }

@Override
@Transactional
public void saveTransactions(List<Item> itemList, String lawdCd, String dealYmd) {
    // 미리 존재하는 거래 내역을 조회하여 Set에 저장
    List<RealEstateTransaction> existingTransactions = realEstateTransactionRepository
            .findByLawdCdAndDealYmd(lawdCd, dealYmd);

    Set<String> existingKeys = existingTransactions.stream()
            .map(t -> t.getAptNm() + t.getDealMonth() + t.getDealDay() + t.getFloor() + t.getDealAmount())
            .collect(Collectors.toSet());

    List<RealEstateTransaction> transactionsToSave = new ArrayList<>();  // 배치 저장용 리스트

    itemList.forEach(item -> {
        String key = item.getAptNm() + item.getDealMonth() + item.getDealDay() + item.getFloor() + item.getDealAmount();

        if (!existingKeys.contains(key)) {
            RealEstateTransaction transaction = new RealEstateTransaction();
            transaction.setSggCd(item.getSggCd());
            transaction.setUmdCd(item.getUmdCd());
            transaction.setLandCd(item.getLandCd());
            transaction.setBonbun(item.getBonbun());
            transaction.setBubun(item.getBubun());
            transaction.setRoadNm(item.getRoadNm());
            transaction.setRoadNmSggCd(item.getRoadNmSggCd());
            transaction.setRoadNmCd(item.getRoadNmCd());
            transaction.setRoadNmSeq(item.getRoadNmSeq());
            transaction.setRoadNmbCd(item.getRoadNmbCd());
            transaction.setRoadNmBonbun(item.getRoadNmBonbun());
            transaction.setRoadNmBubun(item.getRoadNmBubun());
            transaction.setUmdNm(item.getUmdNm());
            transaction.setAptNm(item.getAptNm());
            transaction.setJibun(item.getJibun());
            transaction.setExcluUseAr(item.getExcluUseAr());
            transaction.setDealYear(item.getDealYear());
            transaction.setDealMonth(item.getDealMonth());
            transaction.setDealDay(item.getDealDay());
            transaction.setDealAmount(item.getDealAmount());
            transaction.setFloor(item.getFloor());
            transaction.setBuildYear(item.getBuildYear());
            transaction.setAptSeq(item.getAptSeq());
            transaction.setCdealType(item.getCdealType());
            transaction.setCdealDay(item.getCdealDay());
            transaction.setDealingGbn(item.getDealingGbn());
            transaction.setEstateAgentSggNm(item.getEstateAgentSggNm());
            transaction.setRgstDate(item.getRgstDate());
            transaction.setAptDong(item.getAptDong());
            transaction.setSlerGbn(item.getSlerGbn());
            transaction.setBuyerGbn(item.getBuyerGbn());
            transaction.setLandLeaseholdGbn(item.getLandLeaseholdGbn());

            transaction.setLawdCd(lawdCd);
            transaction.setDealYmd(dealYmd);

            transactionsToSave.add(transaction);  // 배치에 추가
            existingKeys.add(key);  // 다음 검사 시 중복 확인을 위해 키 추가

            // 100개씩 모아서 배치 저장
            if (transactionsToSave.size() == 500) {
                realEstateTransactionRepository.saveAll(transactionsToSave);
                transactionsToSave.clear();  // 배치 초기화
            }
        } else {
            logger.warn("중복 데이터가 발견되었습니다. 법정동코드 : " + lawdCd + "\n제외시키고 계속 진행하겠습니다.");
        }
    });

    // 남아있는 데이터를 최종 저장 (100개 미만인 경우)
    if (!transactionsToSave.isEmpty()) {
        realEstateTransactionRepository.saveAll(transactionsToSave);
    }
}


    @Override
    public List<RealEstateTransaction> getTransactions(String lawdCd, String dealYmd) {
        return realEstateTransactionRepository.findByLawdCdAndDealYmd(lawdCd, dealYmd);
    }
}
