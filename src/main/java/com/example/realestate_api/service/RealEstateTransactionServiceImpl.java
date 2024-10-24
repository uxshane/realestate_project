package com.example.realestate_api.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.example.realestate_api.dto.ApiResponseDto;
import com.example.realestate_api.dto.ApiResponseDto.Item;
import com.example.realestate_api.entity.RealEstateTransaction;
import com.example.realestate_api.repository.RealEstateTransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class RealEstateTransactionServiceImpl implements RealEstateTransactionService {

    //공공데이터 API 호출 시 스레드 풀을 사용해 비동기 처리
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

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
                fetchAndSaveTransactions(lawdCd, dealYmd);
            }, executorService);
        }
    }

    @Override
    @Transactional
    public void fetchAndSaveTransactions(String lawdCd, String dealYmd) {
        int numOfRows = 500;  // 한 페이지에 500개 데이터 가져옴
        int pageNo = 1;
        boolean hasNextPage = true;

        while (hasNextPage) {
            // API 호출 (페이징 지원)
            ApiResponseDto response = realEstateApiService.fetchRealEstateData(lawdCd, dealYmd, pageNo, numOfRows);

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
        for (ApiResponseDto.Item item : itemList) {
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
