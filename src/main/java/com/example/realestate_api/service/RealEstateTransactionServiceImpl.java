package com.example.realestate_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.realestate_api.dto.ApiResponseDto;
import com.example.realestate_api.dto.ApiResponseDto.Item;
import com.example.realestate_api.entity.RealEstateTransaction;
import com.example.realestate_api.repository.RealEstateTransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class RealEstateTransactionServiceImpl implements RealEstateTransactionService{

    private final RealEstateTransactionRepository realEstateTransactionRepository;

    public RealEstateTransactionServiceImpl(RealEstateTransactionRepository realEstateTransactionRepository) {
        this.realEstateTransactionRepository = realEstateTransactionRepository;
    }

    @Transactional
    @Override
    public void saveTransactions(List<Item> itemList) {
        for (ApiResponseDto.Item item : itemList) {
            // 중복 데이터 확인 (예: 동일한 아파트 이름, 거래 연월일을 기준으로 확인)
            boolean exists = realEstateTransactionRepository.existsByAptNmAndDealYearAndDealMonthAndDealDay(
                    item.getAptNm(), item.getDealYear(), item.getDealMonth(), item.getDealDay());

            if (!exists) {
                // 중복되지 않은 경우에만 저장
                RealEstateTransaction transaction = new RealEstateTransaction();
                transaction.setAptDong(item.getAptDong());
                transaction.setAptNm(item.getAptNm());
                transaction.setBuildYear(item.getBuildYear());
                transaction.setBuyerGbn(item.getBuyerGbn());
                transaction.setDealAmount(item.getDealAmount());
                transaction.setDealDay(item.getDealDay());
                transaction.setDealMonth(item.getDealMonth());
                transaction.setDealYear(item.getDealYear());
                transaction.setDealingGbn(item.getDealingGbn());
                transaction.setEstateAgentSggNm(item.getEstateAgentSggNm());
                transaction.setExcluUseAr(item.getExcluUseAr());
                transaction.setFloor(item.getFloor());
                transaction.setJibun(item.getJibun());
                transaction.setLandLeaseholdGbn(item.getLandLeaseholdGbn());
                transaction.setRgstDate(item.getRgstDate());
                transaction.setSggCd(item.getSggCd());
                transaction.setSlerGbn(item.getSlerGbn());
                transaction.setUmdNm(item.getUmdNm());

                // 저장
                realEstateTransactionRepository.save(transaction);
            }
        }
    }
}
