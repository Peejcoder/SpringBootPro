package com.ccic.payroll.excelutil;

import com.ccic.payroll.entity.LevelType;
import com.ccic.payroll.entity.UserInfo;
import com.ccic.payroll.entity.UserSalaryInfo;
import com.ccic.payroll.entity.WorkType;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ExcelService implements IExcelService {
    private static final Logger log = LoggerFactory.getLogger(ExcelService.class);

    /**
     * 输出方法
     *
     * @param response
     * @return
     */
    @Override
    public String exportSalaryXls(List<UserSalaryInfo> list, String fileName, HttpServletResponse response) {
        try {
            List<Map<String, Object>> maplist = createExcelSalary(list);
            String columnNames[] = {"工作时间", "Pos登录名", "姓名", "管理站编号", "岗位工资", "'司龄工资", "加班工资", "月任务工资", "规范工资", "夜班补贴", "总工资"};//列名
            String keys[] = {"workMonth", "userName", "realName", "orgId", "salary1", "salary2", "salary3", "salary4", "salary5", "salary6", "totalSalary"};//map中的key
            ExcelUtil.downloadWorkBook(maplist, keys, columnNames, fileName, response);
        } catch (IOException e) {
            log.info("export failure ..." + e.getLocalizedMessage());
        }
        return "excel";
    }

    /**
     * 输出方法
     *
     * @param response
     * @return
     */
    @Override
    public String exportPersonXls(List<UserInfo> list, String fileName, HttpServletResponse response) {
        try {
            List<Map<String, Object>> maplist = createExcelPerson(list);
            String columnNames[] = {"工作时间", "Pos登录名", "姓名", "管理站编号", "当月任务", "当月应上班天数", "当月上班类型", "星级", "司龄", "入职时间", "否为返聘"};//列名
            String keys[] = {"workMonth", "userName", "realName", "orgId", "task", "workDays", "workType", "level", "workAge", "enterDate", "rework"};//map中的key
            ExcelUtil.downloadWorkBook(maplist, keys, columnNames, fileName, response);
        } catch (IOException e) {
            log.info("export failure ..." + e.getLocalizedMessage());
        }
        return "excel";
    }

    /**
     * 创建工资Excel表中的记录
     *
     * @param arlist
     * @return
     */
    private List<Map<String, Object>> createExcelSalary(List<UserSalaryInfo> arlist) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("sheetName", "sheet1");
            listmap.add(map);
            for (int j = 0; j < arlist.size(); j++) {
                UserSalaryInfo item = arlist.get(j);
                Map<String, Object> mapValue = new HashMap<String, Object>();
                mapValue.put("workMonth", item.getWorkMonth());
                mapValue.put("userName", item.getUserName());
                mapValue.put("realName", item.getRealName());
                mapValue.put("orgId", item.getOrgId());
                mapValue.put("salary1", item.getSalary1());
                mapValue.put("salary2", item.getSalary2());
                mapValue.put("salary3", item.getSalary3());
                mapValue.put("salary4", item.getSalary4());
                mapValue.put("salary5", item.getSalary5());
                mapValue.put("salary6", item.getSalary6());
                mapValue.put("totalSalary", item.getTotalSalary());
//                String attachmentURL = projectAuditListVo.getAttachment()==null?"无": FileUtil.getUploadPath()+projectAuditListVo.getAttachment();

                listmap.add(mapValue);
            }
        } catch (Exception e) {
            log.info("createExcelSalary failure ..." + e.getLocalizedMessage());
        }
        return listmap;
    }

    /**
     * 创建用户Excel表中的记录
     *
     * @param arlist
     * @return
     */
    private List<Map<String, Object>> createExcelPerson(List<UserInfo> arlist) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("sheetName", "sheet1");
            listmap.add(map);
            for (int j = 0; j < arlist.size(); j++) {
                UserInfo item = arlist.get(j);

                Map<String, Object> mapValue = new HashMap<String, Object>();
                mapValue.put("workMonth", item.getWorkMonth());
                mapValue.put("userName", item.getUserName());
                mapValue.put("realName", item.getRealName());
                mapValue.put("orgId", item.getOrgId());
                mapValue.put("task", item.getTask());
                mapValue.put("workDays", item.getWorkDays());
                mapValue.put("workType", WorkType.getName(item.getWorkType()));
                mapValue.put("level", LevelType.getName(item.getLevel()));
                mapValue.put("workAge", item.getWorkAge());
                mapValue.put("enterDate", item.getEnterDate());
                mapValue.put("rework", item.getRework() == 1 ? "是" : "否");
//                String attachmentURL = projectAuditListVo.getAttachment()==null?"无": FileUtil.getUploadPath()+projectAuditListVo.getAttachment();

                listmap.add(mapValue);
            }
        } catch (Exception e) {
            log.info("createExcelPerson failure ..." + e.getLocalizedMessage());
        }
        return listmap;
    }

    @Override
    public List<UserInfo> importExcel(File file) throws IOException {
        List<UserInfo> list = new ArrayList<UserInfo>();
        //创建Excel，读取文件内容
        HSSFWorkbook workbook = new HSSFWorkbook(FileUtils.openInputStream(file));

        //获取第一个工作表
        HSSFSheet sheet = workbook.getSheet("学员信息");

        //获取sheet中第一行行号
        int firstRowNum = sheet.getFirstRowNum();
        //获取sheet中最后一行行号
        int lastRowNum = sheet.getLastRowNum();

        try {
            //循环插入数据
            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                HSSFRow row = sheet.getRow(i);

                UserInfo userInfo = new UserInfo();

                HSSFCell calMonthCell = row.getCell(0);//time
//                if(calMonthCell!=null){
//                    calMonthCell.setCellType(Cell.CELL_TYPE_STRING);
//                    userInfo.setCalMonth(calMonthCell.getStringCellValue());
//                }
//
//                HSSFCell nameCell = row.getCell(1);//name
//                if(nameCell!=null){
//                    nameCell.setCellType(Cell.CELL_TYPE_STRING);
//                    userInfo.setUserName((nameCell.getStringCellValue()));
//                }
//
//                HSSFCell levelCell = row.getCell(2);//time
//                if(levelCell!=null){
//                    levelCell.setCellType(Cell.CELL_TYPE_STRING);
//                    userInfo.setUserLevel(levelCell.getStringCellValue());
//                }
//
//                HSSFCell ageCell = row.getCell(3);//name
//                if(ageCell!=null){
//                    ageCell.setCellType(Cell.CELL_TYPE_STRING);
//                    userInfo.setWorkAge((ageCell.getStringCellValue()));
//                }

                list.add(userInfo);
            }
            //usersMapper.insert(list);//往数据库插入数据
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }
        return list;
    }
}
