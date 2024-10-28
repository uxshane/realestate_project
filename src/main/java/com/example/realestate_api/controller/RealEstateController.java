package com.example.realestate_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.realestate_api.entity.RealEstateTransaction;
import com.example.realestate_api.service.RealEstateTransactionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/realestate")
public class RealEstateController {

    private final RealEstateTransactionService realEstateTransactionService;

    @Autowired
    public RealEstateController(RealEstateTransactionService realEstateTransactionService) {
        this.realEstateTransactionService = realEstateTransactionService;
    }

    /**
     * 2024년도 1~9월 경기도권에 있는 시,군,구 부동산 거래 데이터를 가져오고 저장하는 API
     *
     * @throws Exception API 호출 및 데이터 처리 중 예외 발생 시
     */
    @GetMapping("/fetch-and-save/Gyeonggido")
    public void fetchAndSaveRealEstateDataforGyeonggido() throws Exception{
        realEstateTransactionService.fetchAndSaveAllRegionsTransactions();
    }
    

    /**
     * 지정한 법정동 코드 하나의 2024년도 1~9월 부동산 거래 데이터를 가져오고 저장하는 API
     *
     * @param lawdCd  법정동 코드
     * @throws Exception API 호출 및 데이터 처리 중 예외 발생 시
     */
    @GetMapping("/fetch-and-save/2024")
    public void fetchAndSaveRealEstateDatafor2024(@RequestParam("lawdCd") String lawdCd) throws Exception {
        // RealEstateTransactionService의 메서드를 호출하여 데이터를 가져오고 저장
        realEstateTransactionService.fetchAndSaveTransactionsAsync(lawdCd);
    }

    /**
     * 특정 월, 특정 지역 부동산 거래 데이터를 가져오고 저장하는 API
     *
     * @param lawdCd  법정동 코드
     * @param dealYmd 거래 연월 (YYYYMM 형식)
     * @throws Exception API 호출 및 데이터 처리 중 예외 발생 시
     */
    @GetMapping("/fetch-and-save")
    public void fetchAndSaveRealEstateData(@RequestParam("lawdCd") String lawdCd,
                                           @RequestParam("dealYmd") String dealYmd) throws Exception {
        // RealEstateTransactionService의 메서드를 호출하여 데이터를 가져오고 저장
        realEstateTransactionService.fetchAndSaveTransactions(lawdCd, dealYmd);
    }

    /**
     * DB에 저장된 부동산 거래 데이터를 조회하는 API
     *
     * @param lawdCd 법정동 코드
     * @param dealYmd 거래 연월 (YYYYMM 형식)
     * @return List<RealEstateTransaction> 저장된 거래 데이터 리스트
     */
    @GetMapping("/transactions")
    public List<RealEstateTransaction> getStoredRealEstateData(@RequestParam("lawdCd") String lawdCd,
                                                               @RequestParam("dealYmd") String dealYmd) {
        return realEstateTransactionService.getTransactions(lawdCd, dealYmd);
    }
}
