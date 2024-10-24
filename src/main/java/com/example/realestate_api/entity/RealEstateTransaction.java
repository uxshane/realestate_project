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

    @Column(name = "sgg_cd")
    private String sggCd;

    @Column(name = "umd_cd")
    private String umdCd;

    @Column(name = "land_cd")
    private String landCd;

    @Column(name = "bonbun")
    private String bonbun;

    @Column(name = "bubun")
    private String bubun;

    @Column(name = "road_nm")
    private String roadNm;

    @Column(name = "road_nm_sgg_cd")
    private String roadNmSggCd;

    @Column(name = "road_nm_cd")
    private String roadNmCd;

    @Column(name = "road_nm_seq")
    private String roadNmSeq;

    @Column(name = "road_nmb_cd")
    private String roadNmbCd;

    @Column(name = "road_nm_bonbun")
    private String roadNmBonbun;

    @Column(name = "road_nm_bubun")
    private String roadNmBubun;

    @Column(name = "umd_nm")
    private String umdNm;

    @Column(name = "apt_name")
    private String aptNm;

    @Column(name = "jibun")
    private String jibun;

    @Column(name = "exclu_use_ar")
    private String excluUseAr;

    @Column(name = "deal_year")
    private String dealYear;

    @Column(name = "deal_month")
    private String dealMonth;

    @Column(name = "deal_day")
    private String dealDay;

    @Column(name = "deal_amount")
    private String dealAmount;

    @Column(name = "floor")
    private String floor;

    @Column(name = "build_year")
    private String buildYear;

    @Column(name = "apt_seq")
    private String aptSeq;

    @Column(name = "cdeal_type")
    private String cdealType;

    @Column(name = "cdeal_day")
    private String cdealDay;

    @Column(name = "dealing_gbn")
    private String dealingGbn;

    @Column(name = "estate_agent_sgg_nm")
    private String estateAgentSggNm;

    @Column(name = "rgst_date")
    private String rgstDate;

    @Column(name = "apt_dong")
    private String aptDong;

    @Column(name = "sler_gbn")
    private String slerGbn;

    @Column(name = "buyer_gbn")
    private String buyerGbn;

    @Column(name = "land_leasehold_gbn")
    private String landLeaseholdGbn;

    @Column(name = "lawd_cd")
    private String lawdCd;

    @Column(name = "deal_ymd")
    private String dealYmd;
}
