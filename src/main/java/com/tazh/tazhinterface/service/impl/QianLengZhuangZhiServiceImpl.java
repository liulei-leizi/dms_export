package com.tazh.tazhinterface.service.impl;

import com.tazh.tazhinterface.mapper.QianlengZhuangZhiMapper;
import com.tazh.tazhinterface.pojo.*;
import com.tazh.tazhinterface.service.QianLengZhuangZhiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class QianLengZhuangZhiServiceImpl implements QianLengZhuangZhiService {
    @Resource
    private QianlengZhuangZhiMapper qianlengZhuangZhiMapper;

    @Override
    public Map<String,Object> getQiLengZhuangZhiByDate(String startTime, String endTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = simpleDateFormat.format(new Date());
        //将字符串截取出来并且进行拼凑
        String time = currentTime.substring(0,10);
        Map<String,Object> qLzhObj = new HashMap<>();
        try{
            TianRanQi tianRanQi =qianlengZhuangZhiMapper.getTianRanQiDateByDay(startTime,endTime);
            NingXiYou ningXiYou = qianlengZhuangZhiMapper.getNingXiYouDateByDay(startTime, endTime);
            WenDingQingTing wenDingQingTing = qianlengZhuangZhiMapper.getWenDingQingTingByDay(startTime, endTime);
            YeHuaQi yeHuaQi = qianlengZhuangZhiMapper.getYeHuaQiByDay(startTime, endTime);
            YiWan yiWan = qianlengZhuangZhiMapper.getYiWanByDay(startTime, endTime);
            qLzhObj.put("time",time);
            qLzhObj.put("waishujihuanchanliang","52.23t");
            qLzhObj.put("kaijingshu",22);

            qLzhObj.put("pressure",tianRanQi.getPressure());
            qLzhObj.put("temprature",tianRanQi.getTemperature());
            qLzhObj.put("ranqinumber",tianRanQi.getRanqinumber());
            qLzhObj.put("jingkouchanliang",tianRanQi.getJingkouchanliang());
            qLzhObj.put("jihuaduibi",tianRanQi.getJihuaduibi());
            qLzhObj.put("zuoriduibi",tianRanQi.getZuoriduibi());

            qLzhObj.put("nrichan",ningXiYou.getRichan());
            qLzhObj.put("nkucun",ningXiYou.getKucun());

            qLzhObj.put("wrichan",wenDingQingTing.getRichan());
            qLzhObj.put("wkucun",wenDingQingTing.getKucun());

            qLzhObj.put("yrichan",yeHuaQi.getRichan());
            qLzhObj.put("ykucun",yeHuaQi.getKucun());

            qLzhObj.put("yirichan",yiWan.getRichan());
            qLzhObj.put("yikucun",yiWan.getKucun());

        }catch (Exception e){
            e.printStackTrace();
        }

        return qLzhObj;
    }

    @Override
    public List<TianRanQi> getTianRanQiList() {
        return qianlengZhuangZhiMapper.getTianRanQiList();
    }

    @Override
    public List<TianRanQi> getTianRanQiListTest(String argo) {
        return qianlengZhuangZhiMapper.getTianRanQiListTest(argo);
    }


}
