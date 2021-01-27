package com.tazh.tazhinterface.service;

import com.tazh.tazhinterface.pojo.PcRpdReportMessageT;

import java.text.ParseException;

public interface ReportMessageService {
    PcRpdReportMessageT serachReportMessages(String id, String bbmc,String textDate) throws ParseException;
}
