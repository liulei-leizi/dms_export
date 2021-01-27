package com.tazh.tazhinterface.service;

import com.tazh.tazhinterface.pojo.TianRanQi;

import java.util.List;
import java.util.Map;

public interface QianLengZhuangZhiService {
    public Map<String,Object> getQiLengZhuangZhiByDate(String startTime, String endTime);
    public List<TianRanQi> getTianRanQiList();
    public List<TianRanQi> getTianRanQiListTest(String argo);
}
