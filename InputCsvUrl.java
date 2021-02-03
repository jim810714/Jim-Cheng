import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InputCsvUrl {
	static int count = 0;
	public static void main(String[] args)  {
		
		URL url = null;
		try {
			url = new URL("https://data.epa.gov.tw/api/v1/aqx_p_322?limit=1000&api_key=9be7b239-557b-4c10-9775-78cadfc555e9&format=csv");
		} catch (MalformedURLException e) {
			System.out.println("網站輸入錯誤，請重新輸入!");			
		}
		try (InputStream is = url.openStream();
			InputStreamReader isr=new InputStreamReader(is,"UTF8");
			BufferedReader br = new BufferedReader(isr);
				
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xepdb1", "xxxx",
			"xxxx");){
			try(Statement stmt = conn.createStatement();
		        ResultSet rs = stmt.executeQuery("select * from pm25 ");
		        PreparedStatement pstmt = conn.prepareStatement("insert into pm25 (SiteId,SiteName,County,MonitorDate,Concentration) values (?,?,?,?,?)");	
				){
				conn.setAutoCommit(false);
				String readLine = null;				
				while ((readLine = br.readLine()) != null ) {
		             String[] list = readLine.split(",");	             
		           //設定日期
		             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 	             
	//	             while(!(list[7].equals("MonitorDate")) && ((br.readLine()) != null )) {
	//		             java.util.Date javaDate = sdf.parse(list[7].toString());		             
	//		             java.sql.Date sqlDate =new java.sql.Date(javaDate.getTime());
	//		             System.out.println(sqlDate);
	//	             }
		           //寫入Oracle  		             
		             if( !(list[0].equals("﻿SiteId"))) { //把標題列篩掉
		            	 if(list[8].getClass().equals("String")) {
		            		 pstmt.setInt(5, -1);
		            	 }else {
				             pstmt.setInt(5, Integer.parseInt(list[8]));
		            	 }		            		 
		            	 pstmt.setInt(1, Integer.valueOf(list[0]));
			             pstmt.setString(2, list[1]);
			             pstmt.setString(3, list[2]);
			             java.util.Date javaDate = sdf.parse(list[7]);	//parse()字串轉日期
			             pstmt.setDate(4, new java.sql.Date(javaDate.getTime()));             
			             pstmt.setInt(5, Integer.parseInt(list[8]));
			             count++;
			             pstmt.addBatch();		            	 
		             }
		 		}
	            pstmt.executeBatch();
	            pstmt.clearBatch();
	            pstmt.clearParameters();
	            conn.commit();
	        	System.out.println("更新" + count + "筆資料完成");
			}catch (SQLException e1) {
				conn.rollback();
				System.out.println("資料庫錯誤，請重新操作!");
			}catch (Exception e2) {
				conn.rollback();
				System.out.println("錯誤，請重新操作!");
			}
	}catch (SQLException e1) {		
		System.out.println("資料庫錯誤，請重新操作!");
	}catch (Exception e2) {
		System.out.println("錯誤，請重新操作!");
	}
	}
}
