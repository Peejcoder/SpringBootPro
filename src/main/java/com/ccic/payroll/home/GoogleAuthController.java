package com.ccic.payroll.home;

import com.ccic.payroll.googleutil.GoogleAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/home/", "/payroll/api/home/"})
public class GoogleAuthController {

    private static final Logger log = LoggerFactory.getLogger(GoogleAuthController.class);
    private static String secret = "R2Q3S52RNXBTFTOM";

    /**
     * 生成验证信息
     */
    @RequestMapping(value = "getToken", produces = {"application/json;charset=UTF-8"})
    public String getToken() {
        //secret = GoogleAuthenticator.generateSecretKey();
        // 把这个qrcode生成二维码，用google身份验证器扫描二维码就能添加成功
        String qrcode = GoogleAuthenticator.getQRBarcode("123456", secret);
        return "" + qrcode + "    " + secret;
    }

    /**
     * 验证信息
     */
    @RequestMapping(value = "verfiy", produces = {"application/json;charset=UTF-8"})
    public String verfiy(@RequestParam("code") String code1) {
        long code = Long.valueOf(code1);
        long t = System.currentTimeMillis();
        GoogleAuthenticator ga = new GoogleAuthenticator();
        ga.setWindowSize(5);
        boolean r = ga.check_code(secret, code, t);
        return "" + r;
    }
}
