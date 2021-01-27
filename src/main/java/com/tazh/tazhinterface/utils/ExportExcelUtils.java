package com.tazh.tazhinterface.utils;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExportExcelUtils {
	public void exportExcel_QiLengCanShu(HSSFWorkbook workbook, int sheetNum,
            String sheetTitle, Map<String,Object> qLzhObj) throws Exception {

        //定义所需的列
        String[] columnKey = {"time","waishujihuanchanliang","kaijingshu","pressure","temprature","ranqinumber","jingkouchanliang","jihuaduibi"
                ,"zuoriduibi","nrichan","nkucun","wrichan","wkucun","yrichan","ykucun","yirichan","yikucun"};
        // 生成一个表格  
        HSSFSheet sheet = workbook.createSheet("sheet1");
        try {
            //给生成的Sheet起名
        	workbook.setSheetName(sheetNum, sheetTitle);  
			
		} catch (Exception e) {
			// TODO: handle exception
		}
        //先生成正文字体
        Font contextFont = workbook.createFont();
        contextFont.setFontName("宋体");
        contextFont.setFontHeightInPoints((short) 12);
       /* contextFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);*/
        contextFont.setColor(HSSFColor.BLACK.index);
        //单元格样式  上下左右居中 有边框
        CellStyle commonStyle = workbook.createCellStyle();
        commonStyle.setFont(contextFont);
       /* commonStyle.setAlignment(CellStyle.ALIGN_CENTER);// 左右居中
        commonStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 上下居中*/
        commonStyle.setLocked(true);
        commonStyle.setWrapText(false);// 自动换行
       /* commonStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        commonStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        commonStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        commonStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框*/
        //单元格样式 上下左右居中 无边框
        CellStyle commonStyleNoBorder = workbook.createCellStyle();
        commonStyleNoBorder.setFont(contextFont);
//        commonStyleNoBorder.setAlignment(CellStyle.ALIGN_CENTER);// 左右居中
//        commonStyleNoBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 上下居中
        commonStyleNoBorder.setLocked(true);
        commonStyleNoBorder.setWrapText(false);// 自动换行

        //将获取数据的值拿过来 目前只是测试  用假数据代替
        // 行号
        int rowNum = 0;
        //设置列宽 17列 每一列1000像素的宽度
        for (int i = 0; i < 17; i++) {
            sheet.setColumnWidth(i, 2000);
        }
        //先遍历第一行
        Row r0 = sheet.createRow(rowNum++);
        r0.setHeight((short) 300);
        String[] row_first = {"时间", "外输计划产量", "开井数", "天然气", "", "", "","","","凝析油","","稳定轻烃","","液化气","","乙烷",""};
        for (int i =0;i<row_first.length;i++) {
            //逐渐给第一行的每一列创建单元格
            Cell tempCell = r0.createCell(i);
            //先默认都是有边框的
            tempCell.setCellStyle(commonStyle);
            tempCell.setCellValue(row_first[i]);
        }
        //进行单元格的合并
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 8));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 9, 10));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 12));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 14));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 15, 16));
        //遍历第二行
        Row r1 = sheet.createRow(rowNum++);
        r1.setHeight((short) 300);
        String[] row_second = {"", "", "", "外输压力", "外输温度", "外数量", "井口产量","与计划对比","与昨日对比","日产","库存","日产","库存","日产","库存","日产","库存"};
        for (int i =0;i<row_first.length;i++) {
             //逐渐给第一行的每一列创建单元格
             Cell tempCell = r1.createCell(i);
             //先默认都是有边框的
             tempCell.setCellStyle(commonStyle);
             tempCell.setCellValue(row_second[i]);
        }
        Row r2 = sheet.createRow(rowNum++);
        r2.setHeight((short) 300);
        for(int i =0;i<17;i++){
            Cell tempCell = r2.createCell(i);
            tempCell.setCellValue((columnKey[i] == null ? " " : qLzhObj.get(columnKey[i])).toString());

        }
        /*HSSFRow row = sheet.createRow(0);
        row.setHeightInPoints(20);
        for (int i = 0; i < headers.length; i++) {  
            HSSFCell cell = row.createCell((short) i); 
            
            cell.setCellStyle(style1);  
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);  
            cell.setCellValue(text.toString());  
        }  */
        /*// 遍历集合数据，产生数据行
        try {
    	  if (list.size() > 0) {
    		  for (short i = 1; i < list.size(); i++) {
    				// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
    				// 创建一行，在页sheet上
    				Row row1 = sheet.createRow((short) i);
    				// 在row行上创建一个方格
    				for (short j = 0; j < keys.length; j++) {
    					Cell cell = row1.createCell(j);
        		        String suoxin = list.get(i).get("suoxin") == null ? "" : list.get(i).get("suoxin") + "";
        		        if(!"".equals(suoxin)){
        		        		try {
        		            if(j == 8){
        	    		        row1.setHeightInPoints(110);
            		        	String value = list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString();
        		            	 // 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
            		            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

            		            BufferedImage bufferImg = ImageIO.read(new File(value));
            		            ImageIO.write(bufferImg, "png", byteArrayOut);

            		            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

            		            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0,
            		                    (short) 8, i, (short) 9, i+1);
            		            patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut
            		                    .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));

        		            }else{
            		        	cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
        		            }
        					cell.setCellStyle(style2);
        		            } catch (IOException io) {
        		                io.printStackTrace();
        		                System.out.println("io erorr : " + io.getMessage());
        		            }
        		        }else{
    					cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
    					cell.setCellStyle(style2);
        		        }
    				}
    			}
          }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/
    }  
    

}
