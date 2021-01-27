package com.tazh.tazhinterface.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFConditionalFormattingRule;
import org.apache.poi.hssf.usermodel.HSSFPatternFormatting;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSheetConditionalFormatting;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author Administrator
 * 
 */
public class ExcelUtil {
	/**
	 * 获取模板
	 * @param filePath
	 * @return
	 */
	public static HSSFWorkbook getExcelTemplet(String filePath) {
		HSSFWorkbook wb = null;
		File file = null;
		InputStream input = null;
		try {
			//获取模板
			file = new File(filePath);
			input = new FileInputStream(file);
			POIFSFileSystem poiFileSystem = new POIFSFileSystem(input);  
			//得到文档对象  
			wb = new HSSFWorkbook(poiFileSystem);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}
	public static HSSFSheet getExcelTemplet(Workbook wb,int sheetNum) {
		//得到表单 
		return (HSSFSheet) wb.getSheetAt(sheetNum);
	}

	/**
	 * 创建excel数据行并写入数据
	 * 
	 */
	public static HSSFWorkbook insertExcelData(HSSFWorkbook wb,Sheet sheet,short startRowNum, Collection<Object[]> dataSet) {
		Row dataRow = null;
		Cell dataCell = null;
		Object[] objectData = null;
		CellStyle dataStyle = getStyle(wb);
		dataStyle = getDataStyle(dataStyle);
		for (Iterator<Object[]> it = dataSet.iterator(); it.hasNext();) {
			objectData = it.next();
			dataRow = sheet.createRow((short) startRowNum);
			for (int i = 0; i < objectData.length; i++) {
				dataCell = dataRow.createCell(i);
				dataCell = setCell(dataCell,objectData[i],dataStyle);
			}
			startRowNum ++;
		}
		
		return wb;
	}
		

	/**
	 *
	 * 
	 * @param filename
	 * @return
	 * @throws IOException 
	 */
	static ByteArrayOutputStream getFileByteArray(Workbook wb,ByteArrayOutputStream output) throws IOException {
		wb.write(output);
		output.flush();
		output.close();
		return output;
	}
	/*
	 * 写入数据和样式
	 */
	public static Cell setCell(Cell c ,Object objectValue,CellStyle cellStyle){
		if (objectValue instanceof Integer) {
			c.setCellValue((Integer) objectValue);
		} else if (objectValue instanceof Short) {
			c.setCellValue((Short) objectValue);
		} else if (objectValue instanceof Float) {
			c.setCellValue((Float) objectValue);
		} else if (objectValue instanceof Double) {
			c.setCellValue((Double) objectValue);
		} else if (objectValue instanceof Long) {
			c.setCellValue((Long) objectValue);
		} else if (objectValue instanceof BigDecimal) {
			c.setCellValue(((BigDecimal) objectValue).doubleValue());
		} else if (objectValue instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			c.setCellValue(sdf.format((Date) objectValue));
			c.setCellStyle(cellStyle); // 设置样式
//			return c;
		} else {
			// 其它数据类型都当作字符串简单处理
			c.setCellValue(String.valueOf((objectValue == null) ? "" : objectValue));
			c.setCellStyle(cellStyle); // 设置样式
		}
		if (!(objectValue instanceof Date) && (objectValue instanceof String)) {
			c.setCellStyle(getNumStyle(cellStyle)); // 设置样式
//			return c;
		}else{
			c.setCellStyle(getStringStyle(cellStyle)); // 设置样式
		}
		return c;
	}
	
	/*
	 * 写入数据
	 */
	public static Cell setCell(Cell c ,Object objectValue){
		if (objectValue instanceof Integer) {
			c.setCellValue((Integer) objectValue);
		} else if (objectValue instanceof Short) {
			c.setCellValue((Short) objectValue);
		} else if (objectValue instanceof Float) {
			c.setCellValue((Float) objectValue);
		} else if (objectValue instanceof Double) {
			c.setCellValue((Double) objectValue);
		} else if (objectValue instanceof Long) {
			c.setCellValue((Long) objectValue);
		} else if (objectValue instanceof BigDecimal) {
			c.setCellValue(((BigDecimal) objectValue).doubleValue());
		} else if (objectValue instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			c.setCellValue(sdf.format((Date) objectValue));
//			return c;
		} else {
			// 其它数据类型都当作字符串简单处理
			c.setCellValue(String.valueOf((objectValue == null) ? "" : objectValue));
		}
		System.out.println(objectValue.getClass().toString());
		return c;
	}
	/*
	 * 写入数据和样式
	 */
	public static Cell setCellAndStyle(Cell c ,Object objectValue,CellStyle cellStyle){
		if (objectValue instanceof Integer) {
			c.setCellValue((Integer) objectValue);
		} else if (objectValue instanceof Short) {
			c.setCellValue((Short) objectValue);
		} else if (objectValue instanceof Float) {
			c.setCellValue((Float) objectValue);
		} else if (objectValue instanceof Double) {
			c.setCellValue((Double) objectValue);
		} else if (objectValue instanceof Long) {
			c.setCellValue((Long) objectValue);
		} else if (objectValue instanceof BigDecimal) {
			c.setCellValue(((BigDecimal) objectValue).doubleValue());
		} else if (objectValue instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			c.setCellValue(sdf.format((Date) objectValue));
			c.setCellStyle(cellStyle); // 设置样式
//			return c;
		} else {
			// 其它数据类型都当作字符串简单处理
			c.setCellValue(String.valueOf((objectValue == null) ? "" : objectValue));
			c.setCellStyle(cellStyle); // 设置样式
		}
		if (!(objectValue instanceof Date) && (objectValue instanceof String)) {
			c.setCellStyle(getNumStyle(cellStyle)); // 设置样式
//			return c;
		}else{
			c.setCellStyle(getStringStyle(cellStyle)); // 设置样式
		}
		return c;
	}
	
	/*
	 * 写入标题、表头
	 */
	public static HSSFSheet writeTitle(HSSFWorkbook wb,HSSFSheet sheet,String title,int rowNum, int cellNum) {
		HSSFRow row = sheet.getRow(rowNum);
		HSSFCell cell = row.getCell(cellNum);
		cell.setCellValue(title);
		return sheet;
	}
	/*
	 * 写入其他数据（合计、说明等）
	 */
	
	public static HSSFSheet writeOtherInfo(HSSFWorkbook wb,HSSFSheet sheet,String info,int rowNum, int cellNum) {
		HSSFRow row = sheet.createRow(rowNum);
		HSSFCell cell = row.createCell(cellNum);
		if (info != null) {
			cell.setCellValue(info);
		}
		CellStyle cellStyle = getStyle(wb);
		cellStyle = getDataStyle(cellStyle);
		cell.setCellStyle(cellStyle); // 设置样式
		return sheet;
	}
	/*
	 * 列数据元信息单格数字样式
	 */
	public static CellStyle getNumStyle(CellStyle cellStyle){
		// styleForNum.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		// //整数部分三位逗号隔开，小数显示实际位数（最多4位，最少2位）
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 右对齐
		return cellStyle;
	}
	
	
	/*
	 * 列数据信息单元格字符样式
	 */
	public static CellStyle getStringStyle(CellStyle style) {
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return style;
	}
	
	/*
	 * 列数据信息单元格综合样式
	 */
	public static CellStyle getDataStyle(CellStyle style) {
		// 设置底边框;
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 设置自动换行;
		style.setWrapText(true);
		// 设置水平对齐的样式为居中对齐;
		//style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
//		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return style;
	}
	
	/*
	 * 获取样式对象
	 */
	public static CellStyle getStyle(Workbook wb){
		// 设置字体
		Font font = wb.createFont();
		// 设置字体大小
		font.setFontHeightInPoints((short) 9);
//		font.setFontHeightInPoints((short) 12);
		// 字体加粗
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Arial");
//		font.setFontName("宋体");
		// 在样式用应用设置的字体;
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		return style;
	}
	
	/*
	 * 获取样式对象 不是获取的Cell 新建的Cell对象时的字体
	 */
	public static void setStyle(Workbook wb, CellStyle style){
		// 设置字体
		Font font = wb.createFont();
		// 设置字体大小
		font.setFontHeightInPoints((short) 12);
		// 设置字体名字
		font.setFontName("宋体");
		// 在样式用应用设置的字体;
		style.setFont(font);
	}
	
	/*
	 * 设置背景色
	 */
	public static HSSFCellStyle getbackGroundColorStyle(HSSFCellStyle style) {
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return style;
	}
	public static Workbook insertExcel(Workbook book, Sheet sheet,
			int startRowNum, Collection<Object[]> dataSet) {
		Row dataRow = null;
		Cell dataCell = null;
		Object[] objectData = null;
		CellStyle dataStyle = getStyle(book);
		dataStyle = getDataStyle(dataStyle);
		for (Iterator<Object[]> it = dataSet.iterator(); it.hasNext();) {
			objectData = it.next();
			dataRow = sheet.createRow(startRowNum);
			for (int i = 0; i < objectData.length; i++) {
				dataCell = dataRow.createCell(i);
				dataCell = setCell(dataCell,objectData[i],dataStyle);
			}
			startRowNum ++;
		}
		return book;
	}
	
	/*
	 * scf  
	 * formula  公式
	 * color  字体颜色
	 * compreValue 比较值范围
	 * scope  数据范围
	 */
	public static HSSFSheetConditionalFormatting setFormula(HSSFSheetConditionalFormatting scf, String formula,
			short color,String scope,String compreValue) {
		
//		HSSFConditionalFormattingRule cf_rule= scf.createConditionalFormattingRule(formula);  
		
		//设置"条件格式"的规则，本例选择的条件类型是："单元格数据"  
		//如果当前单元格的数据等于R，则显示红色  
		HSSFConditionalFormattingRule cf_R_rule = scf.createConditionalFormattingRule(ComparisonOperator.GE, "\""+compreValue+"\"", null);  
		HSSFPatternFormatting cf_R = cf_R_rule.createPatternFormatting();  
//		cf_R.setFillBackgroundColor(HSSFColor.RED.index);  

//		HSSFPatternFormatting cf = cf_rule.createPatternFormatting();  
		cf_R.setFillBackgroundColor(color);
		HSSFConditionalFormattingRule[] cfRules = {cf_R_rule};  
		//条件格式应用的单元格范围
		CellRangeAddress[] regions = {CellRangeAddress.valueOf(scope)};  
		scf.addConditionalFormatting(regions, cfRules);  
		return scf;
	}
	
	/**
	 * 获取不重复上传文件名称
	 * @param filename
	 * @return
	 */
	public static String getNerverExcelName(String filename){
		String fileName = "";
		String date = new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date());
		fileName = date+function()+"_"+filename;
		
		return fileName;
	}

	public static String function() {
		int n = 0;
		n = (int) (Math.random() * 100000);
		while (n < 10000 || !handle(n)) {
			n = (int) (Math.random() * 100000);
		}
		return String.valueOf(n);
	}
	
	public static boolean handle(int n) {
		int[] list = new int[5];
		for (int i = 0; i < 5; i++) {
			list[i] = n % 10;
			n = n / 10;
		}
		for (int i = 0; i < 5; i++) {
			for (int j = i + 1; j < 5; j++) {
				if (list[i] == list[j])
					return false;
			}
		}
		return true;
	}
	
}
