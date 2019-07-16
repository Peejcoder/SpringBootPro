package com.ccic.payroll.authentication;

import com.ccic.payroll.dbutil.DataRecordSet;
import com.ccic.payroll.dbutil.MySQLDBHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;


@Service
public class AlertAuthenticationProviderDao extends MySQLDBHelper implements IAlertAuthenticationProviderDao {
    private static Logger log = LoggerFactory.getLogger(AlertAuthenticationProviderDao.class);

    @Override
    public void authenticationUser(AuthenticationUserRequest request, AuthenticationUserResponse response)
            throws SQLException {

        DataRecordSet rsSet = null;
        SQLException ex = null;
        try {
            String sql = String.format(
                    "SELECT * FROM hrcp_biz_510100.user where user_status = 1 and user_name = '%s' and  password = '%s'",
                    request.getLoginId(), request.getPassword());

            log.info(sql);
            rsSet = getRs(sql);
            if (null != rsSet) {
                ResultSet rs = rsSet.getResultSet();
                if (null != rs) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        response.setUserId(rs.getString("id"));
                        response.setUserType(rs.getInt("user_type"));
                        response.setRealName(rs.getString("real_name"));
                    }
                    if (count != 0) {
                        response.setLoginId(request.getLoginId());
                        response.setSuccess(true);
                    } else {
                        log.info("登陆失败1");
                        response.setSuccess(false);
                        response.setDescription("用户名或密码错误");
                    }
                } else {
                    log.info("登陆失败2");
                    response.setSuccess(false);
                    response.setDescription("用户名或密码错误");
                }
            }

        } catch (SQLException e) {
            ex = e;
            log.info("authenticationUser failure ..." + e.getLocalizedMessage());

        } finally {
            if (null != rsSet) {
                rsSet.close();
            }
            if (null != ex) {
                throw ex;
            }
        }
    }

}
