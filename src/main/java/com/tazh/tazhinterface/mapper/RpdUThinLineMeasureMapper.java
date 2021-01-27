package com.tazh.tazhinterface.mapper;

import com.tazh.tazhinterface.pojo.RPD_U_THIN_LINE_MERSURE_T;

import java.util.List;

//稀油注输联合站分线计量日报数据Mapper
public interface RpdUThinLineMeasureMapper {
    //查询未添加字段后（乌尔禾1线，2线）的数据，并计算
    List<RPD_U_THIN_LINE_MERSURE_T> getMeasureDataByTimeAddCol(String startTime, String endTime);
    //查询未添加字段前（乌尔禾1线，2线）的数据，并计算
    List<RPD_U_THIN_LINE_MERSURE_T> getMeasureDataByTime(String startTime, String endTime);
    //查询出所有的，不需要计算
    List<RPD_U_THIN_LINE_MERSURE_T> getMeasureDataAllByTime(String startTime, String endTime);

}
