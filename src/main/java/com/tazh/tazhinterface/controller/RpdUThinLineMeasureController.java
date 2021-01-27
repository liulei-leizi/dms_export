package com.tazh.tazhinterface.controller;

import com.tazh.tazhinterface.mapper.ReportMessageMapper;
import com.tazh.tazhinterface.pojo.PcRpdReportMessageT;
import com.tazh.tazhinterface.pojo.RPD_U_THIN_LINE_MERSURE_T;
import com.tazh.tazhinterface.service.ReportMessageService;
import com.tazh.tazhinterface.service.RpdUThinLineMeasureService;
import com.tazh.tazhinterface.utils.PropertiesConfig;
import com.tazh.tazhinterface.utils.U2DataExportUtil;
import freemarker.template.utility.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
@Slf4j
@RestController
@RequestMapping(value = "/thinLineMeasuredataExport")
public class RpdUThinLineMeasureController {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private InputStream excelFile = null;
    private static Logger logger = Logger.getLogger(RpdUThinLineMeasureController.class);
    @Resource
    RpdUThinLineMeasureService rpdUThinLineMeasureService;

    @Resource
    ReportMessageService reportMessageService;

    @RequestMapping(value = "/rpdUThinLineMeasureExport", method = RequestMethod.GET)
    public ResponseEntity<byte[]> complexExcelExportTianRanQiPost(@RequestParam ("queryData") String queryData) throws Exception {
        logger.info("进入了接口");
        System.out.println("进去了接口");
        //txtDate为获取的时间
        String txtDate = queryData;
        //Date paramTime = simpleDateFormat.parse("2020-09-01");
        HttpHeaders headers = null;
        ByteArrayOutputStream baos = null;
        List rutlmts_String = new ArrayList<>();
        Collection<Object[]> rutlmts = rpdUThinLineMeasureService.getMeasureDataByTime(simpleDateFormat.parse(txtDate));

        PropertiesConfig pc = new PropertiesConfig();
        //获取reportMessage
        PcRpdReportMessageT rpdReportMessageT = reportMessageService.serachReportMessages("", "稀油注输联合站分线计量日报表", txtDate);

        //获取主体数据位置
        String RpdUTLMMainData = pc.getSystemConfiguration("RpdUTLMMainData");
        //获取值班人、审核人、日期、备注数据插入位置
        String wATERWatch = pc.getSystemConfiguration("lineMeasureWXY");
        String wATERAuditor = pc.getSystemConfiguration("lineMeasureAXY");
        String wATERReportTime = pc.getSystemConfiguration("lineReportTimeXY");
        //读取excel
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/稀油注输联合站分线计量日报表.xls");
        assert inputStream != null;
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = wb.getSheetAt(0);
        //查询lineMeasureList 即为数据
        //List<Object[]> lineMeasureList = new ArrayList<>();

        //向excel中插入日期
        U2DataExportUtil.insertExcelData(wb, sheet, wATERReportTime, 0, new Object[]{txtDate});

        //插入数据位置  B5,B15,B24
        String[] lineMeasurePositions = pc.getSystemConfiguration("RpdUTLMMainData").split(",");
        // U2DataExportUtil.TemporalGrouping(workbook, sheet, lineMeasureList, dates, lineMeasurePositions, paramTime);
        if (rpdReportMessageT != null) {
            if (!"".equals(rpdReportMessageT.getZbr1()) && null != (rpdReportMessageT.getZbr1())) {
                U2DataExportUtil.insertExcelData(wb, sheet, wATERWatch, 0, new Object[]{rpdReportMessageT.getZbr1()});
            }
            if (!"".equals(rpdReportMessageT.getShr()) && null != (rpdReportMessageT.getShr())) {
                U2DataExportUtil.insertExcelData(wb, sheet, wATERAuditor, 0, new Object[]{rpdReportMessageT.getShr()});
            }
            if (!"".equals(rpdReportMessageT.getBz()) && null != (rpdReportMessageT.getBz())) {
                U2DataExportUtil.insertExcelData(wb, sheet, wATERReportTime, 0, new Object[]{rpdReportMessageT.getBz()});
            }
        }
        if (rutlmts == null || rutlmts.size() == 0) {
             baos = U2DataExportUtil.ExporDataStream(wb);
            if (baos != null) {
                byte[] ba = baos.toByteArray();
                excelFile = new ByteArrayInputStream(ba);
                baos.flush();
                baos.close();
            }
            return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
        }
        //分段时间
        String[] timeStr = pc.getSystemConfiguration("time0").split("&");
        String[][] dates = new String[3][];
        for (int i = 0; i < dates.length; i++) {
            dates[i] = timeStr[i].split(",");
        }
        //格式化时间
        U2DataExportUtil.setDates(txtDate, dates);
        //开始插入数据
        U2DataExportUtil.TemporalGrouping(wb, sheet, rutlmts, dates, lineMeasurePositions, txtDate);
        wb.setForceFormulaRecalculation(true);
        //java.io.ByteArrayOutputStream baos = U2DataExportUtil.ExporDataStream(wb);
       /* if(baos != null){
            byte[] ba = baos.toByteArray();
            excelFile = new ByteArrayInputStream(ba);
            baos.flush();
            baos.close();
        }*/
       /* String fileName = "分线计量";
        wb.write(baos);
        byte[] content = baos.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        httpResponse.reset();
        httpResponse.setContentType("application/vnd.ms-excel;charset=utf-8");
        // 单个导出设置文件名称
        httpResponse.setHeader("Content-Disposition",
                "attachment;filename=" + new String(fileName.getBytes("GBK"), "ISO8859-1") + ".xls");

        ServletOutputStream out = httpResponse.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (IOException ie) {
            logger.error(ie.getMessage(), ie);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (bis != null) {
                bis.close();
            }

            if (bos != null) {
                bos.close();
            }
        }
        return "excel";*/
        headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", new String((txtDate+" 稀油注输联合站分线计量报表.xls").getBytes("UTF-8"), "iso-8859-1"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        baos = new ByteArrayOutputStream();
        wb.write(baos);
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }
}
