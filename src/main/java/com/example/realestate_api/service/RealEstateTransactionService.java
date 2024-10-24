package com.example.realestate_api.service;

import java.util.List;

import com.example.realestate_api.dto.ApiResponseDto;
import com.example.realestate_api.entity.RealEstateTransaction;

public interface RealEstateTransactionService {

    public void fetchAndSaveTransactionsAsync(String lawdCd);

    public void fetchAndSaveTransactions(String lawdCd, String dealYmd);
    
    public void saveTransactions(List<ApiResponseDto.Item> itemList, String lawdCd, String dealYmd);

    public List<RealEstateTransaction> getTransactions(String lawdCd, String dealYmd);

}
