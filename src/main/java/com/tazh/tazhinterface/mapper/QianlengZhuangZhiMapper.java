package com.tazh.tazhinterface.mapper;

import com.tazh.tazhinterface.pojo.*;

import java.util.List;

public interface QianlengZhuangZhiMapper {
   //返回每天的天然气数据
   public TianRanQi getTianRanQiDateByDay(String startTime,String endTime);
   public NingXiYou getNingXiYouDateByDay(String startTime,String endTime);
   public WenDingQingTing getWenDingQingTingByDay(String startTime,String endTime);
   public YeHuaQi getYeHuaQiByDay(String startTime,String endTime);
   public YiWan getYiWanByDay(String startTime,String endTime);
   public List<TianRanQi> getTianRanQiList();
   public int insertDataToTianRanQi();
   public List<TianRanQi> getTianRanQiListTest(String arg0);
}
