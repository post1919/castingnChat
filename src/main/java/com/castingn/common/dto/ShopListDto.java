package com.castingn.common.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@SuppressWarnings("serial")
@ApiModel(description = "샵정보DTO")
public class ShopListDto implements Serializable {

    @ApiModelProperty(notes = "", value = "")
    private int uid;

    @ApiModelProperty(notes = "", value = "")
    private String memberGubun;

    @ApiModelProperty(notes = "", value = "")
    private int memberUid;

    @ApiModelProperty(notes = "", value = "")
    private String serverName;

    @ApiModelProperty(notes = "", value = "")
    private String domain;

    @ApiModelProperty(notes = "", value = "")
    private String dbName;

    @ApiModelProperty(notes = "", value = "")
    private String shopName;

    @ApiModelProperty(notes = "", value = "")
    private String shopUse;

    @ApiModelProperty(notes = "", value = "")
    private String manageUse;

    @ApiModelProperty(notes = "", value = "")
    private LocalDateTime regDate;

    @ApiModelProperty(notes = "", value = "")
    private String shopType;

    @ApiModelProperty(notes = "", value = "")
    private String shopStyle;

    @ApiModelProperty(notes = "", value = "")
    private String shopColor;

    @ApiModelProperty(notes = "", value = "")
    private String priceColor;

    @ApiModelProperty(notes = "", value = "")
    private String dbType;

    @ApiModelProperty(notes = "", value = "")
    private String feeGubun;

    @ApiModelProperty(notes = "", value = "")
    private int fee;

    @ApiModelProperty(notes = "서비스 시작일자", value = "서비스 시작일자")
    private LocalDateTime startDate;
}