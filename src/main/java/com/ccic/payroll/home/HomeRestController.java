package com.ccic.payroll.home;

import com.ccic.payroll.common.BaseResposne;
import com.ccic.payroll.common.PageLimitRequest;
import com.ccic.payroll.common.UserUtils;
import com.ccic.payroll.entity.UserBasicInfo;
import com.ccic.payroll.entity.UserInfo;
import com.ccic.payroll.entity.UserType;
import com.ccic.payroll.entity.UserSalaryInfo;
import com.ccic.payroll.excelutil.IExcelService;
import com.ccic.payroll.service.IPayrollService;
import com.ccic.payroll.service.PayrollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@RestController
@RequestMapping(value = {"/api/home/", "/payroll/api/home/"})
public class HomeRestController {
    private static final Logger log = LoggerFactory.getLogger(HomeRestController.class);
    private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private IPayrollService iPayrollService;
    @Autowired
    private IExcelService iExcelService;

    /**
     * 导出人员信息调用controller
     *
     * @param userName
     * @param realName
     * @param orgId
     * @param workMonth
     * @param pageNum
     * @param pageSize
     * @param response
     */
    @RequestMapping(value = "exportPersonXls", produces = {"application/vnd.ms-excel;charset=UTF-8"})
    public void exportPersonXls(@RequestParam("userName") String userName,
                                @RequestParam("realName") String realName,
                                @RequestParam("orgId") int orgId,
                                @RequestParam("workMonth") String workMonth,
                                @RequestParam("pageNum") int pageNum,
                                @RequestParam("pageSize") int pageSize,
                                HttpServletResponse response) {
        UserBasicInfo request = new UserBasicInfo();
        request.setUserName(userName);
        request.setRealName(realName);
        request.setOrgId(orgId);
        request.setWorkMonth(workMonth);
        request.setPageNum(0);
        request.setPageSize(10000);//默认导出10000

        log.info("exportPersonXls........" + request.getUserName());
        List<UserInfo> userInfos;
        try {
            userInfos = iPayrollService.getUserInfo(request);
            String fileName = "人员信息表" + "—" + workMonth;
            iExcelService.exportPersonXls(userInfos, fileName, response);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("exportPersonXls error:" + e.toString());
        }
    }


    @RequestMapping(value = "exportSalaryXls", produces = {"application/vnd.ms-excel;charset=UTF-8"})
    public void exportSalaryXls(@RequestParam("userName") String userName,
                                @RequestParam("realName") String realName,
                                @RequestParam("orgId") int orgId,
                                @RequestParam("workMonth") String workMonth,
                                @RequestParam("pageNum") int pageNum,
                                @RequestParam("pageSize") int pageSize,
                                HttpServletResponse response) {
        UserBasicInfo request = new UserBasicInfo();
        request.setUserName(userName);
        request.setRealName(realName);
        request.setOrgId(orgId);
        request.setWorkMonth(workMonth);
        request.setPageNum(0);
        request.setPageSize(10000);//默认导出10000

        log.info("exportSalaryXls........" + request.getUserName());
        List<UserSalaryInfo> salaryInfos;
        try {
            salaryInfos = iPayrollService.getSalaryInfo(request);
            String fileName = "工资信息表" + "—" + workMonth;
            iExcelService.exportSalaryXls(salaryInfos, fileName, response);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("exportPersonXls error:" + e.toString());
        }
    }

    /**
     * 工资信息controller
     */
    @RequestMapping(value = "getSalary", produces = {"application/json;charset=UTF-8"})
    public List<UserSalaryInfo> getSalary(@RequestBody UserBasicInfo info) {
        List<UserSalaryInfo> userInfos = new ArrayList<>();
        try {
            userInfos = iPayrollService.getSalaryInfo(info);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("getSalaryInfo error:" + e.toString());
        }
        return userInfos;
    }

    @RequestMapping(value = "updateSalaryInfo", produces = {"application/json;charset=UTF-8"})
    public boolean updateSalaryInfo(@RequestBody List<UserSalaryInfo> userLst) {
        boolean result = false;
        if (UserUtils.getAuthToken().getUserType() != UserType.admin.getIndex()) {
            log.info(UserUtils.getAuthToken().getRealName() + "无权限updateSalaryInfo");
            return false;
        }
        try {
            result = iPayrollService.updateSalaryInfo(userLst);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("updateSalaryInfo error:" + e.toString());
        }
        return result;
    }

    /**
     * 人员信息controller
     */
    @RequestMapping(value = "getUserInfo", produces = {"application/json;charset=UTF-8"})
    public List<UserInfo> getUserInfo(@RequestBody UserBasicInfo info) {
        List<UserInfo> userInfos = new ArrayList<>();
        try {
            userInfos = iPayrollService.getUserInfo(info);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("getUserInfo error:" + e.toString());
        }
        return userInfos;
    }

    @RequestMapping(value = "updateUserInfo", produces = {"application/json;charset=UTF-8"})
    public boolean updateUserInfo(@RequestBody List<UserInfo> userLst) {
        if (UserUtils.getAuthToken().getUserType() != UserType.admin.getIndex()) {
            log.info(UserUtils.getAuthToken().getRealName() + "无权限updateUserInfo");
            return false;
        }
        boolean result = false;
        try {
            result = iPayrollService.updateUserInfo(userLst);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("updateUserInfo error:" + e.toString());
        }
        return result;
    }

    @RequestMapping(value = "testCal", produces = {"application/json;charset=UTF-8"})
    public void testCal() throws SQLException {
        Calendar c = java.util.Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        String calMonth = "";
        if (month > 9) {
            calMonth = year + "-" + month;
        } else {
            calMonth = year + "-0" + month;
        }
        String startDate = "";
        if (month == 1) {
            startDate = (year - 1) + "-12-26";
        } else {
            if (month > 10) {
                startDate = year + "-" + (month - 1) + "-26";
            } else {
                startDate = year + "-0" + (month - 1) + "-26";
            }
        }
        String endDate = "";
        if (month > 9) {
            endDate = year + "-" + month + "-25";
        } else {
            endDate = year + "-0" + month + "-25";
        }

        String nextMonth = "";
        if (month == 12) {
            nextMonth = (year + 1) + "-01-26";
        } else {
            if (month > 8) {
                nextMonth = year + "-" + (month + 1);
            } else {
                nextMonth = year + "-0" + (month + 1);
            }
        }

        System.out.println(calMonth + "  " + startDate + "  " + endDate + "  " + nextMonth);

        if (!iPayrollService.getWorkDaysMoney(calMonth, startDate, endDate)) {
            log.info("schedule getWorkDaysMoney failure.....");
        }
        if (!iPayrollService.calSalary(calMonth)) {
            log.info("schedule calSalary failure.....");
        }
        if (!iPayrollService.insertWorkInfo(calMonth, nextMonth)) {
            log.info("schedule insertWorkInfo failure.....");
        }
    }

    @RequestMapping(value = "dailyExport", produces = {"application/json;charset=UTF-8"})
    public String dailyExport(@RequestParam("reportDate") String reportDate) throws SQLException {
        String result = "";
        try {
            iPayrollService.dailyExport(reportDate);
            result = "success";
        } catch (SQLException e) {
            log.error("getUserInfo error:" + e.toString());
            return "failed";
        }
        return result;
    }
}
