package com.example.realestate_api.transaction.transaction_service;

import com.example.realestate_api.transaction.transaction_dto.XmlApiResponseDto;

public interface RealEstateApiService {

    XmlApiResponseDto fetchRealEstateData(String lawdCd, String dealYmd, int pageNo, int numOfRows);
    
}
