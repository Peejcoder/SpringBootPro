package com.ccic.payroll.home;

import com.ccic.payroll.common.AlertAuthenticationLoginObject;
import com.ccic.payroll.common.AlertBaseController;
import com.ccic.payroll.common.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends AlertBaseController {
    private static Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    AlertAuthenticationLoginObject loginAuthentication;

    @RequestMapping(value = {"/","/index"})
    public ModelAndView indexHome() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("home");
        mv.addObject("UserInfo", UserUtils.getAuthToken());
        return mv;
    }

    @RequestMapping(value = "/login")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login");
        mv.addObject("loginMsg", loginAuthentication);
        return mv;
    }

}
