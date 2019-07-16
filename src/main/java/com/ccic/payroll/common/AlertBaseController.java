package com.ccic.payroll.common;

import com.ccic.payroll.authentication.AlertAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;


public class AlertBaseController {
	private static Logger log = LoggerFactory.getLogger(AlertBaseController.class);

	@ModelAttribute("authentication")
	public AlertAuthenticationToken authentication() {
		return UserUtils.getAuthToken();
	}

	@ModelAttribute("isLogin")
	public boolean isLogin() {
		if (null != UserUtils.getAuthToken()) {
			return true;
		} else {
			return false;
		}
	}
}
