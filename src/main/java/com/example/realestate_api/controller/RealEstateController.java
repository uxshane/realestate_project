package com.example.realestate_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.realestate_api.dto.ApiResponseDto;
import com.example.realestate_api.entity.RealEstateTransaction;
import com.example.realestate_api.service.RealEstateApiService;
import com.example.realestate_api.service.RealEstateTransactionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class RealEstateController {

    private final RealEstateApiService realEstateApiService;
    private final RealEstateTransactionService realEstateTransactionService;

    @Autowired
    public RealEstateController(RealEstateApiService realEstateApiService,
                                RealEstateTransactionService realEstateTransactionService) {
        this.realEstateApiService = realEstateApiService;
        this.realEstateTransactionService = realEstateTransactionService;
    }

    /**
     * 부동산 거래 데이터를 가져오는 API
     *
     * @param lawdCd  법정동 코드
     * @param dealYmd 거래 연월 (YYYYMM 형식)
     * @return ApiResponseDto 부동산 거래 데이터를 포함한 응답 객체
     * @throws Exception API 호출 및 데이터 처리 중 예외 발생 시
     */
    @GetMapping("/realestate/transactions/fetch-and-save")
    public ApiResponseDto fetchAndSaveRealEstateData(@RequestParam("lawdCd") String lawdCd,
                                            @RequestParam("dealYmd") String dealYmd) throws Exception {
        // 1. API 호출로 데이터 가져오기
        ApiResponseDto response = realEstateApiService.fetchRealEstateData(lawdCd, dealYmd);

        // 2. 여러 Item 객체를 저장 (리스트로 저장)
        realEstateTransactionService.saveTransactions(response.getBody().getItems().getItemList(),
                                                      lawdCd, dealYmd);

        // 3. 결과 반환
        return response;
    }

    /**
     * DB에 저장된 부동산 거래 데이터를 조회하는 API
     *
     * @param lawdCd 법정동 코드
     * @param dealYmd 거래 연월 (YYYYMM 형식)
     * @return List<RealEstateTransaction> 저장된 거래 데이터 리스트
     */
    @GetMapping("/realestate/transactions")
    public List<RealEstateTransaction> getStoredRealEstateData(@RequestParam("lawdCd") String lawdCd,
                                                               @RequestParam("dealYmd") String dealYmd) {
        return realEstateTransactionService.getTransactions(lawdCd, dealYmd);
    }
    
}
