package com.ccic.payroll.entity;

import com.ccic.payroll.common.PageLimitRequest;

public class UserBasicInfo extends PageLimitRequest {
    private String userName;
    private String realName;
    private int orgId;
    private String workMonth;

    public String getWorkMonth() {
        return workMonth;
    }

    public void setWorkMonth(String workMonth) {
        this.workMonth = workMonth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }
}
