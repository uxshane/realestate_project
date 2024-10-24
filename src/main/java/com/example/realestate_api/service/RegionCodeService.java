package com.example.realestate_api.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.realestate_api.entity.RegionCode;
import com.example.realestate_api.repository.RegionCodeRepository;

@Service
public class RegionCodeService {
    private final RegionCodeRepository regionCodeRepository;

    public RegionCodeService(RegionCodeRepository regionCodeRepository) {
        this.regionCodeRepository = regionCodeRepository;
    }

    public void saveRegionCodes(List<RegionCode> regionCodes) {
        regionCodeRepository.saveAll(regionCodes);
    }
    
    // 스프레드시트 데이터를 DB에 저장하는 로직
    public void storeSpreadsheetData(MultipartFile file) throws IOException{
        List<RegionCode> regionCodes = new ArrayList<>();
        // 스프레드시트에서 데이터를 읽어와서 리스트에 추가
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                String lawdCd = columns[0].substring(0, 5);
                String regionName = columns[1];
                regionCodes.add(new RegionCode(lawdCd, regionName));
            }
        }
        // DB에 저장
        saveRegionCodes(regionCodes);
    }
}
