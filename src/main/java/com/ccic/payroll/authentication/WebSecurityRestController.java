package com.ccic.payroll.authentication;

import com.ccic.payroll.common.BaseResposne;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSecurityRestController {
	private static final String NO_LOGIN = "没有登录，请先登录。";

	@RequestMapping(value = "/nologin", produces = "application/json;charset=UTF-8")
	public BaseResposne nologin() {
		BaseResposne response = new BaseResposne();
		response.setDescription(NO_LOGIN);
		response.setSuccess(false);
		return response;
	}
}
