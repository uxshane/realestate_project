package com.example.realestate_api.service;

import com.example.realestate_api.dto.XmlApiResponseDto;

public interface RealEstateApiService {

    XmlApiResponseDto fetchRealEstateData(String lawdCd, String dealYmd, int pageNo, int numOfRows);
    
}
