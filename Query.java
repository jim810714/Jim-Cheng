import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;

public class Query {

	public void Query1(){

			BasicDataSource dataSource = new BasicDataSource();
	        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
	        dataSource.setUrl("jdbc:oracle:thin:@//localhost:1521/xepdb1");
	        dataSource.setUsername("test1");
	        dataSource.setPassword("test");
	        dataSource.setMaxTotal(50); //設定最多connection上線,超過使用量必須等待
	        dataSource.setMaxIdle(50);   //設定最多idle的connection,超過的connection會被執行connection.close()

	        try (Connection conn = dataSource.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery("select * from pm25 order by siteid");
	        ) {
	        	 int i1=15;
	             Scanner sc = new Scanner(System.in);	             
	             System.out.println("請輸入數值查詢各地區PM2.5資料(15以下為良好指標):");
	             i1 = sc.nextInt();
	             
	            while (rs.next()) {
	            	int id = rs.getInt("SITEID");
	            	String name = rs.getString("SITENAME");
	            	int pollution = rs.getInt("CONCENTRATION");	            	
	                String county = rs.getString("COUNTY");
	                
	                
	                if((pollution>=i1)  ) {//查詢新北市數值大於15的資料
	                System.out.println(county+"PM2.5超過"+ i1 +"的縣市: 編號:"+ id+ ",區域:"+ name+ ",數值:"+ pollution  );
	                }
	            }
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException(e);
	        }catch (Exception e) {
	        	e.printStackTrace();
	        }
	
	        System.out.println("finished");
	    }
	}