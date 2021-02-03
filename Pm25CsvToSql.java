import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Pm25CsvToSql {	
    public void inputFromCsv() {    	
    	try(Connection conn = new Pm25JDBCDAO().getDataSource().getConnection();
    		PreparedStatement pstmt = conn.prepareStatement("insert into pm25 (SiteId,SiteName,County,MonitorDate,Concentration) values (?,?,?,?,?)"); 
    		){
			try {
				conn.setAutoCommit(false);
				List<Pm25> list = listPm25(); //呼叫listPm25			
				int count=0;
				for(int i=0;i<list.size();i++) {
					Pm25 inputUrlList = list.get(i);				
			        pstmt.setInt(1, inputUrlList.getSiteid());
			        pstmt.setString(2,inputUrlList.getSitename());
			        pstmt.setString(3, inputUrlList.getCounty());
			        pstmt.setDate(4, new java.sql.Date(inputUrlList.getMonitordate().getTime()));             
			        pstmt.setInt(5, inputUrlList.getConcentration());
			        count++;
			        pstmt.addBatch();				    
				}
	            pstmt.executeBatch();
	            pstmt.clearBatch();
	            pstmt.clearParameters();
	            conn.commit();
				System.out.println("成功新增:"+count+"筆資料");
			} catch (SQLException e) {
				System.out.println("資料庫錯誤，請重新操作!");
			} 	
		} catch (Exception e) {
			System.out.println("操作錯誤，請重新操作!");	
		}	
    }
	
    public List<Pm25> listPm25() throws IOException {
        List<Pm25> pm1List = new ArrayList<>();
        File file  = new File("./PM25.csv");
        String path=file.getCanonicalPath();
        System.out.println(path);
        try (   
        		InputStream is = new FileInputStream(path);
        	){            
            InputStreamReader isr = new InputStreamReader(is,"MS950");
            CSVParser parser = CSVFormat.EXCEL.withHeader().parse(isr);
            List<CSVRecord> results = parser.getRecords();
            for (int i = 0; i < results.size(); i++) {
                CSVRecord rs = results.get(i);
                Pm25 pm1 = new Pm25();
                String s1 = rs.get("SiteId");
                pm1.setSiteid(Integer.parseInt(s1));
				pm1.setSitename(rs.get("SiteName"));
				pm1.setCounty(rs.get("County"));
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
				Date javaDate = sdf.parse(rs.get("MonitorDate"));	//parse()字串轉日期				
				pm1.setMonitordate(javaDate);
				pm1.setConcentration(Integer.parseInt(rs.get("Concentration")));							
                pm1List.add(pm1);
            } 
        } catch (UnsupportedEncodingException e) {
			System.out.println("操作錯誤，請重新操作!");	
        } catch (IOException e) {
			System.out.println("操作錯誤，請重新操作!");	
        } catch (ParseException e) {
			System.out.println("操作錯誤，請重新操作!");	
		}
        return pm1List;
    }
   
    public void inputFromUrl(){

		URL url = null;
		try {url = new URL("https://data.epa.gov.tw/api/v1/aqx_p_322?limit=1000&api_key=9be7b239-557b-4c10-9775-78cadfc555e9&format=csv");
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
			System.out.println("網址或網址錯誤，請重新操作!");
		}		
		try (InputStream is = url.openStream();
				InputStreamReader isr = new InputStreamReader(is, "UTF8");
				BufferedReader br = new BufferedReader(isr);
				Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xepdb1", "xxxx",
						"xxxx");
				PreparedStatement pstmt = conn.prepareStatement(
				"insert into pm25 (SiteId,SiteName,County,MonitorDate,Concentration) values (?,?,?,?,?)");							
			){
			
				try (
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("select * from pm25 ");					
				) {
					conn.setAutoCommit(false);
					String readLine = null;
	//				System.out.println((Integer.valueOf("A")).getClass());
					int count = 0;
					while ((readLine = br.readLine()) != null) {
						String[] list = readLine.split(",");

					// 設定日期
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		             while(!(list[7].equals("MonitorDate")) && ((br.readLine()) != null )) {
//			             java.util.Date javaDate = sdf.parse(list[7].toString());		             
//			             java.sql.Date sqlDate =new java.sql.Date(javaDate.getTime());
//			             System.out.println(sqlDate);
//		             }
					// 寫入Oracle
					if (!(list[0].equals("?SiteId"))) { // 把標題列篩掉
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
//					pstmt.setInt(5, Integer.parseInt(list[8]));
					count++;
					pstmt.addBatch();						
					}
				}
				pstmt.executeBatch();			
				pstmt.clearBatch();
				pstmt.clearParameters();
				conn.commit();
				System.out.println("更新" + count + "筆資料完成");
			} catch (SQLException e) {
				System.out.println("資料庫錯誤，請重新操作!");
				conn.rollback();
			} catch (ParseException e) {
				System.out.println("資料庫錯誤，請重新操作!");
				conn.rollback();
			} catch (IOException e1) {
				System.out.println("資料庫錯誤，請重新操作!");
				conn.rollback();
			}	
		} catch (Exception e) {
			System.out.println("操作錯誤，請重新操作!");
		}
			
	}

}
