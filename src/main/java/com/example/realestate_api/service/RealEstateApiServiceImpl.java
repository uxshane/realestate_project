package com.example.realestate_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.realestate_api.dto.ApiResponseDto;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Service
public class RealEstateApiServiceImpl implements RealEstateApiService {

    private final RestTemplate restTemplate;

    @Value("${realestate.api.key}")
    private String apiKey;

    public RealEstateApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ApiResponseDto fetchRealEstateData(String lawdCd, String dealYmd) {
        String apiUrl = String.format("https://apis.data.go.kr/1613000/RTMSDataSvcAptTrade/getRTMSDataSvcAptTrade?LAWD_CD=%s&DEAL_YMD=%s&serviceKey=%s", lawdCd, dealYmd, apiKey);
        
        String xmlResponse = restTemplate.getForObject(apiUrl, String.class);
        
        if (xmlResponse == null) {
            throw new IllegalArgumentException("API 응답이 null입니다.");
        }

        XmlMapper mapper = new XmlMapper();
        try {
            return mapper.readValue(xmlResponse, ApiResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("응답을 처리하는 중 오류 발생", e);
        }
    }
}
