package com.example.realestate_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.realestate_api.entity.RealEstateTransaction;
import java.util.List;

public interface RealEstateTransactionRepository extends JpaRepository<RealEstateTransaction, Long>{
    
    boolean existsByAptNmAndDealYearAndDealMonthAndDealDayAndFloorAndDealAmount(String aptNm, String dealYear, String dealMonth, String dealDay, String floor, String dealAmount);

    List<RealEstateTransaction> findByLawdCdAndDealYmd(String lawdCd, String dealYmd);

}
