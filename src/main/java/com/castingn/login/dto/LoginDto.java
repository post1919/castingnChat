package com.castingn.login.dto;

import com.castingn.user.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LoginDto implements Serializable {

    private static final long serialVersionUID = 5177407244985764350L;

    //USER_INFO 항목들[s]
    protected int uPk;
    protected String rCdPk;
    protected String uName;

    //@NotNull(message = "아이디는 필수 값입니다.")
    protected String uId;

    //@NotNull(message = "비밀번호는 필수 값입니다.")
    protected String uPasswd;
    protected String uEmail;
    protected String uNewsletter;
    protected String uSnsType;
    protected String uSnsId;
    protected String uType;
    protected String uSex;
    protected String uBirthday;
    protected String uMobile;
    protected String uMobileCert;
    protected LocalDateTime uMobileCertDate;
    protected String uSms;
    protected String uPhone;
    protected String uZone1;
    protected String uZone2;
    protected String uZipcode;
    protected String uAddress;
    protected String uCompany;
    protected String uRegistrationNumber;
    protected String uFounder;
    protected String uDuty;
    protected String uDepartment;
    protected String uPosition;
    protected String uPicture;
    protected String uCompanyCeritify;
    protected LocalDateTime uCertifyDate;
    protected LocalDateTime uRegisterDate;
    protected LocalDateTime uConfirmDate;
    protected LocalDateTime uWithdrawDate;
    protected LocalDateTime uModifyDate;
    protected String uStatus;
    protected String uProjNum;
    protected String uProjContractNum;
    protected String uPorjIngNum;
    protected String uPorjDoneNum;
    protected String uProjTotalPrice;
    protected String uFrom;
    protected String uFromId;
    protected String uFromJoinDate;
    protected String uBid;
    protected String agentId;
    protected LocalDateTime uLogindate;
    protected String incode;
    protected String mocode;
    protected LocalDateTime modate;
    protected String uSsoId;
    protected String rUgPk;
    protected String uAdminUseYn;

    //Plaza_member에서 USER_INFO로 옮긴 칼럼들
    protected Integer memberLevel;
    protected String approval;
    protected int reserve;
    protected String license;
    protected String bizStatus;
    protected String bizSubject;
    protected String companyPhone;
    protected String companyFax;
    protected int credit;
    protected int bounds;
    protected String notify;
    protected int notifyBounds;
    protected String bank;
    protected String bankbook;
    protected String account;
    protected String depositor;
    protected String calcGbn;
    protected String calculate;
    protected String contract;
    protected String apprAuth;
    protected String secession;
    //protected LocalDateTime secessionDate; -> uWithdrawDate로 대체
    protected String secessionReason;
    protected String secessionMemo;
    protected String approvalAdmin;
    protected String joinAgreeTerms;
    protected String recommandId;
    //USER_INFO 항목들[e]

    //protected String memberId;     // '아이디',
    //protected String memberPw;     // '비밀번호',
    protected String cCode;     //
    protected String name;     // '이름',
    protected String email;     // '이메일',
    protected String zipCode;     // '우편번호',
    protected String address;     //
    protected String phoneNo;     // '전화번호',
    protected String autoMail;  // '메일수신여부',
    protected String code;            // '업체코드',
    protected String department;      //
    protected String depPosition;    //
    protected int level;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    protected LocalDateTime regDate; // '등록일',

    protected int msPk;
    protected String mApprovalAdmin; // '승인관리자',
    protected int mPk;

    protected String join_agree_privacy;
    protected String marketing_agree;
    protected String join_agree_marketing;
    protected LocalDateTime agree_indate;

    protected String userId;

    protected String sessionId;
    protected Role role;

    public LoginDto update(String name, String picture){
        this.uName = name;
        this.uPicture = picture;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
