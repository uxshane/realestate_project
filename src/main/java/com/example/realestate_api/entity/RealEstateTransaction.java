package com.example.realestate_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "real_estate_transaction")
public class RealEstateTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "apt_dong")
    private String aptDong;

    @Column(name = "apt_name")
    private String aptNm;

    @Column(name = "build_year")
    private int buildYear;  // buildYear는 int형으로 유지

    @Column(name = "buyer_gbn")
    private String buyerGbn;

    @Column(name = "deal_amount")
    private String dealAmount;  // dealAmount는 String으로 저장

    @Column(name = "deal_day")
    private int dealDay;  // dealDay는 int로 저장

    @Column(name = "deal_month")
    private int dealMonth;  // dealMonth는 int로 저장

    @Column(name = "deal_year")
    private int dealYear;  // dealYear는 int로 저장

    @Column(name = "dealing_gbn")
    private String dealingGbn;

    @Column(name = "estate_agent_sgg_nm")
    private String estateAgentSggNm;

    @Column(name = "exclu_use_ar")
    private double excluUseAr;  // excluUseAr는 double로 저장

    @Column(name = "floor")
    private int floor;  // floor는 int로 저장

    @Column(name = "jibun")
    private String jibun;

    @Column(name = "land_leasehold_gbn")
    private String landLeaseholdGbn;

    @Column(name = "rgst_date")
    private String rgstDate;

    @Column(name = "sgg_cd")
    private String sggCd;

    @Column(name = "sler_gbn")
    private String slerGbn;

    @Column(name = "umd_nm")
    private String umdNm;

    @Column(name = "lawd_cd")
    private String lawdCd;  // 법정동 코드

    @Column(name = "deal_ymd")
    private String dealYmd;  // 거래 연월 (YYYYMM 형식)
}
