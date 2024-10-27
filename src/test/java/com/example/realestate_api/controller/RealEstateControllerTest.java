package com.example.realestate_api.controller;

import com.example.realestate_api.entity.RealEstateTransaction;
import com.example.realestate_api.service.RealEstateTransactionService;
import com.example.realestate_api.config.TestSecurityConfig;  // TestSecurityConfig 추가

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(RealEstateController.class)
@Import(TestSecurityConfig.class)  // TestSecurityConfig를 Import하여 보안 비활성화
public class RealEstateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RealEstateTransactionService realEstateTransactionService;

    @Test
    public void testFetchAndSaveRealEstateData() throws Exception {
        // Mock Service 레이어의 동작 설정: 데이터를 저장하는 동작을 호출했을 때 아무 작업도 수행하지 않음
        doNothing().when(realEstateTransactionService).fetchAndSaveTransactions("11110", "202410");

        // Controller의 fetch-and-save 엔드포인트 테스트
        mockMvc.perform(get("/realestate/fetch-and-save")
                .param("lawdCd", "11110")
                .param("dealYmd", "202410"))
                .andExpect(status().isOk())  // HTTP 200 상태 코드 확인
                .andDo(print());  // 응답 내용을 출력
    }

    @Test
    public void testGetStoredRealEstateData() throws Exception {
        // Mock 응답 데이터 구성
        RealEstateTransaction transaction = new RealEstateTransaction();
        transaction.setSggCd("11110");
        transaction.setAptNm("Mock Apt 1");
        transaction.setDealAmount("100,000");
        transaction.setDealYear("2024");
        transaction.setDealMonth("10");
        transaction.setDealDay("5");
        transaction.setExcluUseAr(84.5);
        transaction.setFloor(5);
        transaction.setBuildYear(2000);
        transaction.setUmdNm("Mock UmdNm");
        transaction.setJibun("Mock Jibun");

        // 여러 거래 데이터를 위한 리스트를 설정하고, 서비스가 호출될 때 이를 반환하도록 설정
        given(realEstateTransactionService.getTransactions("11110", "202410"))
                .willReturn(Arrays.asList(transaction));

        // Controller의 transactions 엔드포인트 테스트 및 응답 내용 검증
        mockMvc.perform(get("/realestate/transactions")
                .param("lawdCd", "11110")
                .param("dealYmd", "202410"))
                .andExpect(status().isOk())  // HTTP 200 상태 코드 확인
                .andDo(print())  // 응답 내용을 출력
                .andExpect(jsonPath("$[0].aptNm").value("Mock Apt 1"))  // 첫 번째 아파트 이름 확인
                .andExpect(jsonPath("$[0].dealAmount").value("100,000")) // 첫 번째 거래 금액 확인
                .andExpect(jsonPath("$[0].floor").value("5")) // 층수 확인
                .andExpect(jsonPath("$[0].excluUseAr").value("84.5"));  // 전용 면적 확인
    }
}
