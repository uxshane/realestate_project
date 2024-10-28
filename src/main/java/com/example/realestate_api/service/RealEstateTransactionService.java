package com.example.realestate_api.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.example.realestate_api.dto.XmlApiResponseDto;
import com.example.realestate_api.entity.RealEstateTransaction;

public interface RealEstateTransactionService {

    public void fetchAndSaveAllRegionsTransactions();

    public CompletableFuture<Void> fetchAndSaveTransactionsAsync(String lawdCd);

    public void fetchAndSaveTransactions(String lawdCd, String dealYmd);
    
    public void saveTransactions(List<XmlApiResponseDto.Item> itemList, String lawdCd, String dealYmd);

    public List<RealEstateTransaction> getTransactions(String lawdCd, String dealYmd);

}
