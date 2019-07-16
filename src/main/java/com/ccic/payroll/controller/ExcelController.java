package com.ccic.payroll.controller;

import com.ccic.payroll.entity.UserInfo;
import com.ccic.payroll.excelutil.ExcelService;
import com.ccic.payroll.excelutil.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/excel/")
public class ExcelController {
    /**
     * 导入人员清单
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/importUsers", method = RequestMethod.POST)
    @ResponseBody
    public String importUsers(@RequestParam MultipartFile file) throws IOException{
        //把MultipartFile转化为File
        CommonsMultipartFile cmf= (CommonsMultipartFile)file;
        DiskFileItem dfi=(DiskFileItem) cmf.getFileItem();
        File fo=dfi.getStoreLocation();
        ExcelService xService = new ExcelService();
        List<UserInfo> userLst = xService.importExcel(fo);

        return "";
    }
}
