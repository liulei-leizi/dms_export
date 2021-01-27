package com.tazh.tazhinterface.pojo;

import oracle.sql.TIMESTAMP;

import java.util.Objects;

public class NingXiYou {
    private TIMESTAMP reportdate;
    private float richan;
    private float kucun;

    public NingXiYou() {
    }

    public NingXiYou(TIMESTAMP reportdate, float richan, float kucun) {
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
        NingXiYou ningXIYou = (NingXiYou) o;
        return Float.compare(ningXIYou.richan, richan) == 0 &&
                Float.compare(ningXIYou.kucun, kucun) == 0 &&
                Objects.equals(reportdate, ningXIYou.reportdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportdate, richan, kucun);
    }

    @Override
    public String toString() {
        return "NingXIYou{" +
                "reportdate=" + reportdate +
                ", richan=" + richan +
                ", kucun=" + kucun +
                '}';
    }
}
