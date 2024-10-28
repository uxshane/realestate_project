package com.example.realestate_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.realestate_api.entity.RealEstateTransaction;
import java.util.List;

public interface RealEstateTransactionRepository extends JpaRepository<RealEstateTransaction, Long>{
    
    boolean existsByAptNmAndDealMonthAndDealDayAndFloorAndDealAmount(String aptNm, String dealMonth, String dealDay, int floor, String dealAmount);

    List<RealEstateTransaction> findByLawdCdAndDealYmd(String lawdCd, String dealYmd);

}
