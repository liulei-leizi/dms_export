package com.tazh.tazhinterface.controller;

import com.alibaba.fastjson.JSONObject;
import com.tazh.tazhinterface.service.QianLengZhuangZhiService;
import com.tazh.tazhinterface.utils.OrclJDBC;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping(value = "/dataImport")
public class QiLengZhuangZhiImport {
    private static Connection conn = null;
    private static Statement pStatement = null;

    private static Logger logger = Logger.getLogger(QianLengZhuangZhiExport.class);

    @RequestMapping(value = "/tianRanQiData", method = RequestMethod.POST)
    public String tianRanQiData(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) throws IOException {

        //先获取表名
        String tableName = request.getParameter("tableName");
        //获取列的个数
        String scolumsNumber = request.getParameter("columsNumber");
        //将列的个数转换成int
        int  columsNumber = Integer.parseInt(scolumsNumber);
        request.getParameter("tableName");
        response.addHeader("Access-Control-Allow-Origin", "*");
        XSSFWorkbook hssfWorkbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet xssfSheet = hssfWorkbook.getSheetAt(0);
        //得到sheet1的最后一行 用于循环遍历
        int rows = xssfSheet.getLastRowNum() + 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (file.isEmpty()) {
            return "文件为空";
        }
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String username = "SCOTT";
        String password = "ABCDqw8863766";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            pStatement =  conn.createStatement();
            for (int i = 1; i < rows; i++) {
                XSSFRow xssfRow = xssfSheet.getRow(i);
                String sqlheader = "";
                if (xssfRow != null) {
                    for(int j = 0;j<columsNumber;j++){
                        XSSFCell xssfCell = xssfRow.getCell(j);
                        //对xssfCell进行解析
                        if(j == 0){
                            //第一个字段为时间，需要转换成时间类型的String
                            Date date = xssfCell.getDateCellValue();
                            //将次data格式化成字符串
                            String reportdate = sdf.format(date);
                            sqlheader = "insert into"+tableName+" values(to_date('"+reportdate+"','YYYY-MM-DD HH24:MI:SS')'";
                        }else{
                            String value = xssfCell.getStringCellValue();
                            if(j == columsNumber-1){
                                sqlheader += (","+value+")");
                            }else{
                                sqlheader += (","+value);
                            }

                        }

                    }


                }
                pStatement.addBatch(sqlheader);
            }
            pStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                pStatement.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // "0"代表导入成功
        return "0";
    }

    @RequestMapping(value = "/ningXiYouDataTest", method = RequestMethod.GET)
    public String ningXiYouDataTest() throws IOException {

//        String driver = "oracle.jdbc.driver.OracleDriver";
//        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
//        String username = "SCOTT";
//        String password = "ABCDqw8863766";
//        try{
//            Class.forName(driver);
//            conn = DriverManager.getConnection(url, username, password);
//            pStatement =  conn.createStatement();
//            String sql = "insert into ningxiyou values(to_date('2020-08-27 15:00:00','YYYY-MM-DD HH24:MI:SS'),54.6,65.67,";
//            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
//            System.out.println("========"+currentDate);
//            sql += "to_date('"+currentDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS'))";

            Calendar ca = Calendar.getInstance();
            ca.setTime(new java.sql.Date(System.currentTimeMillis()));
            ca.add(Calendar.DAY_OF_MONTH, -1);
            Date dbelongtoDate = (Date) ca.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
           System.out.println("==========="+dbelongtoDate);
//            pStatement.execute(sql);
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally {
//
//        }
//
       return "0";
   }

}
