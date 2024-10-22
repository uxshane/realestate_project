package com.example.realestate_api.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import com.example.realestate_api.dto.ApiResponseDto;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RealEstateApiServiceTest {

    @Autowired
    private RealEstateApiService realEstateApiService;

    @MockBean
    private RestTemplate restTemplate;  // RestTemplate을 MockBean으로 주입

        @Test
    public void testFetchRealEstateData() throws Exception {
        // Given: RestTemplate의 모킹된 동작을 설정
        String mockResponse = "<response><header><resultCode>000</resultCode><resultMsg>OK</resultMsg></header><body><items><item><aptNm>Mock Apt</aptNm></item></items></body></response>";
        
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);

        // When: RealEstateApiService 호출
        String lawdCd = "11110";  // 서울특별시 법정동 코드
        String dealYmd = "202410";  // 2024년 10월 거래 데이터

        ApiResponseDto result = realEstateApiService.fetchRealEstateData(lawdCd, dealYmd);

        // Then: 결과 검증
        assertThat(result).isNotNull();
        assertThat(result.getHeader().getResultCode()).isEqualTo("000");
        assertThat(result.getHeader().getResultMsg()).isEqualTo("OK");
        assertThat(result.getBody().getItems().getItemList()).isNotEmpty();
        assertThat(result.getBody().getItems().getItemList().get(0).getAptNm()).isEqualTo("Mock Apt");
    }
}
