package com.ccic.payroll.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;


@Service
public class AlertAuthenticationProviderService implements IAlertAuthenticationProviderService {

	@Autowired
	public IAlertAuthenticationProviderDao AuthenticationDao;

	@Override
	public void authenticationUser(AuthenticationUserRequest request, AuthenticationUserResponse response)
			throws Exception {
		try {
			AuthenticationDao.authenticationUser(request, response);
		} catch (SQLException e) {
			throw new Exception(e.getLocalizedMessage());
		}
	}

}
