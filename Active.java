import java.io.IOException;
import java.util.Scanner;

public class Active {

	public static void main(String[] args) throws IOException {
		try {
			for(int i=0;i<1;) {	
				int i1;
		        Scanner sc = new Scanner(System.in);	             
		        System.out.println("請輸入代號以執行功能:1.匯入2.新增3.查詢4.修改5.刪除6.匯出");
		        i1 = sc.nextInt();		        	
		        switch (i1) {
				case 1://匯入					
					SqlFunction inputFromCsv = new SqlFunction();
					inputFromCsv.inputCsvToSql();
					break;
				case 2://新增						
					SqlFunction insert = new SqlFunction();
					insert.insertIntoSql();
					break;
				case 3://查詢						
					SqlFunction query = new SqlFunction();
					query.querySql();
					break;
				case 4://更新(修改)					 
					SqlFunction update = new SqlFunction();
					update.updateSql();
					break;
				case 5://刪除					
		 			SqlFunction delete = new SqlFunction();
					delete.deleteSql();
					break;
				case 6://匯出				    
					SqlFunction output = new SqlFunction();
					output.outputToCsv();
					break;		
				default:
					System.out.println("輸入錯誤，請重新輸入!!!");
					break;
				}
			}	
		} catch (java.util.InputMismatchException e) {
			System.out.println("輸入錯誤，請重新輸入!!!");
		}
	}
}
