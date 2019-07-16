package com.ccic.payroll.home;

import com.ccic.payroll.common.AlertBaseController;
import com.ccic.payroll.common.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/","/payroll/"})
public class IndexController extends AlertBaseController {
    private static Logger log =  LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = {"wagesInfo"})
    public ModelAndView dataTable() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index_wages_temp");
        mv.addObject("UserInfo", UserUtils.getAuthToken());
        return mv;
    }

    @RequestMapping(value = {"personInfo"})
    public ModelAndView personTable() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index_person_info_temp");
        mv.addObject("UserInfo", UserUtils.getAuthToken());
        return mv;
    }

    @RequestMapping(value = {"home"})
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }
}
