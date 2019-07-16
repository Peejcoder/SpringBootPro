package com.ccic.payroll.authentication;

import com.ccic.payroll.common.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class AlertAuthenticationProvider implements AuthenticationProvider {
	private static Logger log = LoggerFactory.getLogger(AlertAuthenticationProvider.class);
	private final static String USER_ROLE = "USER";

	@Autowired
	public IAlertAuthenticationProviderService authenticationService;

	public AlertAuthenticationProvider() {
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String loginId = authentication.getName();
		Object credentials = authentication.getCredentials();
		log.info("authenticate................");
		if (!(credentials instanceof String)) {
			return null;
		}
		String password_no = credentials.toString();
		String password = MD5.getMD5(password_no);

		try {
			AuthenticationUserRequest authenticationUserRequest = new AuthenticationUserRequest();
			AuthenticationUserResponse authenticationUserResponse = new AuthenticationUserResponse();
			authenticationUserRequest.setLoginId(loginId);
			authenticationUserRequest.setPassword(password);
			authenticationService.authenticationUser(authenticationUserRequest, authenticationUserResponse);
			if (!authenticationUserResponse.isSuccess()) {
				log.info("错误信息" + authenticationUserResponse.getDescription());
				throw new BadCredentialsException(authenticationUserResponse.getDescription());
			}

			List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
			grantedAuthorities.add(new SimpleGrantedAuthority(USER_ROLE));

			AlertAuthenticationToken auth = new AlertAuthenticationToken(loginId, authentication.getCredentials(),
					grantedAuthorities);

			auth.setUserId(authenticationUserResponse.getUserId());
			auth.setLoginId(authenticationUserResponse.getLoginId());
			auth.setUserType(authenticationUserResponse.getUserType());
			auth.setRealName(authenticationUserResponse.getRealName());
			return auth;
		} catch (Exception e) {
			throw new BadCredentialsException(e.getLocalizedMessage());
		}

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(AlertAuthenticationToken.class);
	}

}
