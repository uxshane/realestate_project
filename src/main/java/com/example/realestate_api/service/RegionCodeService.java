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
    public void storeSpreadsheetData(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (!"text/csv".equals(contentType) && 
            !"application/vnd.ms-excel".equals(contentType) && 
            !"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            throw new IllegalArgumentException("잘못된 파일 형식입니다. CSV 또는 Excel 파일만 허용됩니다.");
        }
    
        List<RegionCode> regionCodes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true; // 첫 줄 여부를 나타내는 플래그
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) { // 첫 번째 줄(제목 행)은 건너뜁니다.
                    isFirstLine = false;
                    continue;
                }

                String[] columns = line.split(",");
                if (columns.length < 2) { // 열 개수 검증
                    throw new IllegalArgumentException("잘못된 데이터 형식입니다. 두 개의 열이 필요합니다.");
                }
                String lawdCd = columns[0].substring(0, 5);
                String regionName = columns[1];
                regionCodes.add(new RegionCode(lawdCd, regionName));
            }
        }
        saveRegionCodes(regionCodes);  // DB에 저장
    }
}
