package com.example.realestate_api.transaction.transaction_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "real_estate_transaction", indexes = {
    @Index(name = "idx_aptNm_dealMonth_dealDay_floor_dealAmount", 
           columnList = "apt_name, deal_month, deal_day, floor, deal_amount")
})
public class RealEstateTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sgg_cd", length = 5)
    private String sggCd;

    @Column(name = "umd_cd", length = 5)
    private String umdCd;

    @Column(name = "land_cd", length = 1)
    private String landCd;

    @Column(name = "bonbun", length = 4)
    private String bonbun;

    @Column(name = "bubun", length = 4)
    private String bubun;

    @Column(name = "road_nm", length = 100)
    private String roadNm;

    //ssgCd, lawdCd와 중복된 값
    @Column(name = "road_nm_sgg_cd", length = 5)
    private String roadNmSggCd;

    @Column(name = "road_nm_cd", length = 7)
    private String roadNmCd;
    
    @Column(name = "road_nm_seq", length = 2)
    private String roadNmSeq;

    @Column(name = "road_nmb_cd", length = 1)
    private String roadNmbCd;

    @Column(name = "road_nm_bonbun", length = 5)
    private String roadNmBonbun;

    @Column(name = "road_nm_bubun", length = 5)
    private String roadNmBubun;

    @Column(name = "umd_nm", length = 60)
    private String umdNm;

    @Column(name = "apt_name", length = 100)
    private String aptNm;

    @Column(name = "jibun", length = 20)
    private String jibun;

    @Column(name = "exclu_use_ar", length = 22)
    private double excluUseAr;

    @Column(name = "deal_year", length = 4)
    private String dealYear;

    @Column(name = "deal_month", length = 2)
    private String dealMonth;

    @Column(name = "deal_day", length = 2)
    private String dealDay;

    @Column(name = "deal_amount", length = 40)
    private String dealAmount;

    @Column(name = "floor", length = 10)
    private int floor;

    @Column(name = "build_year", length = 4)
    private int buildYear;

    @Column(name = "apt_seq", length = 20)
    private String aptSeq;

    @Column(name = "cdeal_type", length = 1)
    private String cdealType;

    @Column(name = "cdeal_day", length = 8)
    private String cdealDay;

    @Column(name = "dealing_gbn", length = 10)
    private String dealingGbn;

    @Column(name = "estate_agent_sgg_nm", length = 3000)
    private String estateAgentSggNm;

    @Column(name = "rgst_date", length = 8)
    private String rgstDate;

    @Column(name = "apt_dong", length = 400)
    private String aptDong;

    @Column(name = "sler_gbn", length = 100)
    private String slerGbn;

    @Column(name = "buyer_gbn", length = 100)
    private String buyerGbn;

    @Column(name = "land_leasehold_gbn", length = 1)
    private String landLeaseholdGbn;

    @Column(name = "lawd_cd")
    private String lawdCd;

    @Column(name = "deal_ymd")
    private String dealYmd;
}
