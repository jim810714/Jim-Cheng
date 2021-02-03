import java.sql.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class Pm25JDBCDAO implements Pm25DAO  {
	private DataSource dataSource;
	
	public DataSource getDataSource() {
		if (dataSource == null) {			
			BasicDataSource ds = new BasicDataSource();
			ds.setDriverClassName("oracle.jdbc.OracleDriver");
			ds.setUrl("jdbc:oracle:thin:@//localhost:1521/xepdb1");
			ds.setUsername("test1");
			ds.setPassword("test");
			ds.setMaxTotal(50);
			dataSource = ds;
		}
		return dataSource;
	}	
	public List<Pm25> listPm25s(){
		List<Pm25> list = new ArrayList<Pm25>();
		try(  Connection conn = getDataSource().getConnection();
				Statement stmt = conn.createStatement();				
			){
			try(ResultSet rs = stmt.executeQuery("select * from pm25 order by SiteId");
				){
				conn.setAutoCommit(false);
				while(rs.next()) {
					Pm25 pm1 = new Pm25();				
					pm1.setSiteid(rs.getInt("siteId"));
					pm1.setSitename(rs.getString("sitename"));
					pm1.setCounty(rs.getString("county"));
					pm1.setMonitordate(rs.getDate("monitorDate"));
					pm1.setConcentration(rs.getInt("concentration"));
					list.add(pm1);
					}	
				conn.commit();
			} catch (SQLException e) {
				System.out.println("資料庫錯誤，請重新操作!");
				conn.rollback();				
			}
		}catch (Exception e) {
			System.out.println("操作錯誤，請重新操作!");	
		}
		return list;
	}		
//新增	
	public void insertIntoPm25(Pm25 pm1) {
	
		try( Connection conn = getDataSource().getConnection();			
			){	
				try	(PreparedStatement pstmt = conn.prepareStatement("insert into pm25 (SiteId,SiteName,County,MonitorDate,Concentration) values (?,?,?,?,?)");
				){					
					conn.setAutoCommit(false);
					pstmt.setInt(1,pm1.getSiteid());
					pstmt.setString(2,pm1.getSitename());
					pstmt.setString(3,pm1.getCounty());
					pstmt.setDate(4, new java.sql.Date(pm1.getMonitordate().getTime()));             
					pstmt.setInt(5, pm1.getConcentration());
					
					pstmt.addBatch();
			        pstmt.executeBatch();			        
			        pstmt.clearBatch();
			        pstmt.clearParameters(); 
			        conn.commit();
			        System.out.println("新增資料成功");
				}catch (SQLException e) {			
					conn.rollback();
					System.out.println("資料庫錯誤，請重新操作!");
				}
			} catch (Exception e) {
				System.out.println("操作錯誤，請重新操作!");	
			}
	}
//查詢
	public void queryPm25(Pm25 pm1) {
        try (Connection conn = getDataSource().getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery("select * from pm25 order by siteid");
	        ) {
        	
//	        	 int i1=15;//預設值設15
//	             Scanner sc = new Scanner(System.in);	             
//	             System.out.println("請輸入數值查詢各地區PM2.5資料(15以下為良好指標):");
//	             i1 = sc.nextInt();             
	        } catch (SQLException e) {
				System.out.println("資料庫錯誤，請重新操作!");
	        }catch (Exception e) {
				System.out.println("操作錯誤，請重新操作!");	
	        }	
	        System.out.println("finished");
	}
		
//更新(修改)
	public void updatePm25(Pm25 pm1) {
		try(Connection conn = getDataSource().getConnection();				
			){
			try(PreparedStatement pstmt = conn.prepareStatement("update pm25 set siteid=?,county=?,monitordate=?,CONCENTRATION=? where Siteid=?");
			){		
				conn.setAutoCommit(false);
				pstmt.setInt(1, pm1.getSiteid());
				pstmt.setString(2, pm1.getCounty());
				pstmt.setDate(3, new java.sql.Date(pm1.getMonitordate().getTime()));
				pstmt.setInt(4, pm1.getConcentration());
				pstmt.setInt(5, pm1.getSiteid());
				
				pstmt.addBatch();
				pstmt.executeBatch();
				pstmt.clearParameters();
				pstmt.clearBatch();
				conn.commit();
				System.out.println("更新資料成功!");
			} catch (SQLException e) {			
				conn.rollback();
				System.out.println("資料庫錯誤，請重新操作!");
			}
		} catch (Exception e1) {
			System.out.println("操作錯誤，請重新操作!");
		}	
	}
//刪除	
	public void deletePm25(Pm25 pm1){
		try (Connection conn = getDataSource().getConnection();					 	 
	    	) {
			try(Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select * from pm25");
				PreparedStatement pstmt = conn.prepareStatement("delete from pm25 where siteid=?")
				){		 
					conn.setAutoCommit(false);
					while (rs.next()) {
				        int siteid =rs.getInt("siteid"); 
				        if(siteid==pm1.getSiteid() ) { 
				        	pstmt.setInt(1,pm1.getSiteid());
				        	pstmt.addBatch();
				        }
					}
					pstmt.executeBatch();	    
				    pstmt.clearParameters();
				    pstmt.clearBatch();
				    conn.commit();
				    System.out.println("刪除資料成功!");
				}catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("您輸入的地區沒有資料");	
				}catch (SQLException e) {
					conn.rollback();
					System.out.println("資料庫錯誤，請重新操作!");
				}					
		}catch (SQLException e1) {
			System.out.println("資料庫錯誤，請重新操作!");
		}
	}
	
}