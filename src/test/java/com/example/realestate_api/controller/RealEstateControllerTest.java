package com.example.realestate_api.controller;

import com.example.realestate_api.dto.ApiResponseDto;
import com.example.realestate_api.dto.ApiResponseDto.Header;
import com.example.realestate_api.dto.ApiResponseDto.Body;
import com.example.realestate_api.dto.ApiResponseDto.Items;
import com.example.realestate_api.dto.ApiResponseDto.Item;
import com.example.realestate_api.service.RealEstateApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(RealEstateController.class)
public class RealEstateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RealEstateApiService realEstateApiService;

    @Test
    public void testGetRealEstateData() throws Exception {
        // 1. Mock된 응답 데이터 구성
        Header header = new Header();
        header.setResultCode("000");
        header.setResultMsg("OK");

        Item item1 = new Item();
        item1.setAptNm("Mock Apt 1");
        item1.setDealAmount("100,000");
        item1.setDealYear(2024);
        item1.setDealMonth(10);
        item1.setDealDay(5);
        item1.setExcluUseAr(84.5);
        item1.setFloor(5);
        item1.setBuildYear(2000);

        Item item2 = new Item();
        item2.setAptNm("Mock Apt 2");
        item2.setDealAmount("120,000");
        item2.setDealYear(2024);
        item2.setDealMonth(10);
        item2.setDealDay(7);
        item2.setExcluUseAr(99.5);
        item2.setFloor(10);
        item2.setBuildYear(2005);

        Items items = new Items();
        items.setItemList(Arrays.asList(item1, item2));

        Body body = new Body();
        body.setItems(items);
        body.setNumOfRows(10);
        body.setPageNo(1);
        body.setTotalCount(2);

        ApiResponseDto mockResponse = new ApiResponseDto();
        mockResponse.setHeader(header);
        mockResponse.setBody(body);

        // 2. Mock된 Service 레이어의 동작 설정
        given(realEstateApiService.fetchRealEstateData("11110", "202410"))
                .willReturn(mockResponse);

        // 3. Controller의 엔드포인트 테스트
        mockMvc.perform(get("/realestate/transactions")
                .param("lawdCd", "11110")
                .param("dealYmd", "202410"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.resultCode").value("000"))  // 헤더의 resultCode 확인
                .andExpect(jsonPath("$.header.resultMsg").value("OK"))   // 헤더의 resultMsg 확인
                .andExpect(jsonPath("$.body.items.itemList[0].aptNm").value("Mock Apt 1"))  // 첫 번째 아파트 이름 확인
                .andExpect(jsonPath("$.body.items.itemList[0].dealAmount").value("100,000")) // 첫 번째 거래 금액 확인
                .andExpect(jsonPath("$.body.items.itemList[1].aptNm").value("Mock Apt 2"))  // 두 번째 아파트 이름 확인
                .andExpect(jsonPath("$.body.items.itemList[1].dealAmount").value("120,000")); // 두 번째 거래 금액 확인
    }
}
