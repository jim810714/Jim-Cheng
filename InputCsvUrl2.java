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

public class InputCsvUrl2 {


	public static void main(String[] args)  {

		URL url = null;
		try {url = new URL("https://data.epa.gov.tw/api/v1/aqx_p_322?limit=1000&api_key=9be7b239-557b-4c10-9775-78cadfc555e9&format=csv");
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		try (InputStream is = url.openStream();
				InputStreamReader isr = new InputStreamReader(is, "UTF8");
				BufferedReader br = new BufferedReader(isr);

				Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xepdb1", "xxxx",
						"XXXX");
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select * from pm25 ");
				PreparedStatement pstmt = conn.prepareStatement(
						"insert into pm25 (SiteId,SiteName,County,MonitorDate,Concentration) values (?,?,?,?,?)");) {
			String readLine = null;
//			System.out.println((Integer.valueOf("A")).getClass());
			int count = 0;
			while ((readLine = br.readLine()) != null) {
				String[] list = readLine.split(",");

				// 設定日期
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//	             while(!(list[7].equals("MonitorDate")) && ((br.readLine()) != null )) {
//		             java.util.Date javaDate = sdf.parse(list[7].toString());		             
//		             java.sql.Date sqlDate =new java.sql.Date(javaDate.getTime());
//		             System.out.println(sqlDate);
//	             }
				// 寫入Oracle
				if (!(list[0].equals("﻿SiteId"))) { // 把標題列篩掉
					try {
						String.valueOf((Integer.valueOf(list[8])).getClass());
						pstmt.setInt(5, Integer.parseInt(list[8]));
					} catch (NumberFormatException e) {
						pstmt.setInt(5, -1);
						// TODO: handle exception
					}

					pstmt.setInt(1, Integer.valueOf(list[0]));
					pstmt.setString(2, list[1]);
					pstmt.setString(3, list[2]);
					java.util.Date javaDate = sdf.parse(list[7]); // parse()字串轉日期
					pstmt.setDate(4, new java.sql.Date(javaDate.getTime()));
//		             pstmt.setInt(5, Integer.parseInt(list[8]));
					count++;
					pstmt.addBatch();
					pstmt.clearParameters();
				}
			}
			pstmt.executeBatch();
			System.out.println("更新" + count + "筆資料完成");
			pstmt.clearBatch();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
}
