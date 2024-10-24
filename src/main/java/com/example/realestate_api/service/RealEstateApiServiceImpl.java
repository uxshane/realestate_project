package com.example.realestate_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import com.example.realestate_api.dto.ApiResponseDto;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Service
public class RealEstateApiServiceImpl implements RealEstateApiService {

    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(RealEstateApiServiceImpl.class);

    @Value("${realestate.api.key}")
    private String apiKey;

    public RealEstateApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ApiResponseDto fetchRealEstateData(String lawdCd, String dealYmd, int pageNo, int numOfRows) {
        String apiUrl = String.format(
            "https://apis.data.go.kr/1613000/RTMSDataSvcAptTradeDev/getRTMSDataSvcAptTradeDev?LAWD_CD=%s&DEAL_YMD=%s&serviceKey=%s&pageNo=%d&numOfRows=%d",
            lawdCd, dealYmd, apiKey, pageNo, numOfRows);
        
        try {
            String xmlResponse = restTemplate.getForObject(apiUrl, String.class);

            if (xmlResponse == null) {
                logger.error("API 응답이 null입니다: lawdCd={}, dealYmd={}", lawdCd, dealYmd);
                throw new IllegalArgumentException("API 응답이 null입니다.");
            }

            XmlMapper mapper = new XmlMapper();
            return mapper.readValue(xmlResponse, ApiResponseDto.class);

        } catch (RestClientException e) {
            logger.error("API 호출 오류 발생: {}", e);
            throw new RuntimeException("API 호출 중 오류 발생", e);
        } catch (Exception e) {
            logger.error("응답을 처리하는 중 오류 발생: {}", e);
            throw new RuntimeException("응답을 처리하는 중 오류 발생", e);
        }
    }
}
