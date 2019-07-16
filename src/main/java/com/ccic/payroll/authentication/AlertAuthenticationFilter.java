package com.ccic.payroll.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AlertAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static Logger log = LoggerFactory.getLogger(AlertAuthenticationFilter.class);

	public static final String QTS_SECURITY_FORM_USERNAME_KEY = "username";
	public static final String QTS_SECURITY_FORM_PASSWORD_KEY = "password";

	private String usernameParameter = QTS_SECURITY_FORM_USERNAME_KEY;
	private String passwordParameter = QTS_SECURITY_FORM_PASSWORD_KEY;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		AlertAuthenticationToken authRequest = getAuthRequest(request);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

	private AlertAuthenticationToken getAuthRequest(HttpServletRequest request) {
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		return new AlertAuthenticationToken(username, password);
	}

	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(passwordParameter);
	}

	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(usernameParameter);
	}

}
