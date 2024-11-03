package com.example.realestate_api.region.region_repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.realestate_api.region.region_entity.RegionCode;

public interface RegionCodeRepository extends JpaRepository<RegionCode, String>{

    
    
}
