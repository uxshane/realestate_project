package com.example.realestate_api.service;

import com.example.realestate_api.entity.RealEstateTransaction;
import com.example.realestate_api.repository.RealEstateTransactionRepository;
import com.example.realestate_api.dto.ApiResponseDto;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RealEstateApiIntegrationTest {

    @Autowired
    private RealEstateApiService realEstateApiService;

    @Autowired
    private RealEstateTransactionService realEstateTransactionService;

    @Autowired
    private RealEstateTransactionRepository transactionRepository;

    @Test
    public void testFetchAndStoreRealEstateData() throws Exception {
        // 1. API 데이터를 호출하여 가져옴
        ApiResponseDto responseDto = realEstateApiService.fetchRealEstateData("11110", "202410");
        
        // 2. 데이터를 DB에 저장 (중복 방지 포함)
        // responseDto의 item 필드가 단일 객체이므로 단일 객체를 사용하여 저장 처리
        realEstateTransactionService.saveTransactions(responseDto.getBody().getItems().getItemList());

        // 3. DB에서 데이터가 저장되었는지 확인
        assertThat(transactionRepository.findAll()).isNotEmpty();

        // 4. 조회 API 호출을 통해 데이터가 조회되는지 확인
        List<RealEstateTransaction> transactions = transactionRepository.findAll();
        assertThat(transactions).isNotNull();
        assertThat(transactions.size()).isGreaterThan(0);
        assertThat(responseDto.getBody().getItems().getItemList().size()).isEqualTo(transactions.size());
    }
}
