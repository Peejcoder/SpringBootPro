package com.ccic.payroll.service;

import com.ccic.payroll.entity.UserBasicInfo;
import com.ccic.payroll.entity.UserInfo;
import com.ccic.payroll.entity.UserSalaryInfo;

import java.sql.SQLException;
import java.util.List;

public interface IPayrollService {
    List<UserInfo> getUserInfo(UserBasicInfo userInfo) throws SQLException;
    boolean updateUserInfo(List<UserInfo> userLst) throws SQLException;
    List<UserSalaryInfo> getSalaryInfo(UserBasicInfo userInfo) throws SQLException;
    boolean updateSalaryInfo(List<UserSalaryInfo> userLst) throws SQLException;
    boolean insertWorkInfo(String workMonth, String nextMoth) throws SQLException;
    boolean calSalary(String workMonth) throws SQLException;
    boolean getWorkDaysMoney(String workMonth, String from_date, String to_date) throws SQLException;
    boolean dailyExport(String reportDate) throws SQLException;
}
