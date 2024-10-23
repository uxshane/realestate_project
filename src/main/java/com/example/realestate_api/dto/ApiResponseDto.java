package com.example.realestate_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class ApiResponseDto {

    @JacksonXmlProperty(localName = "header")
    private Header header;

    @JacksonXmlProperty(localName = "body")
    private Body body;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Header {
        @JacksonXmlProperty(localName = "resultCode")
        private String resultCode;

        @JacksonXmlProperty(localName = "resultMsg")
        private String resultMsg;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Body {
        @JacksonXmlProperty(localName = "items")
        private Items items;

        @JacksonXmlProperty(localName = "numOfRows")
        private int numOfRows;

        @JacksonXmlProperty(localName = "pageNo")
        private int pageNo;

        @JacksonXmlProperty(localName = "totalCount")
        private int totalCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Items {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        private List<Item> itemList;  // 단일 객체 대신 리스트로 변경
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Item {
        @JacksonXmlProperty(localName = "aptDong")
        private String aptDong;

        @JacksonXmlProperty(localName = "aptNm")
        private String aptNm;

        @JacksonXmlProperty(localName = "buildYear")
        private int buildYear;  // buildYear를 int로 변경

        @JacksonXmlProperty(localName = "buyerGbn")
        private String buyerGbn;

        @JacksonXmlProperty(localName = "cdealDay")
        private String cdealDay;

        @JacksonXmlProperty(localName = "cdealType")
        private String cdealType;

        @JacksonXmlProperty(localName = "dealAmount")
        private String dealAmount;

        @JacksonXmlProperty(localName = "dealDay")
        private int dealDay;  // dealDay를 int로 변경

        @JacksonXmlProperty(localName = "dealMonth")
        private int dealMonth;  // dealMonth를 int로 변경

        @JacksonXmlProperty(localName = "dealYear")
        private int dealYear;  // dealYear를 int로 변경

        @JacksonXmlProperty(localName = "dealingGbn")
        private String dealingGbn;

        @JacksonXmlProperty(localName = "estateAgentSggNm")
        private String estateAgentSggNm;

        @JacksonXmlProperty(localName = "excluUseAr")
        private double excluUseAr;  // excluUseAr를 double로 변경

        @JacksonXmlProperty(localName = "floor")
        private int floor;  // floor를 int로 변경

        @JacksonXmlProperty(localName = "jibun")
        private String jibun;

        @JacksonXmlProperty(localName = "landLeaseholdGbn")
        private String landLeaseholdGbn;

        @JacksonXmlProperty(localName = "rgstDate")
        private String rgstDate;

        @JacksonXmlProperty(localName = "sggCd")
        private String sggCd;

        @JacksonXmlProperty(localName = "slerGbn")
        private String slerGbn;

        @JacksonXmlProperty(localName = "umdNm")
        private String umdNm;
    }
}
