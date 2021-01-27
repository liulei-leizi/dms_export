package com.tazh.tazhinterface.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//import org.apache.poi.hssf.converter.ExcelToHtmlUtils;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/*
 * 2号处理站报表导出
 */
public class U2DataExportUtil {
	private static ByteArrayOutputStream abos = null;
	
	/**
	 * 返回Excel 输出流
	 * @param wb
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static ByteArrayOutputStream ExporDataStream(HSSFWorkbook wb) throws UnsupportedEncodingException {
			abos = new ByteArrayOutputStream();
			try {
				return ExcelUtil.getFileByteArray(wb,abos);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return new ByteArrayOutputStream();
	}
	
	/**
	 * 按时间分组插入数据 如：采出水处理系统日报表、反应器日报表、水处理加药系统日报表、原油处理加药系统日报表
	 * @param targetFilePath
	 * @param dataSet
	 * @param dates
	 * @param cellPositions
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void TemporalGrouping(HSSFWorkbook wb, HSSFSheet sheet, Collection<Object[]> dataSet, String[][] dates, String[] cellPositions, String txtDate) throws IOException{

		//dataSet为list数据  //dates 为时间的字符串数据   cellPositions为数据的起始位置 txtDate为日期
		Object[] dataGrouping = splitDataByTime(dataSet, dates, txtDate);
		for (int i = 0; i < cellPositions.length; i++) {
			if (dataGrouping != null && dataGrouping.length != 0) {
				insertExcelData(wb, sheet, cellPositions[i], (Collection<Object[]>)dataGrouping[i]);
			}
		}
	}
	
	@SuppressWarnings("unchecked") //传入一个日期，取包含该日期向前6天的数据
	public static void TemporalGroupingWeek(HSSFWorkbook wb, HSSFSheet sheet, Collection<Object[]> dataSet, String[][] dates, String[] cellPositions, String txtDate) throws IOException{	
		Object[] dataGrouping = splitDataByWeek(dataSet, dates, txtDate);
		for (int i = 0; i < cellPositions.length; i++) {
			if (dataGrouping != null && dataGrouping.length != 0) {
				insertExcelData(wb, sheet, cellPositions[i], (Collection<Object[]>)dataGrouping[i]);
			}
		}
	}
	
	@SuppressWarnings("unchecked") //传入一个年份月份，取该年该月的数据
	public static void TemporalGroupingMonth(HSSFWorkbook wb, HSSFSheet sheet, Collection<Object[]> dataSet, String[][] dates, String[] cellPositions, String txtYear,String txtMonth) throws IOException{
		Object[] dataGrouping = splitDataByMonth(dataSet, dates, txtYear, txtMonth);
		for (int i = 0; i < cellPositions.length; i++) {
			if (dataGrouping != null && dataGrouping.length != 0) {
				insertExcelData(wb, sheet, cellPositions[i], (Collection<Object[]>)dataGrouping[i]);
			}
		}
	}
	/**
	 * 
	 * @param dataSet 数据
	 * @param targetFilePath 模板文件
	 * @param positions 数据切割起始位置
	 * @param dates 数据切割时间段
	 * @param cellPositions 插入数据分段位置
	 * @param txtDate 数据分割所在时间段日期
	 * @return
	 * @throws IOException
	 */
	/*@SuppressWarnings("unchecked")
	public static HSSFWorkbook BranchingGrouping(Collection<Object[]> dataSet, String targetFilePath, String[][] positions, String[][] dates, String[][] cellPositions, String txtDate) throws IOException{
		HSSFWorkbook wb = ExcelToHtmlUtils.loadXls( new File( targetFilePath ) );
		HSSFSheet sheet = wb.getSheetAt(0);
		//按指定位置的数据段分割数据

		Object[] dataGrouping = splitDataByPosition(dataSet, positions);
		String[] timeSlot = new String[dates.length];
		int t = 0;
		for (Iterator<Object[]> iterator = dataSet.iterator(); iterator.hasNext();) {
			Object[] obj = (Object[]) iterator.next();
			timeSlot[t] = obj[0].toString();//按数据顺序截取 每行数据时间段与dates的时间段分组
			t++;
		}

		for (int i = 0; i < dataGrouping.length; i++) {
			Object[] timeDataGroupings = splitDataByTime((Collection<Object[]>) dataGrouping[i], dates , txtDate);
//			for (int j = 0; j < cellPositions.length; j++) {
//				wb = insertExcelData(wb, sheet, cellPositions[i][j], (Collection<Object[]>) timeDataGroupings[j]);
				insertDataByPosition(timeDataGroupings, wb, sheet, cellPositions[i]);
//			}
		}
		return wb;
	}	*/
	
	/**
	 * 
	 * @param wb
	 * @param sheet
	 * @param startCellColumn 插入数据开始位置
	 * @param startRowNum 插入数据结束位置
	 * @param dataSet 数据集 多行单元格数据
	 * @return
	 */
	public static void insertExcelData(HSSFWorkbook wb, Sheet sheet, String cellPosition, Collection<Object[]> dataSet) {
		Object[] objectData = null;
		CellReference cr = new CellReference(cellPosition);
		int startRowNum = cr.getRow();
		for (Iterator<Object[]> it = dataSet.iterator(); it.hasNext();) {
			objectData = it.next();
			if(objectData!=null){
				insertExcelData(wb, sheet, cellPosition, startRowNum, objectData);
			}
			startRowNum ++;
		}
	}
	
	/**
	 * 插入指定位置数据 不能通用dataSet[i]数据是按时间分组的数据 第一个字段为时间
	 * @param wb
	 * @param sheet
	 * @param
	 * @param startRowNum
	 * @param dataSet 多个或一个单元格数据
	 * @return
	 */
	public static void insertExcelData(HSSFWorkbook wb, Sheet sheet, String cellPosition, int startRowNum, Object[] dataSet) {
		CellReference cr = new CellReference(cellPosition);
		int startCellColumn = cr.getCol();
		CellStyle dataStyle = ExcelUtil.getStyle(wb);
		dataStyle = ExcelUtil.getDataStyle(dataStyle);
		//插入一个单元数据
		if (dataSet.length == 1) {
			insertExcelCell(sheet, startCellColumn, cr.getRow(), dataSet[0], dataStyle);
		}
		else {

			//插入一行数据
			for (int i = startCellColumn; i < dataSet.length-3; i++) {//此处插入一行数据方式 只适用 插入位置为B开头且查询字段第一个为日期的 数据 startCellColumn=1
				insertExcelCell(sheet, i, startRowNum, dataSet[i], dataStyle);
			}
		}
	}
	//插入表头
	public static void insertExcelTableHead(HSSFWorkbook wb, Sheet sheet, String cellPosition, String dataSet) {
		CellReference cr = new CellReference(cellPosition);
		int startCellColumn = cr.getCol();
		CellStyle dataStyle = ExcelUtil.getStyle(wb);
		dataStyle = ExcelUtil.getDataStyle(dataStyle);
		//插入一个单元数据
		if (dataSet !=null && !"".equals(dataSet)) {
			insertExcelCell(sheet, startCellColumn, cr.getRow(), dataSet, dataStyle);
		}
	}
	
	public static void insertExcelCell(Sheet sheet, int cellCol, int startRowNum, Object dataObj, CellStyle dataStyle) {
		Row dataRow = sheet.getRow(startRowNum);
		if (dataRow == null) {
			dataRow = sheet.createRow(startRowNum);
		}
		Cell dataCell = dataRow.getCell(cellCol);
		if (dataCell == null) {
			dataCell = dataRow.createCell(cellCol);
			dataCell = ExcelUtil.setCellAndStyle(dataCell, dataObj, dataStyle);
			return;
		}
		dataCell = ExcelUtil.setCell(dataCell, dataObj);
	}
	
	/**
	 * 按时间段截取 数据并分组
	 * @param dataSet
	 * @return
	 */
	public static Object[] splitDataByTime(Collection<Object[]> dataSet,String[][] dates, String txtDate){
		//dataSet里面是对象数组的数组

		//分为三个对象数组
		Object[] dataGrouping = new Object[3];
		//创建一个对象数组的list
		List<Object[]> subList = null;
		Object[] o = null;
		for (int i = 0; i < dates.length; i++) {//筛选3次数据 数据量小
			subList = new ArrayList<Object[]>();
			for (String s : dates[i]) {
				//相当于对象
				Object[] dat = null;
				for (Iterator<Object[]> iterator = dataSet.iterator(); iterator.hasNext();) {
					//一行数据
					o = iterator.next();
					String ss = o[0].toString().substring(0,o[0].toString().length()-5);
					if (ss.equals(s)){//此行数据的时间相同的时间数据
						dat = o;
						break;
					}
				}
				if(dat == null){

				}else{
					subList.add(dat);
				}

			}
			dataGrouping[i] = subList;
		}
		return dataGrouping;
	}
	
	//传入日期，获取7天节点
	public static Object[] splitDataByWeek(Collection<Object[]> dataSet,String[][] dates, String txtDate){
		Object[] dataGrouping = new Object[7];		
		List<Object[]> subList = null;
		Object[] o = null;
		for (int i = 0; i < dates.length; i++) {//筛选3次数据 数据量小
			subList = new ArrayList<Object[]>();
			for (String s : dates[i]) {
				Object[] dat = null;
				for (Iterator<Object[]> iterator = dataSet.iterator(); iterator.hasNext();) {
					//一行数据
					o = iterator.next();
					if (o[0].toString().indexOf(s) != -1) {//此行数据的时间相同的时间数据
						dat = o;
						break;
					}					
				}
				subList.add(dat);
			}
			dataGrouping[i] = subList;
		}		
		return dataGrouping;
	}

	//传入日期，获取一个月节点
	public static Object[] splitDataByMonth(Collection<Object[]> dataSet,String[][] dates, String txtYear, String txtMonth){
		Object[] dataGrouping = new Object[3];		
		List<Object[]> subList = null;
		Object[] o = null;
		for (int i = 0; i < dates.length; i++) {//筛选3次数据 数据量小
			subList = new ArrayList<Object[]>();
			for (String s : dates[i]) {
				Object[] dat = null;
				for (Iterator<Object[]> iterator = dataSet.iterator(); iterator.hasNext();) {
					//一行数据
					o = iterator.next();

					if (o[1].toString().substring(8, 10).equals(s)) {//此行数据的时间相同的时间数据
						dat = o;
						break;
					}			
				}
				subList.add(dat);
			}
			dataGrouping[i] = subList;
		}		
		return dataGrouping;
	}
	
	/**
	 * 按分线分割数据
	 * @param dataSet
	 * @return
	 */
	public static Object[] splitDataByLine(Collection<Object[]> dataSet, String[] lineNames) {
		Object[] o = null;
		Object[] dataGrouping = null;
		List<Object[]> lineSet = null;
		for (int i = 0; i < lineNames.length; i++) {
			dataGrouping = new Object[lineNames.length];//按分线分组
			lineSet = new ArrayList<Object[]>();
			for (Iterator<Object[]> iterator = dataSet.iterator(); iterator.hasNext();) {
				o = iterator.next();
				// 判断归属分线
				if (o[1].equals(lineNames[i])) {
					lineSet.add(o);
				}
			}
			dataGrouping[i] = lineSet;
		}
		return dataGrouping;
	}
	/**
	 * 通用 按指定位置分组的数据插入 可插入多行、一行数据 或一个单元格数据
	 * @param objDataList 数据组
	 * @param wb
	 * @param st
	 * @param cellPositions 位置组长度与objDateList相同
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked" })
	public static void insertDataByPosition(Object[] objDataList, HSSFWorkbook wb, HSSFSheet st, String[] cellPositions) throws IOException{
		Collection<Object[]> dataSet = null;
		for (int i = 0; i < objDataList.length; i++) {
			dataSet = (Collection<Object[]>) objDataList[i];
			insertDataByPosition(dataSet, wb, st, cellPositions[i]);
		}
	}

	public static void insertDataByPositionZSBF(Collection<Object[]> objDataList, HSSFWorkbook wb, HSSFSheet st, String cellPosition) throws IOException{
		Object[] objectData = null;
		CellReference cr = new CellReference(cellPosition);
		int startRowNum = cr.getRow();
		int time=8;
		for (Iterator<Object[]> it = objDataList.iterator(); it.hasNext();) {
			objectData = it.next();
			int startCellColumn = cr.getCol();
			CellStyle dataStyle = ExcelUtil.getStyle(wb);
			dataStyle = ExcelUtil.getDataStyle(dataStyle);
			//插入一个单元数据
//			System.out.print(objectData);
			if (objectData.length == 1) {
				insertExcelCell(st, startCellColumn, startRowNum, objectData[0], dataStyle);
				st.setForceFormulaRecalculation(true);//是强制计算
				startRowNum ++;
				continue;
			}
			//插入一行数据

			if(Integer.parseInt(objectData[0].toString().split(" ")[1].split(":")[0])!=time)
			{
				if(Integer.parseInt(objectData[0].toString().split(" ")[1].split(":")[0])!=0)
				{
					int kongCi=Integer.parseInt(objectData[0].toString().split(" ")[1].split(":")[0])-time;
					System.out.println(Integer.parseInt(objectData[0].toString().split(" ")[1].split(":")[0])-time);
					for(int i=0;i<kongCi;i++)
					{
						for (int j = 1; j < objectData.length; j++) {
							insertExcelCell(st, j + startCellColumn - 1, startRowNum, "", dataStyle);
							st.setForceFormulaRecalculation(true);//是强制计算
						}
						startRowNum++;
						time=time+1;
						if(time==24)
							time=0;
					}
				}
				else {
					int kongci1 = 24 - time;

					for (int i = 0; i < kongci1; i++) {
						for (int j = 1; j < objectData.length; j++) {
							insertExcelCell(st, j + startCellColumn - 1, startRowNum, "", dataStyle);
							st.setForceFormulaRecalculation(true);//是强制计算
						}
						startRowNum++;
						time = time + 1;
						if (time == 24)
							time = 0;
					}
				}

			}

			if(time==24)
				time=0;

			if(Integer.parseInt(objectData[0].toString().split(" ")[1].split(":")[0])==time) {
				for (int j = 1; j < objectData.length; j++) {
					insertExcelCell(st, j + startCellColumn - 1, startRowNum, objectData[j], dataStyle);
					st.setForceFormulaRecalculation(true);//是强制计算
				}
			    time=time+1;
				startRowNum ++;
			}

		}
	}

	public static void insertDataByPosition(Collection<Object[]> objDataList, HSSFWorkbook wb, HSSFSheet st, String cellPosition) throws IOException{
		Object[] objectData = null;
		CellReference cr = new CellReference(cellPosition);
		int startRowNum = cr.getRow();
		for (Iterator<Object[]> it = objDataList.iterator(); it.hasNext();) {
			objectData = it.next();
			int startCellColumn = cr.getCol();
			CellStyle dataStyle = ExcelUtil.getStyle(wb);
			dataStyle = ExcelUtil.getDataStyle(dataStyle);
			//插入一个单元数据
			if (objectData.length == 1) {
				insertExcelCell(st, startCellColumn, startRowNum, objectData[0], dataStyle);
				st.setForceFormulaRecalculation(true);//是强制计算
				startRowNum ++;
				continue;
			}
			//插入一行数据
			for (int j = 0; j < objectData.length; j++) {//
				insertExcelCell(st, j + startCellColumn, startRowNum, objectData[j], dataStyle);
				st.setForceFormulaRecalculation(true);//是强制计算
			}
			startRowNum ++;
		}
	}
	@SuppressWarnings("rawtypes")
	public static void insertDataByPosition1(List objDataList, HSSFWorkbook wb, HSSFSheet st, String cellPosition) throws IOException{
		List objectData = new ArrayList();
		CellReference cr = new CellReference(cellPosition);
		int startRowNum = cr.getRow();
		for (int i = 0; i<objDataList.size();i++) {
			objectData = (List) objDataList.get(i);
			int startCellColumn = cr.getCol();
			CellStyle dataStyle = ExcelUtil.getStyle(wb);
			dataStyle = ExcelUtil.getDataStyle(dataStyle);
			//插入一个单元数据
			if (objectData.size() == 1) {
				insertExcelCell(st, startCellColumn, startRowNum, objectData.get(0), dataStyle);
				//st.setForceFormulaRecalculation(true);//是强制计算
				startRowNum ++;
				continue;
			}
			//插入一行数据
			for (int j = 0; j < objectData.size(); j++) {//
				insertExcelCell(st, j + startCellColumn, startRowNum, objectData.get(j), dataStyle);
				//st.setForceFormulaRecalculation(true);//是强制计算
			}
			startRowNum ++;
		}

	}
	
	public static void insertDataByPosition(Collection<Object[]> objDataList, HSSFWorkbook wb, HSSFSheet st, String cellPosition, CellStyle[] sytles) throws IOException{
		Object[] objectData = null;
		CellReference cr = new CellReference(cellPosition);
		int startRowNum = cr.getRow();
		for (Iterator<Object[]> it = objDataList.iterator(); it.hasNext();) {
			objectData = it.next();
			int startCellColumn = cr.getCol();
			//插入一个单元数据
			if (objectData.length == 1) {
				insertExcelCell(st, startCellColumn, startRowNum, objectData[0], sytles[0]);
				startRowNum ++;
				continue;
			}
			//插入一行数据
			for (int j = 0; j < objectData.length; j++) {//
				insertExcelCell(st, j + startCellColumn, startRowNum, objectData[j], sytles[j]);
			}
			startRowNum ++;
		}
	}
	
	/**
	 * 按指定位置的数据段分割数据
	 * @param dataSet 查询的数据字段需按要分割的单元格排列顺序查询
	 * @param positions start position,end position
	 * @return Object[][] 返回多组数据
	 */
	public static Object[] splitDataByPosition(Collection<Object[]> dataSet,String[][] positions){
		Object[] dataGrouping = new Object[positions.length];//多组单元格位置 分割dataSet为多组数据
		List<Object[]> subSet = null;
		Object[] o = null;
		int strartPosition = 0;
		int endPosition = 0;
		Object[] tempArray = null;
		for (int i = 0; i < positions.length; i++) {//筛选多次数据 
			//一组起始位置
				strartPosition = Integer.parseInt(positions[i][0]);
				endPosition = Integer.parseInt(positions[i][1]);
				subSet = new ArrayList<Object[]>();
				for (Iterator<Object[]> iterator = dataSet.iterator(); iterator.hasNext();) {
					//一行数据
					o = iterator.next();
					if (o.length < endPosition) {
						return dataGrouping;
					}
					tempArray = new Object[endPosition - strartPosition + 1];
					if (strartPosition == endPosition) {
						tempArray = new Object[1];
					}
					int t = 0;
					for (int j = strartPosition; j < endPosition + 1; j++) {
						tempArray[t] = o[j];
						t++;
					}
					subSet.add(tempArray);
					t = 0;
				}
				dataGrouping[i] = subSet;
		}
		return dataGrouping;
	}
	
	public static Sheet calcVale(Sheet sheet, String[] U2_OIL_calcFlag, String[] U2_OIL_calcValue,HSSFFormulaEvaluator eval) {
		CellReference cr = null;
		Row dataRow = null;
		Cell dataCell = null;
		for (int i = 0; i < U2_OIL_calcValue.length; i++) {
			cr = new CellReference(U2_OIL_calcFlag[i]);
			dataRow = sheet.getRow(cr.getRow());
			dataCell = dataRow.getCell(cr.getCol());
			cr = new CellReference(U2_OIL_calcValue[i]);
			dataRow = sheet.getRow(cr.getRow());
			dataCell = dataRow.getCell(cr.getCol());
			if (!"".endsWith(dataCell.toString())) {
				eval.evaluateFormulaCell(dataCell);
			}
		}
		return sheet;
	}
	
	public static void setDates (String txtDate, String[][] dates) throws ParseException {
		//将String转换成Date
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(simpleDateFormat.parse(txtDate));
		calendar.add(Calendar.DAY_OF_MONTH,-1);
		//txtDate = txtDate.substring(8,10);
		boolean flag = false;
		for (int i = 0; i < dates.length; i++) {
			for (int j = 0; j < dates[i].length; j++) {
				if (flag) {
					dates[i][j] = txtDate + " " + dates[i][j];
					continue;
				}
				if (dates[i][j].equals("00:00")) {
					dates[i][j] = txtDate + " " + dates[i][j];
					flag = true;
					continue;
				}
				dates[i][j] = simpleDateFormat.format(calendar.getTime()) + " " + dates[i][j];
			}
		}
//		return dates;
	}
	
	public static List<Object[]> splitLogData(Collection<Object[]> dataSet){
		List<Object[]> list = new ArrayList<Object[]>();
		List<Object[]> listTemp = new ArrayList<Object[]>();
		boolean calc = false;
		String dateValue = null;
		for (Object[] data :dataSet) {
			dateValue = data[0].toString().substring(8, 10);
			data[0] = data[0].toString().replaceAll("-", ".");
			if (dateValue.startsWith("1")) {
				if (dateValue.endsWith("0")) {//10
					listTemp.add(data);
					list.add(data);
					setLogDataList(list, listTemp);
					calc = true;
					continue;
				}
				if (!calc) {
					setLogDataList(list, listTemp);
					calc = true;//第一次合计 计算了
					listTemp = new ArrayList<Object[]>();
				}
				//没有10的情况
				listTemp.add(data);
				list.add(data);
				
			}
			
			if (dateValue.startsWith("2")) {
				if (dateValue.endsWith("0")) {//10
					listTemp.add(data);
					list.add(data);
					setLogDataList(list, listTemp);
					calc = false;//第二次合计 计算
					continue;
				}
				
				if (calc) {
					setLogDataList(list, listTemp);
					calc = false;//第二次合计 计算了
					listTemp = new ArrayList<Object[]>();
				}
				listTemp.add(data);
				list.add(data);
			}
			listTemp.add(data);
			list.add(data);
			
		}
		setLogDataList(list, listTemp);//算合计
		return list;
	}
	
	public static void setLogDataList(List<Object[]> list, List<Object[]> temList){
		String heji = "合计交罐";
		double hjValue = 0;
		Object[] hj = new Object[temList.get(0).length];
		hj[0] = heji;
		for (int i = 2; i < temList.get(0).length; i++) {
			for (int j = 0; j < temList.size(); j++) {
				if (temList.get(j)[i] == null) {
					continue;
				}
				hjValue += Double.parseDouble(temList.get(j)[i].toString());
			}
			hj[i] = hjValue;
			hjValue = 0;
		}
		list.add(hj);
	}
	
	@SuppressWarnings("unused")
	public static List<Object[]> splitU2szjcData(List<Object[]> dataSet){
		List<Object[]> list = new ArrayList<Object[]>();
		List<Object[]> listTemp = new ArrayList<Object[]>();
		Object[] newobj = null;
		boolean calc = false;
		String dateValue = dataSet.get(0)[0].toString();
		String dateMonth ="";
		Object[] konggehang = new Object[dataSet.get(0).length];
		for (Object[] data :dataSet) {
//			System.out.println(a.substring(5, 10));
//			System.out.println(a.substring(11, 16));
			dateMonth = data[0].toString().substring(5, 10);
				newobj = new Object[data.length] ;
			if(dateValue.equals(data[0].toString())){
				newobj = data;
				newobj[0] = dateMonth.replaceAll("-", "/");
				newobj[1] = data[1].toString().substring(11, 16);
				list.add(newobj);
			}else{
				list.add(konggehang);
				dateValue = data[0].toString();
				
				newobj = data;
				newobj[0] = dateMonth.replaceAll("-", "/");
				newobj[1] = data[1].toString().substring(11, 16);
				list.add(newobj);
				
			}
			
		}
		list.add(konggehang);
		return list;
	}
	public static List<Object[]> splitSumData(Collection<Object[]> dataSet){
		List<Object[]> list = new ArrayList<Object[]>();
		List<Object[]> listTemp = new ArrayList<Object[]>();
		for (Object[] data :dataSet) {
			list.add(data);
			listTemp.add(data);
		}
		setSumDataList(list, listTemp);//算合计
		return list;
	}
	public static void setSumDataList(List<Object[]> list, List<Object[]> temList){
		String heji = "日累计（m3）";
		Object hjValue = 0;
		double hjValue1 = 0;
		double hjValue2 = 0;
		Object[] hj = new Object[temList.get(0).length];
		hj[0] = heji;
		for (int i = 0; i < 9; i++) {
//			for (int j = 0; j < temList.size(); j++) {
//				if (temList.get(j)[i] == null) {
//					continue;
//				}
				if(temList.get(12)[i] !=null && temList.get(0)[i] !=null){
				hjValue1 = Double.parseDouble(temList.get(12)[i].toString());
				hjValue2 = Double.parseDouble(temList.get(0)[i].toString());
				hjValue = hjValue1 -hjValue2;
				}else{
					hjValue ="";
				}
			//}
			hj[i] = hjValue;
			hjValue = 0;
		}
		list.add(hj);
	}
/*	public static List<Object[]> addRemoDate(List<Object[]> dataSet){
		List<Object[]> list = new ArrayList<Object[]>();
		for (Object[] data : dataSet) {
				list.add(data);
		}
		setAllList(list);//算增减
		return list;
	}
	public static List<Object[]> setAllList(List<Object[]> list){
		Object hjValue = 0;
		List<Object[]> listObj = new ArrayList<Object[]>();
		Object[] hj = new Object[list.get(0).length];
		for (int i = 0; i < list.get(0).length; i++) {
				if(list.get(0)[i] !=null && list.get(1)[i] !=null){
					hjValue = Double.parseDouble(list.get(1)[i].toString()) -  Double.parseDouble(list.get(0)[i].toString());
				}else{
					hjValue="";
				}
				hj[i] = hjValue;
				hjValue = 0;
			}
			
			listObj.add(hj); 
		
		return listObj;
	}*/
}