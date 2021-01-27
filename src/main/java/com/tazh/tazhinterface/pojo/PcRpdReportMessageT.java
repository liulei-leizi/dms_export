package com.tazh.tazhinterface.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class PcRpdReportMessageT {
    //联合站非综合日报ID
    private String rpd_report_message_id;
    //日期
    private Date rq;
    //报表名称
    private String bbmc;
    //白班
    private String zbr1;
    //晚班
    private String zbr2;
    //零点班
    private String zbr3;
    //填报
    private String tbr;
    //审核
    private String shr;
    //审核时间
    private String shsj;
    //备注
    private String bz;


}
