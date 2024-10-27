package com.example.realestate_api.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.realestate_api.dto.XmlApiResponseDto;
import com.example.realestate_api.dto.XmlApiResponseDto.Item;
import com.example.realestate_api.entity.RealEstateTransaction;
import com.example.realestate_api.repository.RealEstateTransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class RealEstateTransactionServiceImpl implements RealEstateTransactionService {

    //공공데이터 API 호출 시 스레드 풀을 사용해 비동기 처리
    private static final Logger logger = LoggerFactory.getLogger(RealEstateTransactionServiceImpl.class);
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final int MAX_RETRY_COUNT = 3;  // 최대 재시도 횟수

    //필요한 클래스 생성
    private final RealEstateTransactionRepository realEstateTransactionRepository;
    private final RealEstateApiService realEstateApiService;

    public RealEstateTransactionServiceImpl(RealEstateTransactionRepository realEstateTransactionRepository, RealEstateApiService realEstateApiService) {
        this.realEstateTransactionRepository = realEstateTransactionRepository;
        this.realEstateApiService = realEstateApiService;
    }

    @Override
    public void fetchAndSaveTransactionsAsync(String lawdCd) {
        for (int month = 1; month <= 9; month++) {
            String dealYmd = String.format("2024%02d", month); // 202401 ~ 202409
            
            // 비동기 API 호출 및 데이터 저장
            CompletableFuture.runAsync(() -> {
                try {
                    performWithRetry(() -> fetchAndSaveTransactions(lawdCd, dealYmd), lawdCd, dealYmd);
                } catch (Exception e) {
                    logger.error("데이터 저장 실패: lawdCd={}, dealYmd={}, 오류={}", lawdCd, dealYmd, e.getMessage());
                }
            }, executorService);
        }
    }

    // 재시도 로직을 포함한 메서드
    private void performWithRetry(Runnable task, String lawdCd, String dealYmd) throws Exception {
        int retryCount = 0;
        boolean success = false;
        
        while (!success && retryCount < MAX_RETRY_COUNT) {
            try {
                task.run();
                success = true;  // 성공 시 반복 종료
            } catch (Exception e) {
                retryCount++;
                logger.warn("재시도 {}/{} 실패: lawdCd={}, dealYmd={}, 오류={}", retryCount, MAX_RETRY_COUNT, lawdCd, dealYmd, e.getMessage());
                
                if (retryCount >= MAX_RETRY_COUNT) {
                    logger.error("최대 재시도 횟수 초과: lawdCd={}, dealYmd={}", lawdCd, dealYmd);
                    throw e;  // 최종 실패 시 예외 전파
                }
            }
        }
    }

    @Override
    @Transactional
    public void fetchAndSaveTransactions(String lawdCd, String dealYmd) {
        final int numOfRows = 500;  // 한 페이지에 500개 데이터 가져옴
        int pageNo = 1;
        boolean hasNextPage = true;

        while (hasNextPage) {
            // API 호출 (페이징 지원)
            XmlApiResponseDto response = realEstateApiService.fetchRealEstateData(lawdCd, dealYmd, pageNo, numOfRows);

            if (response != null) {
                // 총 데이터 개수 확인
                int totalCount = response.getBody().getTotalCount();

                // 받은 데이터 저장
                List<Item> items = response.getBody().getItems().getItemList();
                saveTransactions(items, lawdCd, dealYmd);

                // 다음 페이지로 넘어갈지 여부 확인
                if (pageNo * numOfRows < totalCount) {
                    pageNo++;
                } else {
                    hasNextPage = false;
                }
            } else {
                System.err.println("API 응답 실패: lawdCd=" + lawdCd + ", dealYmd=" + dealYmd);
                hasNextPage = false;
            }
        }
    }

    @Transactional
    @Override
    public void saveTransactions(List<Item> itemList, String lawdCd, String dealYmd) {
        for (XmlApiResponseDto.Item item : itemList) {
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

            // 추가된 필드 설정
            transaction.setLawdCd(lawdCd);
            transaction.setDealYmd(dealYmd);

            // 저장
            realEstateTransactionRepository.save(transaction);
        }
    }

    @Override
    public List<RealEstateTransaction> getTransactions(String lawdCd, String dealYmd) {
        return realEstateTransactionRepository.findByLawdCdAndDealYmd(lawdCd, dealYmd);
    }
}
