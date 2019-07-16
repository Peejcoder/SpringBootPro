package com.ccic.payroll.authentication;

import java.sql.SQLException;

public interface IAlertAuthenticationProviderDao {
	void authenticationUser(final AuthenticationUserRequest request, final AuthenticationUserResponse response)
			throws SQLException;
}
