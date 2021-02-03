import java.sql.*;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;

public class Delete {

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
	             ResultSet rs = stmt.executeQuery("select * from pm25");
	             PreparedStatement pstmt = conn.prepareStatement("delete from pm25 where siteid=?")
	    			) {
	        	 int i1;
	             Scanner sc = new Scanner(System.in);	             
	             System.out.println("請輸入代號以刪除地區資料1.基隆2:汐止3.萬里");
	             i1 = sc.nextInt();
	             System.out.println("您輸入第一個數字為：" + i1 );
	        	
//			    Pm25 pm25 = new Pm25();
//			    
//			    Scanner sc = new Scanner(System.in);
//			    int i=sc.nextInt();	
//			    
//			    pm25.setSiteld(i);
//			    System.out.println(pm25.getSiteld());
//			    
				while (rs.next()) {
				        int siteid =rs.getInt("siteid"); 
				        if(siteid==i1 && i1 < 4) {
				        	pstmt.setInt(1,i1);
				        	pstmt.addBatch();
				        }
				 }
   
				 int[] deleted =pstmt.executeBatch();
			    
			     pstmt.clearParameters();
			     pstmt.clearBatch();
			     System.out.println("總共刪除"+deleted[0]+"筆資料");
			}catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("您輸入的地區沒有資料");		
				e.printStackTrace();
			}catch (SQLException e) {
			    e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("finished");
		}

}
//	        ) {   
//	            while (rs.next()) {
//	                pstmt.setString(1,"基隆市"); //刪除南投縣資料
////	                pstmt.executeUpdate();
////	                pstmt.clearParameters();
//	                pstmt.addBatch();
//	            }
//	            int[] updated =pstmt.executeBatch();
////	            Delete.printResult(updated); //列印出執行結果
//                pstmt.clearParameters();
//                pstmt.clearBatch();
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	        }catch (Exception e) {
//	        	e.printStackTrace();
//	        }
//	
//	        System.out.println("finished");
//	    }
//
//	}

	