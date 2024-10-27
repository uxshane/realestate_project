package com.example.realestate_api.service;

import com.example.realestate_api.dto.XmlApiResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
    public XmlApiResponseDto fetchRealEstateData(String lawdCd, String dealYmd, int pageNo, int numOfRows) {
        String apiUrl = String.format(
            "https://apis.data.go.kr/1613000/RTMSDataSvcAptTradeDev/getRTMSDataSvcAptTradeDev?LAWD_CD=%s&DEAL_YMD=%s&serviceKey=%s&pageNo=%d&numOfRows=%d",
            lawdCd, dealYmd, apiKey, pageNo, numOfRows);
    
        try {
            String response = restTemplate.getForObject(apiUrl, String.class);
    
            if (response == null) {
                logger.error("API 응답이 null입니다: lawdCd={}, dealYmd={}", lawdCd, dealYmd);
                throw new IllegalArgumentException("API 응답이 null입니다.");
            }
    
            XmlMapper xmlMapper = new XmlMapper();
            try {
                // XML 형식 파싱 시도
                return xmlMapper.readValue(response, XmlApiResponseDto.class);
            } catch (Exception xmlException) {
                logger.warn("XML 파싱 실패, JSON 형식으로 재시도: lawdCd={}, dealYmd={}", lawdCd, dealYmd);
    
                // JSON 형식 파싱 시도
                ObjectMapper jsonMapper = new ObjectMapper();
                JsonNode rootNode = jsonMapper.readTree(response);
    
                // 최상위 노드에서 "response" 부분만 추출
                JsonNode responseNode = rootNode.path("response");
                if (responseNode.isMissingNode()) {
                    throw new RuntimeException("JSON 응답에 'response' 노드가 없습니다.");
                }
    
                // 추출된 "response" 부분을 문자열로 변환
                String xmlContent = new XmlMapper().writeValueAsString(responseNode);
                return xmlMapper.readValue(xmlContent, XmlApiResponseDto.class);
            }
    
        } catch (RestClientException e) {
            logger.error("API 호출 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("API 호출 중 오류 발생", e);
        } catch (Exception e) {
            logger.error("응답을 처리하는 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("응답을 처리하는 중 오류 발생", e);
        }
    }
    
}
