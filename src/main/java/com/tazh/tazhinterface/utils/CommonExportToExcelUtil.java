package com.tazh.tazhinterface.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.synth.Region;
import java.io.*;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by admin on 2018/9/4.
 */
public class CommonExportToExcelUtil<T> {
    //每次设置导出数量
    public static int  NUM=5000;
    public static String title="excel";
    /**
     * 导出Excel的方法
     * @param headers 表头
     * @param result 结果集
     * @param out 输出流
     * @param pattern 时间格式
     * @throws Exception
     */
    public  void exportExcel(String[] headers, String[] columns, List<T> result, OutputStream out, HttpServletRequest request, String pattern) throws Exception{
        //创建一个文件后缀为.zip
        File zip = new File("E://files/" +getFileName() + ".zip");// 压缩文件

        int n=0;
        if (!CollectionUtils.isEmpty(result)) {
            if (result.size() % NUM == 0) {
                n = result.size() / NUM;
            } else {
                n = result.size() / NUM + 1;
            }
        }else{
            n=1;
        }
        List<String> fileNames = new ArrayList();// 用于存放生成的文件名称
        //文件流用于转存文件
        for (int j = 0; j < n; j++) {
            Collection<T> result1=null;
            //切取每5000为一个导出单位，存储一个文件
            //对不足5000做处理；
            if (!CollectionUtils.isEmpty(result)) {
                //如果是最后一个导出单位的话
                if (j == n - 1) {
                    if (result.size() % NUM == 0) {
                        //把最后一个导出单位截取出来放到result1中
                        result1 = result.subList(5000 * j, 5000 * (j + 1));
                    } else {
                        result1 = result.subList(5000 * j,
                                5000 * j + result.size() % NUM);
                    }
                } else {
                    result1 = result.subList(5000 * j, 5000 * (j + 1));
                }
            }
            // 声明一个工作薄
            Workbook workbook = new HSSFWorkbook();
            String trueTitle = title + j;
            // 生成一个表格
            HSSFSheet sheet = (HSSFSheet) workbook.createSheet(trueTitle);
            // 设置表格默认列宽度为18个字节
            sheet.setDefaultColumnWidth((short)18);

            //这个是文件名字
            //String file = request.getRealPath("/files") + "/" + getFileName() + "-" +j+ ".xls";
            String file =  "E://files/" + getFileName() + "-" +j+ ".xls";
            fileNames.add(file);
            File trueFile = new File(file);
            FileOutputStream o = new FileOutputStream(trueFile);

            // 生成一个样式
            HSSFCellStyle style = (HSSFCellStyle) workbook.createCellStyle();
            // 设置这些样式
            style.setFillForegroundColor(HSSFColor.GOLD.index);
            // 生成一个字体
            HSSFFont font = (HSSFFont) workbook.createFont();
            font.setColor(HSSFColor.BLACK.index);
            //font.setFontHeightInPoints((short) 12);
            // 把字体应用到当前的样式
            style.setFont(font);

            // 指定当单元格内容显示不下时自动换行
            style.setWrapText(true);
            // 声明一个画图的顶级管理器
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

            // 产生表格标题行
            //表头的样式
           /* HSSFCellStyle titleStyle = (HSSFCellStyle) workbook.createCellStyle();// 创建样式对象
            // 设置字体
            HSSFFont titleFont = (HSSFFont) workbook.createFont(); // 创建字体对象
            titleFont.setFontHeightInPoints((short) 15); // 设置字体大小
            //  titleFont.setFontName("黑体"); // 设置为黑体字
            titleStyle.setFont(titleFont);
            HSSFRow rowHeader = sheet.createRow(0);
            HSSFCell cellHeader = rowHeader.createCell((short)0);   //只能往第一格子写数据，然后应用样式，就可以水平垂直居中
            HSSFRichTextString textHeader = new HSSFRichTextString(title);
            cellHeader.setCellStyle(titleStyle);
            cellHeader.setCellValue(textHeader);*/

            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell((short)i);
                cell.setCellStyle(style);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
            // 遍历集合数据，产生数据行
            if(result1 != null){
                int index = 1;
                for(T t:result1){
                    row = sheet.createRow(index);
                    index++;
                    for(short i = 0; i < columns.length; i++) {
                        HSSFCell cell = row.createCell(i);
                        String fieldName = columns[i];
                        String getMethodName = "get"
                                + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Class tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                        Object value = getMethod.invoke(t, new Class[]{});
                        String textValue = null;
                        if(value == null) {
                            textValue = "";
                        }else if (value instanceof Date) {
                            Date date = (Date) value;
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            textValue = sdf.format(date);
                        }  else if (value instanceof byte[]) {
                            // 有图片时，设置行高为60px;
                            row.setHeightInPoints(60);
                            // 设置图片所在列宽度为80px,注意这里单位的一个换算 1px为35.7
                            sheet.setColumnWidth(i, (short) (35.7 * 80));
                            byte[] bsValue = (byte[]) value;
                            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
                                    1023, 255, (short) 6, index, (short) 6, index);
                            patriarch.createPicture(anchor, workbook.addPicture(
                                    bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                        } else{
                            //其它数据类型都当作字符串简单处理
                            textValue = value.toString();
                        }

                        if(textValue!= null){
                            Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                            Matcher matcher = p.matcher(textValue);
                            if(matcher.matches()){
                                //是数字当作double处理
                                cell.setCellValue(Double.parseDouble(textValue));
                            }else{
                                HSSFRichTextString richString = new HSSFRichTextString(textValue);
                                cell.setCellValue(richString);
                            }
                        }
                    }
                }
            }
            workbook.write(o);
            File srcfile[] = new File[fileNames.size()];
            for (int i = 0, n1 = fileNames.size(); i < n1; i++) {
                srcfile[i] = new File(fileNames.get(i));
            }
            ZipFiles(srcfile, zip);
            FileInputStream inStream = new FileInputStream(zip);
            byte[] buf = new byte[4096];
            int readLength;
            while (((readLength = inStream.read(buf)) != -1)) {
                out.write(buf, 0, readLength);
            }
            inStream.close();
        }
    }
    //获取文件名字
    public static String getFileName(){
        // 文件名获取
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String f = title + format.format(date);
        return f;
    }
    //压缩文件
    public static void ZipFiles(java.io.File[] srcfile, java.io.File zipfile) {
        byte[] buf = new byte[1024];
        try {
            //创建一个写压缩的对象 并且把名字传输进去
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 设置响应头 */
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            this.title=fileName;
            response.reset();// 清空输出流
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    +new String(this.title.getBytes("GB2312"), "8859_1")
                    + ".zip");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
