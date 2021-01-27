package com.tazh.tazhinterface.pojo;

import oracle.sql.TIMESTAMP;

import java.util.Objects;

public class YiWan {
    private TIMESTAMP reportdate;
    private float richan;
    private float kucun;

    public YiWan() {
    }

    public YiWan(TIMESTAMP reportdate, float richan, float kucun) {
        this.reportdate = reportdate;
        this.richan = richan;
        this.kucun = kucun;
    }

    public TIMESTAMP getReportdate() {
        return reportdate;
    }

    public void setReportdate(TIMESTAMP reportdate) {
        this.reportdate = reportdate;
    }

    public float getRichan() {
        return richan;
    }

    public void setRichan(float richan) {
        this.richan = richan;
    }

    public float getKucun() {
        return kucun;
    }

    public void setKucun(float kucun) {
        this.kucun = kucun;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YiWan yiWan = (YiWan) o;
        return Float.compare(yiWan.richan, richan) == 0 &&
                Float.compare(yiWan.kucun, kucun) == 0 &&
                Objects.equals(reportdate, yiWan.reportdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportdate, richan, kucun);
    }

    @Override
    public String toString() {
        return "YiWan{" +
                "reportdate=" + reportdate +
                ", richan=" + richan +
                ", kucun=" + kucun +
                '}';
    }
}
