package com.example.realestate_api.transaction.transaction_service_test;

import com.example.realestate_api.transaction.transaction_dto.XmlApiResponseDto;
import com.example.realestate_api.transaction.transaction_service.RealEstateApiService;
import com.example.realestate_api.transaction.transaction_service.RealEstateTransactionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ApiErrorLoggingTest {

    @Mock
    private RealEstateApiService realEstateApiService;

    @InjectMocks
    private RealEstateTransactionServiceImpl realEstateTransactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchAndSaveTransactionsAsync_RetryOnFailure() throws Exception {
        // 처음 두 번 예외 발생, 세 번째 호출에 성공
        when(realEstateApiService.fetchRealEstateData(anyString(), anyString(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("API 호출 실패"))
                .thenThrow(new RuntimeException("API 호출 실패"))
                .thenReturn(new XmlApiResponseDto()); // 성공적인 응답 반환

        // 비동기 메서드 호출
        realEstateTransactionService.fetchAndSaveTransactionsAsync("41465");

        // 비동기 작업이 완료될 때까지 대기
        TimeUnit.SECONDS.sleep(5); // 비동기 작업이 완료되도록 대기 시간 설정

        // 호출 검증 (유연하게 pageNo 인자 처리)
        verify(realEstateApiService, times(3)).fetchRealEstateData(eq("41465"), eq("202409"), anyInt(), eq(500));
    }

    @Test
    public void testFetchAndSaveTransactions_FailureAfterMaxRetries() throws Exception {
        // 세 번 모두 예외 발생
        when(realEstateApiService.fetchRealEstateData(anyString(), anyString(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("API 호출 실패"));

        // 비동기 메서드 호출
        realEstateTransactionService.fetchAndSaveTransactionsAsync("41465");

        // 비동기 작업이 완료될 때까지 대기
        TimeUnit.SECONDS.sleep(5); // 비동기 작업이 완료되도록 대기 시간 설정

        // 호출 검증
        verify(realEstateApiService, times(3)).fetchRealEstateData(eq("41465"), eq("202409"), anyInt(), eq(500));
    }
}



