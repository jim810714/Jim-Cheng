import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class OracleToCsv {
	public void DBtoCsv() throws IOException {	
		try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@//localhost:1521/xepdb1", "xxxx", "xxxx");
            Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery("select * from pm25");	

        ) {conn.setAutoCommit(false);

            StringBuilder builder = new StringBuilder();
            builder.append("SITEID").append(",").append("SITENAME").append(",").append("COUNTY").append(",").append("MONITORDATE").append(",").append("CONCENTRATION");
            //DAO
            Pm25JDBCDAO pm25insertdao = new Pm25JDBCDAO();
 	        List<Pm25> listPm25s = pm25insertdao.listPm25s();
 	        	for(Pm25 list1:listPm25s) {	        		
 	        		int id =list1.getSiteid();
 	        		String name =list1.getSitename();
 	        		String county =list1.getCounty();
 	        		Date date =list1.getMonitordate();
 	        		int pollution =list1.getConcentration();
 	        		
 	        		builder.append(System.lineSeparator());
 	                builder.append(id).append(",");
 	                builder.append(name).append(",");
 	                builder.append(county).append(",");
 	                builder.append(date).append(",");
 	                builder.append(pollution).append(",");					
 	        	}
			//
//            while (rs.next()) {
//                int id = rs.getInt("SITEID");
//                String name =rs.getString("SITENAME");
//                String county =rs.getString("COUNTY");
//                String date = rs.getString("MONITORDATE");
//                int pollution = rs.getInt("CONCENTRATION");
//                                
//                builder.append(System.lineSeparator());
//                builder.append(id).append(",");
//                builder.append(name).append(",");
//                builder.append(county).append(",");
//                builder.append(date).append(",");
//                builder.append(pollution).append(",");
//            }
            
            File file  = new File("./OutputPm25.csv");
            String path=file.getCanonicalPath();
//            String path=file.getAbsolutePath();
            
            if(!file.exists()){
            	//先得到檔案的上級目錄，並建立上級目錄，再建立檔案
            	file.getParentFile().mkdir();
            	try {
            		//建立檔案
            		file.createNewFile();
            	}catch (IOException e) {
            		e.printStackTrace();
            	}
            }
            try ( FileOutputStream fos = new FileOutputStream(file);
                 OutputStreamWriter writer = new OutputStreamWriter(fos,"MS950");
                 BufferedWriter bufferedWriter = new BufferedWriter(writer) ){
                bufferedWriter.write(builder.toString());
            }catch (IOException ex){
                ex.printStackTrace();
            }

            System.out.println("finished");
            System.out.println("匯出路徑 = "+path);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
