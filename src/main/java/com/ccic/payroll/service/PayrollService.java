package com.ccic.payroll.service;

import com.ccic.payroll.dbutil.DataRecordSet;
import com.ccic.payroll.dbutil.MySQLDBHelper;
import com.ccic.payroll.dbutil.SQLArray;
import com.ccic.payroll.dbutil.SqlDataObject;
import com.ccic.payroll.entity.PosUserReport;
import com.ccic.payroll.entity.UserBasicInfo;
import com.ccic.payroll.entity.UserInfo;
import com.ccic.payroll.entity.UserSalaryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

@Repository
public class PayrollService extends MySQLDBHelper implements IPayrollService {
    private static Logger log = LoggerFactory.getLogger(PayrollService.class);

    //查找人员信息
    public List<UserInfo> getUserInfo(UserBasicInfo basicInfo)
            throws SQLException {
        List<UserInfo> userLst = new ArrayList<>();
        DataRecordSet rsSet = null;
        SQLException ex = null;
        try {
            String bscSql = "SELECT B.work_month, A.user_name, A.real_name, A.org_id, B.task, B.work_days, B.work_type " +
                    ", A.level, A.work_age, A.enter_date, A.rework " +
                    "FROM hrcp_biz_510100.user_basic_info AS A " +
                    "LEFT JOIN hrcp_biz_510100.user_work_info AS B on " +
                    "A.id = B.id " +
                    "where B.work_month = '" + basicInfo.getWorkMonth() + "' and";
            if (!"".equals(basicInfo.getUserName())) {
                bscSql += " A.user_name LIKE '%%" + basicInfo.getUserName() + "%%' and";
            }
            if (!"".equals(basicInfo.getRealName())) {
                bscSql += " A.real_name LIKE '%%" + basicInfo.getRealName() + "%%' and";
            } else {
                bscSql += " 1=1 and";
            }
            if (basicInfo.getOrgId() != 0) {
                bscSql += " org_id = " + basicInfo.getOrgId();
            } else {
                bscSql += " 1=1 ";
            }
            bscSql += " limit " + basicInfo.getPageNum() * basicInfo.getPageSize() + "," + basicInfo.getPageSize();

            String sql = String.format(bscSql);

            log.info("getUserInfo SQL:" + sql);
            rsSet = getRs(sql);
            if (null != rsSet) {
                ResultSet rs = rsSet.getResultSet();
                if (null != rs) {
                    int count = 0;
                    while (rs.next()) {
                        UserInfo info = new UserInfo();
                        count++;

                        info.setWorkMonth(rs.getString("work_month"));
                        info.setUserName(rs.getString("user_name"));
                        info.setRealName(rs.getString("real_name"));
                        info.setOrgId(rs.getInt("org_id"));
                        info.setTask(rs.getInt("task"));
                        info.setWorkDays(rs.getInt("work_days"));
                        info.setWorkType(rs.getInt("work_type"));
                        info.setLevel(rs.getInt("level"));
                        info.setWorkAge(rs.getInt("work_age"));
                        info.setEnterDate(rs.getString("enter_date"));
                        info.setRework(rs.getInt("rework"));
                        userLst.add(info);
                    }
                    if (count != 0) {
                        log.info("get user info success");
                    } else {
                        log.info("get user info failure:no info");
                    }
                } else {
                    log.info("get user info failure2");
                }
            }

        } catch (SQLException e) {
            ex = e;
            log.info("get user info error ..." + e.getLocalizedMessage());

        } finally {
            if (null != rsSet) {
                rsSet.close();
            }
            if (null != ex) {
                throw ex;
            }
        }
        return userLst;
    }

    //修改人员信息
    public boolean updateUserInfo(List<UserInfo> userLst) throws SQLException {
        boolean result = false;
        for (UserInfo userInfo :
                userLst) {
            SQLException ex = null;
            try {
                String bscSql = "update hrcp_biz_510100.user_basic_info set level = "
                        + userInfo.getLevel() + ", work_age = " + userInfo.getWorkAge()
                        + ", enter_date = '" + userInfo.getEnterDate() + "', rework = "
                        + userInfo.getRework() + " where user_name = '" + userInfo.getUserName() + "'";

                String sql = String.format(bscSql);

                log.info("updateUserBasicInfo SQL:" + sql);
                result = execute(sql);
                if (true != result) {
                    log.info("update user basic info false");
                } else {
                    log.info("update user basic info success");
                }

                bscSql = "update hrcp_biz_510100.user_work_info set task = "
                        + userInfo.getTask() + ", work_days = " + userInfo.getWorkDays()
                        + ", work_type = " + userInfo.getWorkType() + " where id = " +
                        "(select id from hrcp_biz_510100.user_basic_info where user_name = '"
                        + userInfo.getUserName() + "')";

                sql = String.format(bscSql);

                log.info("updateUserWorkInfo SQL:" + sql);
                result = execute(sql);
                if (true != result) {
                    log.info("update user work info false");
                } else {
                    log.info("update user work info success");
                }

            } catch (SQLException e) {
                ex = e;
                log.info("update user info error ..." + e.getLocalizedMessage());

            } finally {
                if (null != ex) {
                    throw ex;
                }
            }
        }
        return result;
    }

    //查询工资
    public List<UserSalaryInfo> getSalaryInfo(UserBasicInfo basicInfo)
            throws SQLException {
        List<UserSalaryInfo> userLst = new ArrayList<>();
        DataRecordSet rsSet = null;
        SQLException ex = null;
        try {
            String bscSql = "SELECT work_month, user_name, real_name, org_id, salary1, salary2, salary3 " +
                    ", salary4, salary5, salary6, total_salary " +
                    "FROM hrcp_biz_510100.user_salary " +
                    "where work_month = '" + basicInfo.getWorkMonth() + "' and";
            if (!"".equals(basicInfo.getUserName())) {
                bscSql += " user_name LIKE '%%" + basicInfo.getUserName() + "%%' and";
            }
            if (!"".equals(basicInfo.getRealName())) {
                bscSql += " real_name LIKE '%%" + basicInfo.getRealName() + "%%' and";
            } else {
                bscSql += " 1=1 and";
            }
            if (basicInfo.getOrgId() != 0) {
                bscSql += " org_id = " + basicInfo.getOrgId();
            } else {
                bscSql += " 1=1 ";
            }
            bscSql += " limit " + basicInfo.getPageNum() * basicInfo.getPageSize() + "," + basicInfo.getPageSize();

            String sql = String.format(bscSql);

            log.info("getUserInfo SQL:" + sql);
            rsSet = getRs(sql);
            if (null != rsSet) {
                ResultSet rs = rsSet.getResultSet();
                if (null != rs) {
                    int count = 0;
                    while (rs.next()) {
                        UserSalaryInfo info = new UserSalaryInfo();
                        count++;

                        info.setWorkMonth(rs.getString("work_month"));
                        info.setUserName(rs.getString("user_name"));
                        info.setRealName(rs.getString("real_name"));
                        info.setOrgId(rs.getInt("org_id"));
                        info.setSalary1(rs.getDouble("salary1"));
                        info.setSalary2(rs.getDouble("salary2"));
                        info.setSalary3(rs.getDouble("salary3"));
                        info.setSalary4(rs.getDouble("salary4"));
                        info.setSalary5(rs.getDouble("salary5"));
                        info.setSalary6(rs.getDouble("salary6"));
                        info.setTotalSalary(rs.getDouble("total_salary"));
                        userLst.add(info);
                    }
                    if (count != 0) {
                        log.info("get salary info success");
                    } else {
                        log.info("get salary info failure:no info");
                    }
                } else {
                    log.info("get salary info failure2");
                }
            }

        } catch (SQLException e) {
            ex = e;
            log.info("get salary info error ..." + e.getLocalizedMessage());

        } finally {
            if (null != rsSet) {
                rsSet.close();
            }
            if (null != ex) {
                throw ex;
            }
        }
        return userLst;
    }

    //修改工资结果
    public boolean updateSalaryInfo(List<UserSalaryInfo> userLst) throws SQLException {
        boolean result = false;
        double totalSalary = 0.0;
        for (UserSalaryInfo userInfo :
                userLst) {
            SQLException ex = null;
            totalSalary = userInfo.getSalary1() + userInfo.getSalary2() + userInfo.getSalary3() + userInfo.getSalary4()
                    + userInfo.getSalary5() + userInfo.getSalary6();
            try {
                String bscSql = "update hrcp_biz_510100.user_salary set salary1 = "
                        + userInfo.getSalary1() + ", salary2 = " + userInfo.getSalary2()
                        + ", salary3 = " + userInfo.getSalary3() + ", salary4 = " + userInfo.getSalary4()
                        + ", salary5 = " + userInfo.getSalary5() + ", salary6 = " + userInfo.getSalary6()
                        + ", total_salary = " + totalSalary
                        + " where user_name = '" + userInfo.getUserName() + "'";

                String sql = String.format(bscSql);

                log.info("updateSalaryInfo SQL:" + sql);
                result = execute(sql);
                if (true != result) {
                    log.info("update salary info false");
                } else {
                    log.info("update salary info success");
                }
            } catch (SQLException e) {
                ex = e;
                log.info("update salary info error ..." + e.getLocalizedMessage());

            } finally {
                if (null != ex) {
                    throw ex;
                }
            }
        }
        return result;
    }

    //将本月目标放至下个月目标
    public boolean insertWorkInfo(String workMonth, String nextMoth) throws SQLException {
        boolean result = false;
        SQLException ex = null;
        try {
            String bscSql = "INSERT INTO hrcp_biz_510100.user_work_info SELECT id, '" + nextMoth + "', task, 0, work_days, 0, 0, work_type"
                    + " FROM hrcp_biz_510100.user_work_info WHERE hrcp_biz_510100.user_work_info.work_month = '" + workMonth + "'";
            String sql = String.format(bscSql);
            log.info("insertWorkInfo SQL:" + sql);
            result = execute(sql);
            if (true != result) {
                log.info("insert work info false");
            } else {
                log.info("insert work info success");
            }
        } catch (SQLException e) {
            ex = e;
            log.info("insert work info error ..." + e.getLocalizedMessage());

        } finally {
            if (null != ex) {
                throw ex;
            }
        }
        return result;
    }

    /**
     * 获取每个月的上班天数和实际收入
     *
     * @param workMonth 2019-05
     * @param from_date 2019-04-26
     * @param to_date   2019-05-25
     * @return
     * @throws SQLException
     */
    public boolean getWorkDaysMoney(String workMonth, String from_date, String to_date) throws SQLException {
        try {
            SqlDataObject in_workMonth = new SqlDataObject(Types.VARCHAR, workMonth);
            SqlDataObject in_from_date = new SqlDataObject(Types.VARCHAR, from_date);
            SqlDataObject in_to_date = new SqlDataObject(Types.VARCHAR, to_date);
            SqlDataObject resp = new SqlDataObject(Types.VARCHAR, "");
            callProc("getWorkDaysMoney", 1,
                    in_workMonth,
                    in_from_date,
                    in_to_date,
                    resp);
            if (resp.getValue().toString().equals("ok")) {
                return true;
            } else {
                throw new SQLException(resp.getValue().toString());
            }

        } catch (SQLException e) {
            log.info("getWorkDaysMoney failure....." + e.getMessage());
            throw e;
        }

    }

    //计算工资
    public boolean calSalary(String workMonth) throws SQLException {
        try {
            SqlDataObject cal_month = new SqlDataObject(Types.VARCHAR, workMonth);
            SqlDataObject resp = new SqlDataObject(Types.VARCHAR, "");
            callProc("calSalary", 1,
                    cal_month,
                    resp);
            if (resp.getValue().toString().equals("ok")) {
                return true;
            } else {
                throw new SQLException(resp.getValue().toString());
            }

        } catch (SQLException e) {
            log.info("calSalary failure....." + e.getMessage());
            throw e;
        }
    }

    /**
     * 每日导出数据入口
     *
     * @param reportDate
     * @return
     * @throws SQLException
     */
    @Override
    public boolean dailyExport(String reportDate) throws SQLException {
        log.info("开始导入每日报表：" + reportDate);
        List<PosUserReport> list = null;
        boolean success = false;
        try {
            list = this.getPosUserReport(reportDate);
            if (null != list && list.size() > 0)
                success = this.savePosUserReport(list);
            else
                log.info("没有要导入得数据");

        } catch (SQLException e) {
            log.info("dailyExport failure....." + e.getMessage());
            throw e;
        }
        return success;
    }

    /**
     * 从生产数据库获取每日报表，当时间为26号时，同时获取当月月报
     *
     * @param reportDate 2019-06-01
     * @return
     * @throws SQLException
     */
    private List<PosUserReport> getPosUserReport(String reportDate) throws SQLException {
        List<PosUserReport> list = new ArrayList<>();
        DataRecordSet rsSet = null;
        SQLException ex = null;
        try {

            String querystr = String.format(" report_date ='%s'", reportDate);
            String date = reportDate.substring(8, 10);
            if (date.equals("26"))
                querystr += String.format(" or report_date = '%s'", reportDate.substring(0, 7));

            String sql = String.format("SELECT id,report_type,report_date,partner_user_id,payment_amount,supply_amount,park_count,leav_count " +
                    "FROM hrcp_biz_510100.pos_user_report " +
                    "where %s;", querystr);

            log.info("getPosUserReport SQL:" + sql);
            rsSet = getRs(sql);
            int count = 0;
            if (null != rsSet) {
                ResultSet rs = rsSet.getResultSet();
                if (null != rs) {
                    while (rs.next()) {
                        PosUserReport item = new PosUserReport();
                        count++;
                        item.setId(rs.getInt("id"));
                        item.setReport_type(rs.getInt("report_type"));
                        item.setReport_date(rs.getString("report_date"));

                        item.setPartner_user_id(rs.getInt("partner_user_id"));
                        item.setPayment_amount(rs.getInt("payment_amount"));
                        item.setSupply_amount(rs.getInt("supply_amount"));

                        item.setPark_count(rs.getInt("park_count"));
                        item.setLeav_count(rs.getInt("leav_count"));
                        list.add(item);
                    }
                }
            }
            log.info("getPosUserReport success;size:" + count);
        } catch (SQLException e) {
            ex = e;
            log.info("getPosUserReport failure ..." + e.getLocalizedMessage());

        } finally {
            if (null != rsSet) {
                rsSet.close();
            }
            if (null != ex) {
                throw ex;
            }
        }
        return list;
    }

    /**
     * 保存每日报表到本地数据库
     *
     * @param list
     * @return boolean
     * @throws SQLException
     */

    private boolean savePosUserReport(List<PosUserReport> list) throws SQLException {
        DataRecordSet rsSet = null;
        SQLException ex = null;
        try {

            SQLArray sqls = new SQLArray();
            String sql = null;
            Iterator<PosUserReport> iterator = list.iterator();
            while (iterator.hasNext()) {
                PosUserReport i = iterator.next();
                sql = String.format("INSERT INTO hrcp_biz_510100.pos_user_report(" +
                        "id,report_type,report_date,partner_user_id,payment_amount,supply_amount,park_count,leav_count) " +
                        "VALUES(%d,%d,%s,%d,%d,%d,%d,%d)", i.getId(), i.getReport_type(), i.getReport_date(), i.getPartner_user_id(), i.getPayment_amount(), i.getSupply_amount(), i.getPark_count(), i.getLeav_count());
                sqls.addSQL(sql);
            }
            log.info("sqls长度:" + sqls.getSqlArray().size()+ "最后一条:" + sql);
            return execute_batch(sqls,2);

        } catch (SQLException e) {
            ex = e;
            log.info("savePosUserReport failure ..." + e.getLocalizedMessage());
        } finally {
            if (null != rsSet) {
                rsSet.close();
            }
            if (null != ex) {
                throw ex;
            }
        }
        return true;
    }
}