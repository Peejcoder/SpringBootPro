package com.ccic.payroll.task;

import com.ccic.payroll.service.IPayrollService;
import com.ccic.payroll.service.PayrollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class SchedulerTask {
    @Autowired
    private IPayrollService iPayrollService;
    private static final Logger log = LoggerFactory.getLogger(SchedulerTask.class);
    @Scheduled(cron = "0 0 2 26 * ?")
    private void process1() throws SQLException {
        Calendar c=java.util.Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH )+1;
        String calMonth = "";
        if(month>9){
            calMonth = year + "-" + month;
        }else{
            calMonth = year + "-0" + month;
        }
        String startDate = "";
        if(month == 1){
            startDate = (year-1) + "-12-26";
        }else{
            if(month>10){
                startDate = year + "-" + (month-1) + "-26";
            }else{
                startDate = year + "-0" + (month-1) + "-26";
            }
        }
        String endDate = "";
        if(month>9){
            endDate = year + "-" + month + "-25";
        }else{
            endDate = year + "-0" + month + "-25";
        }

        String nextMonth = "";
        if(month == 12){
            nextMonth = (year+1) + "-01-26";
        }else{
            if(month>8){
                nextMonth = year + "-" + (month+1);
            }else{
                nextMonth = year + "-0" + (month+1);
            }
        }

        System.out.println(calMonth+"  "+startDate+"  "+endDate+ "  "+nextMonth);

        if(!iPayrollService.getWorkDaysMoney(calMonth
                , startDate, endDate)){
            log.info("schedule getWorkDaysMoney failure.....");
        }
        if(!iPayrollService.calSalary(calMonth)){
            log.info("schedule calSalary failure.....");
        }
        if(!iPayrollService.insertWorkInfo(calMonth, nextMonth)){
            log.info("schedule insertWorkInfo failure.....");
        }
    }

    @Scheduled(cron = "0 0 6 * * ?")
    private void process2() throws SQLException {
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date d=cal.getTime();


        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
        String exportDate=sp.format(d);//获取昨天日期

        System.out.println("export date--->" + exportDate);

        if(!iPayrollService.dailyExport(exportDate)){
            log.info("schedule dailyExport failure.....");
        }
    }
}