package com.example.realestate_api.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.realestate_api.dto.XmlApiResponseDto;
import com.example.realestate_api.service.RealEstateApiService;
import com.example.realestate_api.service.RealEstateTransactionServiceImpl;

@Disabled
@SpringBootTest
public class ApiErrorLoggingTest {

    @Mock
    private RealEstateApiService realEstateApiService;

    @Mock
    private Logger logger;

    @InjectMocks
    private RealEstateTransactionServiceImpl realEstateTransactionServiceImpl;

    private static final String LAWD_CD = "41460";
    private static final String DEAL_YMD = "202401";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchAndSaveTransactionsAsync_Successful() throws Exception {
        // 정상적인 API 응답을 모킹
        when(realEstateApiService.fetchRealEstateData(LAWD_CD, DEAL_YMD, 1, 500)).thenReturn(new XmlApiResponseDto());

        // 비동기 호출
        realEstateTransactionServiceImpl.fetchAndSaveTransactionsAsync(LAWD_CD);

        // 비동기 작업이 완료될 때까지 대기 (3초 정도 기다림)
        Thread.sleep(3000);

        // API 호출이 한 번 발생하는지 검증
        verify(realEstateApiService, times(1)).fetchRealEstateData(LAWD_CD, DEAL_YMD, 1, 500);
    }

    @Test
    public void testFetchAndSaveTransactionsAsync_RetryOnFailure() throws Exception {
        // 첫 두 번의 호출에서 예외 발생, 세 번째 호출은 성공하도록 설정
        when(realEstateApiService.fetchRealEstateData(LAWD_CD, DEAL_YMD, 1, 500))
            .thenThrow(new RuntimeException("API 호출 실패"))
            .thenThrow(new RuntimeException("API 호출 실패"))
            .thenReturn(new XmlApiResponseDto());  // 세 번째 호출은 성공

        // 비동기 호출
        realEstateTransactionServiceImpl.fetchAndSaveTransactionsAsync(LAWD_CD);

        // 비동기 작업이 완료될 때까지 대기 (5초 정도 기다림)
        Thread.sleep(5000);

        // API가 세 번 호출되었는지 확인
        verify(realEstateApiService, times(3)).fetchRealEstateData(LAWD_CD, DEAL_YMD, 1, 500);
    }

    @Test
    public void testFetchAndSaveTransactions_FailureAfterMaxRetries() throws InterruptedException {
        // 모든 재시도에서 실패하도록 설정
        when(realEstateApiService.fetchRealEstateData(LAWD_CD, DEAL_YMD, 1, 500))
            .thenThrow(new RuntimeException("API 호출 실패"));

        // 비동기 호출
        realEstateTransactionServiceImpl.fetchAndSaveTransactionsAsync(LAWD_CD);

        // 비동기 작업이 완료될 때까지 대기 (5초 정도 기다림)
        Thread.sleep(5000);

        // 최대 재시도 횟수만큼 호출되었는지 확인
        verify(realEstateApiService, times(3)).fetchRealEstateData(LAWD_CD, DEAL_YMD, 1, 500);

        // 로그가 기록되었는지 확인
        verify(logger, atLeastOnce()).error(contains("최대 재시도 횟수 초과: lawdCd="), eq(LAWD_CD), eq(DEAL_YMD));
    }
}

