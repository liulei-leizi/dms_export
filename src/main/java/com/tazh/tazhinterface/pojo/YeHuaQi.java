package com.tazh.tazhinterface.pojo;

import oracle.sql.TIMESTAMP;

import java.util.Objects;

public class YeHuaQi {
    private TIMESTAMP reportdate;
    private float richan;
    private float kucun;

    public YeHuaQi() {
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
        YeHuaQi yeHuaQi = (YeHuaQi) o;
        return Float.compare(yeHuaQi.richan, richan) == 0 &&
                Float.compare(yeHuaQi.kucun, kucun) == 0 &&
                Objects.equals(reportdate, yeHuaQi.reportdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportdate, richan, kucun);
    }

    @Override
    public String toString() {
        return "YeHuaQi{" +
                "reportdate=" + reportdate +
                ", richan=" + richan +
                ", kucun=" + kucun +
                '}';
    }
}
