package com.example.realestate_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.realestate_api.entity.RealEstateTransaction;
import java.util.List;

public interface RealEstateTransactionRepository extends JpaRepository<RealEstateTransaction, Long>{
    
    boolean existsByAptNmAndDealYearAndDealMonthAndDealDay(String aptNm, int dealYear, int dealMonth, int dealDay);

    List<RealEstateTransaction> findByLawdCdAndDealYmd(String lawdCd, String dealYmd);

}
