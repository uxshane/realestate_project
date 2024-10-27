package com.example.realestate_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.realestate_api.service.RegionCodeService;

@RestController
public class RegionCodeController {

    private final RegionCodeService regionCodeService;

    public RegionCodeController(RegionCodeService regionCodeService) {
        this.regionCodeService = regionCodeService;
    }

    // 파일 업로드를 처리하는 엔드포인트
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            regionCodeService.storeSpreadsheetData(file);  // 파일 저장 로직 호출
            return ResponseEntity.ok("법정동명 파일 업로드 및 데이터 저장 성공");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("파일 업로드 중 오류 발생: " + e.getMessage());
        }
    }
    
}
