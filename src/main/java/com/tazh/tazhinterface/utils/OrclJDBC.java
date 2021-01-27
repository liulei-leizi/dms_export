package com.tazh.tazhinterface.utils;
import com.tazh.tazhinterface.pojo.TianRanQi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class  OrclJDBC {
	private static Connection conn = null;
	private static Statement  pStatement = null;
	/* 从postgre把数据插入到Oracle中 */
	public static void insertDataToOrcl(String sqlString) {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:orcl";
		String username = "SCOTT";
		String password = "ABCDqw8863766";
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			pStatement =  conn.createStatement();
			pStatement.execute(sqlString);
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
	}

	public static void selectDataToOrcl(String sqlString) {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:orcl";
		String username = "SCOTT";
		String password = "ABCDqw8863766";
		List<TianRanQi> lists = new ArrayList<>();
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			pStatement =  conn.createStatement();
			ResultSet rs= pStatement.executeQuery(sqlString);
			while (rs.next()) {
				TianRanQi tscd = new TianRanQi();
				tscd.setPressure(rs.getFloat("pressure"));
				tscd.setJihuaduibi(rs.getFloat("jihuaduibi"));
				lists.add(tscd);
			}
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
	}


}
