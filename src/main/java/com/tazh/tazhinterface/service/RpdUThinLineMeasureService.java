package com.tazh.tazhinterface.service;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

//稀油注输联合站分线计量日报数据Mapper
public interface RpdUThinLineMeasureService {
    //查询数据 根据时间参数 查询出前天8点到今天早上8点的数据
    Collection<Object[]> getMeasureDataByTime(Date searchData) throws ParseException;
}
