package com.tazh.tazhinterface.pojo;

import oracle.sql.TIMESTAMP;

import java.util.Objects;

public class WenDingQingTing {
    private TIMESTAMP reportdate;
    private float richan;
    private float kucun;

    public WenDingQingTing() {
    }

    public WenDingQingTing(TIMESTAMP reportdate, float richan, float kucun) {
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
        WenDingQingTing that = (WenDingQingTing) o;
        return Float.compare(that.richan, richan) == 0 &&
                Float.compare(that.kucun, kucun) == 0 &&
                Objects.equals(reportdate, that.reportdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportdate, richan, kucun);
    }

    @Override
    public String toString() {
        return "WenDingQingTing{" +
                "reportdate=" + reportdate +
                ", richan=" + richan +
                ", kucun=" + kucun +
                '}';
    }
}
