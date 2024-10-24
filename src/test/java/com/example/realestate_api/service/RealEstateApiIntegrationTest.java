package com.example.realestate_api.service;

import com.example.realestate_api.entity.RealEstateTransaction;
import com.example.realestate_api.repository.RealEstateTransactionRepository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RealEstateApiIntegrationTest {

    @Autowired
    private RealEstateTransactionService realEstateTransactionService;

    @Autowired
    private RealEstateTransactionRepository transactionRepository;

    @Test
    public void testFetchAndStoreRealEstateData() throws Exception {
        // 1. 데이터 가져오기 및 저장 호출
        realEstateTransactionService.fetchAndSaveTransactions("11110", "202410");

        // 2. DB에서 데이터가 저장되었는지 확인
        assertThat(transactionRepository.findAll()).isNotEmpty();

        // 3. DB에서 데이터가 정상적으로 조회되는지 확인
        List<RealEstateTransaction> transactions = transactionRepository.findAll();
        assertThat(transactions).isNotNull();
        assertThat(transactions.size()).isGreaterThan(0);

        // 데이터의 일부 속성을 확인하여 일관성 검증 (예: 아파트 이름, 거래 금액 등)
        RealEstateTransaction transaction = transactions.get(0);
        assertThat(transaction.getAptNm()).isNotNull();
        assertThat(transaction.getDealAmount()).isNotNull();
    }
}
