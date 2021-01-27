package com.tazh.tazhinterface.pojo;

import lombok.Data;

import java.sql.Timestamp;

//稀油注输联合站分线计量日报数据
@Data
public class RPD_U_THIN_LINE_MERSURE_T {
    //ID
    private String rpd_u_thin_line_measure_id;
    //采集时间
    private Timestamp acquisition_time;
    //XY分线计量:乌尔禾总线含水平均值
    private float at_ave1;
    //XY分线计量:夏子街含水平均值
    private float at_ave2;
    //XY分线计量:乌尔禾总线流量平均值
    private float ft_ave1;
    //XY分线计量:夏子街流量平均值
    private float ft_ave2;
    //XY分线计量:乌尔禾总线小时流量累计
    private float ft_hh_101;
    //XY分线计量:夏子街小时流量累计
    private float ft_hh_102;
    //XY分线计量:乌尔禾总线小时平均油量m3
    private float ol_hh_101;
    //XY分线计量:夏子街小时平均油量m3(夏子街油量)
    private float ol_hh_102;
    //乌尔禾水密度
    private float smd_xyfx;
    //乌尔禾油密度
    private float ymd_xyfx;
    //夏子街油密度
    private float ymd_xyfx2;
    //夏子街水密度
    private float smd_xyfx2;
    //XY分线计量:乌尔禾1#含水平均值
    private float at_ave3;
    //XY分线计量:乌尔禾2#含水平均值
    private float at_ave4;
    //XY分线计量:乌尔禾1#流量平均值
    private float ft_ave3;
    //XY分线计量:乌尔禾2#流量平均值
    private float ft_ave4;
    //XY分线计量:乌尔禾1#小时流量累计
    private float ft_hh_103;
    //XY分线计量:乌尔禾2#小时流量累计
    private float ft_hh_104;
    //XY分线计量:乌尔禾1#小时平均油量m3
    private float ol_hh_1021;
    //XY分线计量:乌尔禾2#小时平均油量m3
    private float ol_hh_1022;
    //新添加字段 夏子街前端来液含水平均值
    private float AT_AVE5;
    //新添加字段 夏子街前端来液流量平均值
    private float FT_AVE5;
    //新添加字段 夏子街前端小时流量累计
    private float FT_HH_105;
    //新添加字段 夏子街小时平均油量
    private float OL_HH_105;
}
