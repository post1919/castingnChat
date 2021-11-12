package com.castingn.login.dto;


import com.castingn.common.dto.PlazaDefaultDto;
import com.castingn.common.dto.ShopListDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Data
@SuppressWarnings("serial")
@ToString(callSuper = true)
public class CustomUserDetailsDto extends LoginDto implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	private int enabled;

	private String type;
	private String loc;
	private String msg;

	//USER_INFO 추가
	private String uIdHide;

	//member
	private String compId;
	private String compName;
	private String memId;
	private String memName;
	private int memLevel;
	private int subLevel;
	private String comp;
	private String mobile;
	private String phone;
	private int userLevel;
	private int userPk;
	private String cono1;
	private String cono2;
	private String cono3;

	//sub_member
	private String companyId;
	private String companyQuery;
	private String companyResult;
	private String companyRow;

	//Member_Level
	private String membership;

	private String[] works;

	private ShopListDto shopList;
	private PlazaDefaultDto PlazaDefault;

	//USER_SNS ------ 이걸로 사용함 20211025
	private String snsId;
	private String snsType;
	private String snsEmail;
	private String snsEmailHide;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
	private String indate;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		authList.add(new SimpleGrantedAuthority(super.getRoleKey()));
		return authList;
	}

	@Override
	public String getPassword() { return uPasswd; }

	@Override
	public String getUsername() {
		return uId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled==1?true:false;
	}
}