package com.example.realestate_api.service;

import java.util.List;

import com.example.realestate_api.dto.ApiResponseDto;

public interface RealEstateTransactionService {
    
    public void saveTransactions(List<ApiResponseDto.Item> itemList);

}
