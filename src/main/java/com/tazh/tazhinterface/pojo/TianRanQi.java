package com.tazh.tazhinterface.pojo;


import java.sql.Timestamp;

import java.util.Objects;

public class TianRanQi {
    private Timestamp reportdate;
    private float pressure;
    private float temperature;
    private int ranqinumber;
    private float jingkouchanliang;
    private float jihuaduibi;
    private float zuoriduibi;
    private float chuqiliang;
    private float rongjieyang;
    public TianRanQi() {
    }

    public TianRanQi(Timestamp reportdate, float pressure, float temperature, int ranqinumber, float jingkouchanliang, float jihuaduibi, float zuoriduibi, float chuqiliang, float rongjieyang) {
        this.reportdate = reportdate;
        this.pressure = pressure;
        this.temperature = temperature;
        this.ranqinumber = ranqinumber;
        this.jingkouchanliang = jingkouchanliang;
        this.jihuaduibi = jihuaduibi;
        this.zuoriduibi = zuoriduibi;
        this.chuqiliang = chuqiliang;
        this.rongjieyang = rongjieyang;
    }

    public Timestamp getReportdate() {
        return reportdate;
    }

    public void setReportdate(Timestamp reportdate) {
        this.reportdate = reportdate;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getRanqinumber() {
        return ranqinumber;
    }

    public void setRanqinumber(int ranqinumber) {
        this.ranqinumber = ranqinumber;
    }

    public float getJingkouchanliang() {
        return jingkouchanliang;
    }

    public void setJingkouchanliang(float jingkouchanliang) {
        this.jingkouchanliang = jingkouchanliang;
    }

    public float getJihuaduibi() {
        return jihuaduibi;
    }

    public void setJihuaduibi(float jihuaduibi) {
        this.jihuaduibi = jihuaduibi;
    }

    public float getZuoriduibi() {
        return zuoriduibi;
    }

    public void setZuoriduibi(float zuoriduibi) {
        this.zuoriduibi = zuoriduibi;
    }

    public float getChuqiliang() {
        return chuqiliang;
    }

    public void setChuqiliang(float chuqiliang) {
        this.chuqiliang = chuqiliang;
    }

    public float getRongjieyang() {
        return rongjieyang;
    }

    public void setRongjieyang(float rongjieyang) {
        this.rongjieyang = rongjieyang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TianRanQi tianRanQi = (TianRanQi) o;
        return Float.compare(tianRanQi.pressure, pressure) == 0 &&
                Float.compare(tianRanQi.temperature, temperature) == 0 &&
                ranqinumber == tianRanQi.ranqinumber &&
                Float.compare(tianRanQi.jingkouchanliang, jingkouchanliang) == 0 &&
                Float.compare(tianRanQi.jihuaduibi, jihuaduibi) == 0 &&
                Float.compare(tianRanQi.zuoriduibi, zuoriduibi) == 0 &&
                Float.compare(tianRanQi.chuqiliang, chuqiliang) == 0 &&
                Float.compare(tianRanQi.rongjieyang, rongjieyang) == 0 &&
                Objects.equals(reportdate, tianRanQi.reportdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportdate, pressure, temperature, ranqinumber, jingkouchanliang, jihuaduibi, zuoriduibi, chuqiliang, rongjieyang);
    }

    @Override
    public String toString() {
        return "TianRanQi{" +
                "reportdate=" + reportdate +
                ", pressure=" + pressure +
                ", temperature=" + temperature +
                ", ranqinumber=" + ranqinumber +
                ", jingkouchanliang=" + jingkouchanliang +
                ", jihuaduibi=" + jihuaduibi +
                ", zuoriduibi=" + zuoriduibi +
                ", chuqiliang=" + chuqiliang +
                ", rongjieyang=" + rongjieyang +
                '}';
    }
}
