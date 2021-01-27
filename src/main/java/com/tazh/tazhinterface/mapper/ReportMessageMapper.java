package com.tazh.tazhinterface.mapper;

import com.tazh.tazhinterface.pojo.PcRpdReportMessageT;

import java.util.HashMap;

public interface ReportMessageMapper {
    //根据id,bbmc,rq查询这个对象
    PcRpdReportMessageT getReportMessageTByParam(HashMap paramMap);
}
