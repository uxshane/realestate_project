package com.example.realestate_api.service;

import com.example.realestate_api.entity.RealEstateTransaction;
import com.example.realestate_api.repository.RealEstateTransactionRepository;
import com.example.realestate_api.dto.ApiResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RealEstateApiIntegrationTest {

    @Autowired
    private RealEstateApiService realEstateApiService;

    @Autowired
    private RealEstateTransactionService realEstateTransactionService;

    @Autowired
    private RealEstateTransactionRepository transactionRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testFetchAndStoreRealEstateData() throws Exception {
        // 1. API 데이터를 호출하여 가져옴
        ApiResponseDto responseDto = realEstateApiService.fetchRealEstateData("11110", "202410");

        // 2. 데이터를 DB에 저장 (중복 방지 포함)
        realEstateTransactionService.saveTransactions(responseDto.getBody().getItems().getItemList());

        // 3. DB에서 데이터가 저장되었는지 확인
        assertThat(transactionRepository.findAll()).isNotEmpty();

        // 4. 조회 API 호출을 통해 데이터가 조회되는지 확인
        RealEstateTransaction[] transactions = restTemplate.getForObject("/realestate/transactions", RealEstateTransaction[].class);
        assertThat(transactions).isNotNull();
        assertThat(transactions.length).isGreaterThan(0);
        assertThat(transactions[0].getAptNm()).isEqualTo(responseDto.getBody().getItems().getItemList().get(0).getAptNm());
    }
}
