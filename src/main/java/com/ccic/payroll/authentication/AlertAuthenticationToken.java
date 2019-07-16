package com.ccic.payroll.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AlertAuthenticationToken extends UsernamePasswordAuthenticationToken {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String loginId;
	private int userType;
	private String realName;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public AlertAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}

	public AlertAuthenticationToken(Object principal, Object credentials,
									Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

}
