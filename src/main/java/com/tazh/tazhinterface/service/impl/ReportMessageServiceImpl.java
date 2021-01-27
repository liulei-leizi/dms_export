package com.tazh.tazhinterface.service.impl;

import com.tazh.tazhinterface.mapper.ReportMessageMapper;
import com.tazh.tazhinterface.pojo.PcRpdReportMessageT;
import com.tazh.tazhinterface.service.ReportMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Service
public class ReportMessageServiceImpl implements ReportMessageService {
    @Resource
    ReportMessageMapper reportMessageMapper;
    @Override
    public PcRpdReportMessageT serachReportMessages(String id, String bbmc, String textDate) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HashMap paramMap = new HashMap<>();
        //先将text转换为Date
        String rq = textDate+" 00:00:00";
        paramMap.put("id",id);
        paramMap.put("bbmc",bbmc);
        paramMap.put("rq",rq);
        PcRpdReportMessageT pcRpdReportMessageT = reportMessageMapper.getReportMessageTByParam(paramMap);
        if(pcRpdReportMessageT == null){
            return  null;
        }
        return pcRpdReportMessageT;
    }
}
