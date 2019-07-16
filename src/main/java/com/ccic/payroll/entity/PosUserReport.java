package com.ccic.payroll.entity;

public class PosUserReport {
    private int id;
    private int report_type;
    private String report_date;
    private int partner_user_id;
    private int payment_amount;
    private int supply_amount;
    private int park_count;
    private int leav_count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReport_type() {
        return report_type;
    }

    public void setReport_type(int report_type) {
        this.report_type = report_type;
    }

    public String getReport_date() {
        return report_date;
    }

    public void setReport_date(String report_date) {
        this.report_date = report_date;
    }

    public int getPartner_user_id() {
        return partner_user_id;
    }

    public void setPartner_user_id(int partner_user_id) {
        this.partner_user_id = partner_user_id;
    }

    public int getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(int payment_amount) {
        this.payment_amount = payment_amount;
    }

    public int getSupply_amount() {
        return supply_amount;
    }

    public void setSupply_amount(int supply_amount) {
        this.supply_amount = supply_amount;
    }

    public int getPark_count() {
        return park_count;
    }

    public void setPark_count(int park_count) {
        this.park_count = park_count;
    }

    public int getLeav_count() {
        return leav_count;
    }

    public void setLeav_count(int leav_count) {
        this.leav_count = leav_count;
    }
}
