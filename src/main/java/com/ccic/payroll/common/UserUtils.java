package com.ccic.payroll.common;

import com.ccic.payroll.authentication.AlertAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {
	/**
	 * getAuthToken Get current authenticate token
	 * 
	 * @return AlertAuthenticationToken
	 */
	public static AlertAuthenticationToken getAuthToken() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (null == auth) {
			System.out.println("null == auth.............");
		} else if (auth instanceof AlertAuthenticationToken) {
			return ((AlertAuthenticationToken) auth);
		}
		return null;
	}

}
