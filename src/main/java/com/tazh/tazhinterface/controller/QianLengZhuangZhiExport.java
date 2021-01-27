package com.tazh.tazhinterface.controller;

import com.alibaba.fastjson.JSONObject;
import com.tazh.tazhinterface.pojo.TianRanQi;
import com.tazh.tazhinterface.service.QianLengZhuangZhiService;
import com.tazh.tazhinterface.utils.CommonExportToExcelUtil;
import com.tazh.tazhinterface.utils.ExportExcelUtils;
import com.tazh.tazhinterface.utils.PropertiesConfig;
import com.tazh.tazhinterface.utils.U2DataExportUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/dataExport")
public class QianLengZhuangZhiExport {
    @Resource
    private  QianLengZhuangZhiService qianLengZhuangZhiService;
    private static Logger logger = Logger.getLogger(QianLengZhuangZhiExport.class);
    @RequestMapping(value = "/qianLengCanShu")
    public String qianLengCanShu(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
        ServletContext application = null;
        RequestContext requestContext = new RequestContext(httpRequest);
        try {
            application = httpRequest.getSession().getServletContext();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        //这个list是从数据库中查找出来的数据
        Map<String,Object> qLzhObj = null;
        String fileName = "";
        int indexNum = 0;
        //格式化时间 获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = simpleDateFormat.format(new Date());
        //将字符串截取出来并且进行拼凑
        String time = currentTime.substring(0,10);
        String startTime = time + " 00:00:00";
        String endTime =time + " 23:59:59";
        qLzhObj = qianLengZhuangZhiService.getQiLengZhuangZhiByDate(startTime,endTime);
        try{
            //获取文件名字
            //fileName = requestContext.getMessage("QiLengZhuangZhiExport.qianLengCanShu");
            fileName ="QiLengZhuangZhiExport.qianLengCanShu";// "点位信息"
            ExportExcelUtils eeut =  new ExportExcelUtils();
            HSSFWorkbook workbook = new HSSFWorkbook();
            eeut.exportExcel_QiLengCanShu(workbook,indexNum,fileName,qLzhObj);

            indexNum++;
            //创建一个写入流
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);
            byte[] content = os.toByteArray();
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
        }catch (Exception e1){
            e1.printStackTrace();
        }

        return null;
    }

    @RequestMapping(value = "/templateExportTianRanQi", method = RequestMethod.GET)
    public ResponseEntity<byte[]> templateTianRanQi(){
        //String  sql = "TianRanQi";
        //先查出今日天然气的所有数据
        List<TianRanQi> tianRanQiList = qianLengZhuangZhiService.getTianRanQiList();
        HttpHeaders headers = null;
        ByteArrayOutputStream baos = null;
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/天然气基础信息.xls");
            //1.创建Excel文档
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            //获取Excel表单
            XSSFSheet sheet = workbook.getSheetAt(1);
            //创建日期显示格式
            XSSFCellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
            //定义列的宽度
            sheet.setColumnWidth(0, 12 * 256);
            sheet.setColumnWidth(1, 12 * 256);
            sheet.setColumnWidth(2, 10 * 256);
            sheet.setColumnWidth(3, 12 * 256);
            sheet.setColumnWidth(4, 12 * 256);
            sheet.setColumnWidth(5, 12 * 256);
            //装数据
            for (int i = 0; i < tianRanQiList.size(); i++) {
                XSSFRow row = sheet.createRow(i + 2);
                TianRanQi tianRanQi = tianRanQiList.get(i);
                row.createCell(0).setCellValue(tianRanQi.getPressure());
                row.createCell(1).setCellValue(tianRanQi.getTemperature());
                row.createCell(2).setCellValue(tianRanQi.getRanqinumber());
                row.createCell(3).setCellValue(tianRanQi.getJingkouchanliang());
                row.createCell(4).setCellValue(tianRanQi.getJihuaduibi());
                row.createCell(5).setCellValue(tianRanQi.getZuoriduibi());

            }
            headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", new String("油井基础信息.xls".getBytes("UTF-8"), "iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            baos = new ByteArrayOutputStream();
            workbook.write(baos);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }
    @RequestMapping(value = "/templateCommonExportTianRanQi", method = RequestMethod.GET)
    public void templateCommonTianRanQi(HttpServletRequest request) throws Exception {
        List<TianRanQi> tianRanQiList = qianLengZhuangZhiService.getTianRanQiList();
        String[] headers = {"外输压力","外输温度","外数量","井口产量","与计划对比","与昨日对比"};
        String[] columns ={"pressure","temperature","ranqinumber","jingkouchanliang","jihuaduibi","zuoriduibi"};
        String partten = "yyyy-MM-dd";
        File file = new File("E:\\export.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        CommonExportToExcelUtil commonExportToExcelUtil = new CommonExportToExcelUtil<>();
        commonExportToExcelUtil.exportExcel(headers,columns,tianRanQiList,outputStream,request,partten);

    }
    @RequestMapping(value = "/complexExcelExportTianRanQi", method = RequestMethod.GET)
    public ResponseEntity<byte[]> complexExcelExportTianRanQi(HttpServletRequest request) throws  Exception{
        HttpHeaders headers = null;
        ByteArrayOutputStream baos = null;
        //先查询除所有天然气的数据
        List<TianRanQi> tianRanQiList = qianLengZhuangZhiService.getTianRanQiList();
        PropertiesConfig pc = new PropertiesConfig();
        //获取零散数据的excel位置
        String[] ranQiFenSanData = pc.getSystemConfiguration("RanQiFenSanData").split(",");
        //获取主体数据位置
        String ranQiMain = pc.getSystemConfiguration("RanQiMain");
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/天然气日报.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        //创建一个 List<Object[]>类型的对象  用来放置主体数据
        List<Object[]> List = new ArrayList<>();
        for (int i = 0;i < tianRanQiList.size();i++){
            TianRanQi tianRanQi = tianRanQiList.get(i);
            Object[] objects = {
                    tianRanQi.getReportdate(),
                    tianRanQi.getPressure(),
                    tianRanQi.getTemperature(),
                    tianRanQi.getRanqinumber(),
                    tianRanQi.getJingkouchanliang(),
                    tianRanQi.getJihuaduibi(),
                    tianRanQi.getZuoriduibi(),
                    tianRanQi.getChuqiliang(),
                    tianRanQi.getRongjieyang()
            };
            List.add(i,objects);
        }
        //调用插入数据的方法
        if (List != null && List.size() > 0) {
            //调用插入数据样式的方法
            U2DataExportUtil.insertDataByPosition(List, workbook, sheet, ranQiMain);
        }
        //创建一个 List<Object[]>类型的对象  用来放置零散数据
        //参数是需要前端传过来的  或者后台传过来
        List<Object[]> lingSanList= new ArrayList<>();
        //RanQiFenSanData = G3,H3,A10,E10,B15,F15
        //模拟数据
        Object[] object1 = {"成总","2020年8月28日","刘磊,数据正常","苑超波,数据正常","刘磊","苑超波"};
        lingSanList.add(0,object1);
        if (lingSanList != null && lingSanList.size() > 0) {
            // 获取审核人,年月,记事，填表人等数据插入位置
            for (int j = 0; j < ranQiFenSanData.length; j++) {
                if (lingSanList.get(0)[j] != null) {
                    U2DataExportUtil.insertExcelData(workbook, sheet, ranQiFenSanData[j], 0, new Object[]{lingSanList.get(0)[j]});
                }
            }
        }

        headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", new String("天然气日报信息.xls".getBytes("UTF-8"), "iso-8859-1"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        baos = new ByteArrayOutputStream();
        workbook.write(baos);

        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/complexExcelExportTianRanQiPost", method = RequestMethod.POST)
    public ResponseEntity<byte[]> complexExcelExportTianRanQiPost(@RequestParam ("paramData") String paramData) throws  Exception{
        HttpHeaders headers = null;
        ByteArrayOutputStream baos = null;
        String[] sParamData = paramData.split(",");
        //先查询除所有天然气的数据
        List<TianRanQi> tianRanQiList = qianLengZhuangZhiService.getTianRanQiList();
        PropertiesConfig pc = new PropertiesConfig();
        //获取零散数据的excel位置
        String[] ranQiFenSanData = pc.getSystemConfiguration("RanQiFenSanData").split(",");
        //获取主体数据位置
        String ranQiMain = pc.getSystemConfiguration("RanQiMain");
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/天然气日报.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        //创建一个 List<Object[]>类型的对象  用来放置主体数据
        List<Object[]> List = new ArrayList<>();
        for (int i = 0;i < tianRanQiList.size();i++){
            TianRanQi tianRanQi = tianRanQiList.get(i);
            Object[] objects = {
                    tianRanQi.getReportdate(),
                    tianRanQi.getPressure(),
                    tianRanQi.getTemperature(),
                    tianRanQi.getRanqinumber(),
                    tianRanQi.getJingkouchanliang(),
                    tianRanQi.getJihuaduibi(),
                    tianRanQi.getZuoriduibi(),
                    tianRanQi.getChuqiliang(),
                    tianRanQi.getRongjieyang()
            };
            List.add(i,objects);
        }
        //调用插入数据的方法
        if (List != null && List.size() > 0) {
            //调用插入数据样式的方法
            U2DataExportUtil.insertDataByPosition(List, workbook, sheet, ranQiMain);
        }
        //创建一个 List<Object[]>类型的对象  用来放置零散数据
        //参数是需要前端传过来的  或者后台传过来
        List<Object[]> lingSanList= new ArrayList<>();
        //RanQiFenSanData = G3,H3,A10,E10,B15,F15
        //模拟数据
        //Object[] object1 = {"成总","2020年8月28日","刘磊,数据正常","苑超波,数据正常","刘磊","苑超波"};
        Object[] object1 = sParamData;
        lingSanList.add(0,object1);
        if (lingSanList != null && lingSanList.size() > 0) {
            // 获取审核人,年月,记事，填表人等数据插入位置
            for (int j = 0; j < ranQiFenSanData.length; j++) {
                if (lingSanList.get(0)[j] != null) {
                    U2DataExportUtil.insertExcelData(workbook, sheet, ranQiFenSanData[j], 0, new Object[]{lingSanList.get(0)[j]});
                }
            }
        }

        headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", new String("天然气日报信息.xls".getBytes("UTF-8"), "iso-8859-1"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        baos = new ByteArrayOutputStream();
        workbook.write(baos);

        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }



}
