package com.castingn.common.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("serial")
@ApiModel(description = "샵기본DTO")
public class PlazaDefaultDto implements Serializable {
    @ApiModelProperty(notes = "IDX", value = "IDX")
    private int idx;

    @ApiModelProperty(notes = "상점명", value = "상점명")
    private String siteName;

    @ApiModelProperty(notes = "PK", value = "PK")
    private String adminId;

    @ApiModelProperty(notes = "PK", value = "PK")
    private String adminPassword;

    @ApiModelProperty(notes = "대표자명", value = "대표자명")
    private String siteTitle;

    @ApiModelProperty(notes = "관리자 Email", value = "관리자 Email")
    private String siteEmail;

    @ApiModelProperty(notes = "주문취소 수신 Email", value = "주문취소 수신 Email")
    private String receiveEmail;

    @ApiModelProperty(notes = "상점 홈페이지", value = "상점 홈페이지")
    private String siteHomepage;

    @ApiModelProperty(notes = "전화번호", value = "전화번호")
    private String siteTel;

    @ApiModelProperty(notes = "팩스번호", value = "팩스번호")
    private String siteFax;

    @ApiModelProperty(notes = "PK", value = "PK")
    private String metaTag;

    @ApiModelProperty(notes = "PK", value = "PK")
    private int expireOp;

    @ApiModelProperty(notes = "PK", value = "PK")
    private int expireCartTime;

    @ApiModelProperty(notes = "PK", value = "PK")
    private int expireOrderTime;

    @ApiModelProperty(notes = "배송비 상품총액", value = "배송비 상품총액")
    private int freedelivery;

    @ApiModelProperty(notes = "배송비", value = "배송비")
    private int deliveryFee;

    @ApiModelProperty(notes = "구매확정", value = "구매확정")
    private int deliveryConfirm;

    @ApiModelProperty(notes = "회원가입시 적립금", value = "회원가입시 적립금")
    private int joinPoint;

    @ApiModelProperty(notes = "사업자등록번호", value = "사업자등록번호")
    private String companyNo;

    @ApiModelProperty(notes = "통신판매번호", value = "통신판매번호")
    private String mailorderNo;

    @ApiModelProperty(notes = "우편번호", value = "우편번호")
    private String postSeq;

    @ApiModelProperty(notes = "주소", value = "주소")
    private String addr;

    @ApiModelProperty(notes = "상품헤더", value = "상품헤더")
    private String headeInfo;

    @ApiModelProperty(notes = "상품배송정책", value = "상품배송정책")
    private String dlvrPolicy;

    @ApiModelProperty(notes = "로고", value = "로고")
    private String siteLogo;

    @ApiModelProperty(notes = "근무시간1", value = "근무시간1")
    private String workDay1;

    @ApiModelProperty(notes = "근무시간2", value = "근무시간2")
    private String workDay2;

    @ApiModelProperty(notes = "휴무", value = "휴무")
    private String dayOff;

    @ApiModelProperty(notes = "네이버페이 사용유무", value = "네이버페이 사용유무")
    private String naverpayUse;

    @ApiModelProperty(notes = "네이버페이 상점 ID", value = "네이버페이 상점 ID")
    private String naverpayShopid;

    @ApiModelProperty(notes = "네이버페이 주문인증키", value = "네이버페이 주문인증키")
    private String naverpayOrderkey;

    @ApiModelProperty(notes = "네이버페이 Account ID", value = "네이버페이 Account ID")
    private String naverpayAccount; //Plaza_default,NAVERPAY_ACCOUNT,네이버페이 Account ID

    @ApiModelProperty(notes = "네이버페이 버튼인증키", value = "네이버페이 버튼인증키")
    private String naverpayButkey; //Plaza_default,NAVERPAY_BUTKEY,네이버페이 버튼인증키

    @ApiModelProperty(notes = "카드결제", value = "no:사용안함,inicis:이니시스,lguplus:KG U+")
    private String cardUse;

    @ApiModelProperty(notes = "카드결제 상점 ID", value = "카드결제 상점 ID")
    private String cardShopId;

    @ApiModelProperty(notes = "카드결제 상점명", value = "카드결제 상점명")
    private String cardShopName; //Plaza_default,CARD_SHOP_NAME,카드결제 상점명

    @ApiModelProperty(notes = "카드결제 MertKey", value = "카드결제 MertKey")
    private String cardMertkey;

    @ApiModelProperty(notes = "폐쇄몰지정", value = "폐쇄몰지정")
    private String mallClose;

    @ApiModelProperty(notes = "배너최대갯수(PC)", value = "배너최대갯수(PC)")
    private int maxBannerPc;

    @ApiModelProperty(notes = "배너최대갯수(모바일)", value = "배너최대갯수(모바일)")
    private int maxBannerMb;

    @ApiModelProperty(notes = "배너정보(PC)", value = "배너정보(PC)")
    private String infoBannerPc;

    @ApiModelProperty(notes = "배너정보(모바일)", value = "배너정보(모바일)")
    private String infoBannerMb;

    @ApiModelProperty(notes = "PK", value = "PK")
    private String keywordMethod;
}