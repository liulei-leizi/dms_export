package com.tazh.tazhinterface.utils;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by admin on 2018/9/4.
 */
public class ExportUtil {
    Logger logger = Logger.getLogger(ExportUtil.class);
    /** CSV文件列分隔符 */
    private static final String CSV_COLUMN_SEPARATOR = ",";

    /** CSV文件列分隔符 */
    private static final String CSV_RN = "\r\n";

    /**
     *
     * @param dataList 集合数据
     * @param colNames 表头部数据
     */
    public static boolean doExport(ArrayList<String[]> dataList, String colNames, HttpServletResponse response, String wellName, String IP,ZipOutputStream zipOut) {
        try {
            StringBuffer buf = new StringBuffer();
            String[] colNamesArr = null;
            colNamesArr = colNames.split(",");

            // 完成数据csv文件的封装
            // 输出列头
            for(int i=0;i<colNamesArr.length;i++)
            {
                if(i<colNamesArr.length-1)
                {
                    buf.append(colNamesArr[i]).append(CSV_COLUMN_SEPARATOR);
                }
                else
                    buf.append(colNamesArr[i]);
            }
            buf.append(CSV_RN);
            if (null != dataList) { // 输出数据
                for (int row = 0; row < dataList.size(); row++){
                    for (int i=0;i<colNamesArr.length;i++)
                    {
                        if(i==1||i==2)
                        {
                            buf.append(wellName+"_JK").append(CSV_COLUMN_SEPARATOR);
                            continue;
                        }
                        if(i<colNamesArr.length-1)
                        {
                            buf.append(dataList.get(row)[i]).append(CSV_COLUMN_SEPARATOR);
                        }
                        else
                            buf.append(dataList.get(row)[i]);
                    }
                    buf.append(CSV_RN);
                }
            }
            getZip(response,buf.toString().getBytes("UTF-8"),IP,wellName, zipOut);
            // 写出响应
//            os.write(buf.toString().getBytes("UTF-8"));
//            os.flush();
            return true;
        } catch (Exception e) {
            System.out.println(("doExport错误..."+e));
        }
        return false;
    }
    public static String getZip(HttpServletResponse response,byte[] bytes,String IP,String wellName,ZipOutputStream zipOut){
        try {
//            long a = System.currentTimeMillis();

//                StringBuilder builder = new StringBuilder("");
//                byte[] bytes = builder.toString().getBytes();
                InputStream excelIS = new ByteArrayInputStream(bytes);
                ZipEntry zipEntry = new ZipEntry("modbus_tcp$"+wellName+"_JK"+"$"+IP+"$502$1"+".csv");
                zipOut.putNextEntry(zipEntry);
                byte b[] = new byte[1024];
                int length = 0;
                while ((length = excelIS.read(b)) != -1) {
                    zipOut.write(b, 0, length);
                }
                zipOut.closeEntry();
                excelIS.close();

            return "Ok";
        } catch (IOException e) {
            e.printStackTrace();
//            log.error("getZip exception:{}",e.getMessage());
            return "Ok";
        }
    }
    /**
     * setHeader
     */
    public static void responseSetProperties(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        // 设置文件后缀
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fn = fileName + sdf.format(new Date()) + ".csv";
        // 读取字符编码
        String utf = "UTF-8";

        // 设置响应
        response.setContentType("application/ms-txt.numberformat:@");
        response.setCharacterEncoding(utf);
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, utf));
    }
    public static boolean getType(Object t){
        if(t instanceof Date){
            return true;
        }else {
            return false;
        }
    }

}
