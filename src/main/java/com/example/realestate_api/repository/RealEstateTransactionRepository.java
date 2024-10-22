package com.example.realestate_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.realestate_api.entity.RealEstateTransaction;

@Repository
public interface RealEstateTransactionRepository extends JpaRepository<RealEstateTransaction, Long>{
    
    boolean existsByAptNmAndDealYearAndDealMonthAndDealDay(String aptNm, int dealYear, int dealMonth, int dealDay);

}
