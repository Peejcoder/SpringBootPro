package com.ccic.payroll.common;

import org.springframework.stereotype.Component;

@Component
public class AlertAuthenticationLoginObject {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
