package com.example.realestate_api.service;

import com.example.realestate_api.dto.ApiResponseDto;

public interface RealEstateApiService {

    ApiResponseDto fetchRealEstateData(String lawdCd, String dealYmd, int pageNo, int numOfRows);
    
}
