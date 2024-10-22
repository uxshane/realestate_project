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
    private int buildYear;

    @Column(name = "buyer_gbn")
    private String buyerGbn;

    @Column(name = "deal_amount")
    private String dealAmount;

    @Column(name = "deal_day")
    private int dealDay;

    @Column(name = "deal_month")
    private int dealMonth;

    @Column(name = "deal_year")
    private int dealYear;

    @Column(name = "dealing_gbn")
    private String dealingGbn;

    @Column(name = "estate_agent_sgg_nm")
    private String estateAgentSggNm;

    @Column(name = "exclu_use_ar")
    private double excluUseAr;

    @Column(name = "floor")
    private int floor;

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

}