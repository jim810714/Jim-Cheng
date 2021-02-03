import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;

public class Insertinto {
	static int count = 0;
	public static void main(String[] args){
		
		BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@//localhost:1521/xepdb1");
        dataSource.setUsername();
        dataSource.setPassword();
        dataSource.setMaxTotal(50); //設定最多connection上線,超過使用量必須等待
        dataSource.setMaxIdle(50);   //設定最多idle的connection,超過的connection會被執行connection.close()

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from pm25 ");
             PreparedStatement pstmt = conn.prepareStatement("insert into pm25 (SiteId,SiteName,County,MonitorDate,Concentration) values (?,?,?,?,?)")
        ) {
        	int i1;
        	String s1;
        	String s2;
        	String s3;
        	int i2;
            Scanner sc = new Scanner(System.in);	
            System.out.println("請輸入編號以新增資料:");
            i1 = sc.nextInt();
            System.out.println("請輸入區域名稱以新增資料:");
            s1 = sc.next();
            System.out.println("請輸入縣市名稱以新增資料:");
            s2 = sc.next();
            System.out.println("請輸入日期以新增資料(ex:2020-09-09):");
            s3 = sc.next();
            System.out.println("請輸入PM25數值以新增資料:");
            i2 = sc.nextInt();
        	
////			String readLine = null;
//			while (s1 != null) {
////	        String[] list = readLine.split(",");
//	             
	           //寫入Oracle  

//          pstmt.setInt(1, Integer.valueOf(list[0]));
//	        pstmt.setString(2, list[1]);
//		    pstmt.setString(3, list[2]);
//	        java.util.Date javaDate = sdf.parse(list[7]);	//parse()字串轉日期
//		    pstmt.setDate(4, new java.sql.Date(javaDate.getTime()));             
//		    pstmt.setInt(5, Integer.valueOf(list[8]));
		    
		    pstmt.setInt(1,i1);
		    pstmt.setString(2,s1);
		    pstmt.setString(3,s2);
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    java.util.Date javaDate = sdf.parse(s3);	//parse()字串轉日期
		    pstmt.setDate(4, new java.sql.Date(javaDate.getTime()));             
		    pstmt.setInt(5, i2);
		    count++;
		    pstmt.addBatch();
            pstmt.executeBatch();
            pstmt.clearBatch();
            pstmt.clearParameters();     
	 		

		}catch (SQLException e) {			
			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		System.out.println("新增" + count + "筆資料完成");
	}
}
