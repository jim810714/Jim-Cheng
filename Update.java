import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;

public class Update {

	public static void main(String[] args) {

			BasicDataSource dataSource = new BasicDataSource();
	        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
	        dataSource.setUrl("jdbc:oracle:thin:@//localhost:1521/xepdb1");
	        dataSource.setUsername("test1");
	        dataSource.setPassword("test");
	        dataSource.setMaxTotal(50); //設定最多connection上線,超過使用量必須等待
	        dataSource.setMaxIdle(50);   //設定最多idle的connection,超過的connection會被執行connection.close()

	        try (Connection conn = dataSource.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery("select * from pm25 ");
	             PreparedStatement pstmt = conn.prepareStatement("update pm25 set concentration=? where sitename=?")
	        ) {
	        	String s1;
	        	int i1;
	            Scanner sc = new Scanner(System.in);	             
	            System.out.println("請輸入區域以用來修改PM2.5指標(ex:士林、板橋、觀音):");
	            s1 = sc.next();
	            System.out.println("請輸入數字修改PM2.5數值:");
	            i1 = sc.nextInt();
	            
	            int count = 0;
	            while (rs.next()) {
	                String sitename = rs.getString("sitename");
                
	                if ( sitename.equals(s1)){ //選擇板橋
	                    pstmt.setInt(1,i1);  //把汙染指數改成201
	                    count++;
	                }else{
	                	pstmt.setString(1,rs.getString("concentration"));
	                }   	                
	                pstmt.setString(2,sitename);
	                pstmt.executeUpdate();
	                pstmt.clearParameters();
	                
	            }
	            System.out.println("更新"+count+"筆資料");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }catch (Exception e) {
	        	e.printStackTrace();
	        }
	
	        System.out.println("finished");
	    }
	
	}