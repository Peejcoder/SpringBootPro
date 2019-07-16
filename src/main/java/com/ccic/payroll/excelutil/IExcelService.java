package com.ccic.payroll.excelutil;


import com.ccic.payroll.entity.UserInfo;
import com.ccic.payroll.entity.UserSalaryInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IExcelService {
    String exportSalaryXls(List<UserSalaryInfo> list, String filename, HttpServletResponse response);
    String exportPersonXls(List<UserInfo> list,String filename,HttpServletResponse response);
    List<UserInfo> importExcel(File file) throws IOException;
}
