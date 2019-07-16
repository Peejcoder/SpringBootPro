package com.ccic.payroll.authentication;

public interface IAlertAuthenticationProviderService {
	void authenticationUser(final AuthenticationUserRequest request, final AuthenticationUserResponse response)
			throws Exception;
}
