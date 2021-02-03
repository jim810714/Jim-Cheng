import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		URL url=new URL("https://data.epa.gov.tw/api/v1/aqx_p_322?limit=1000&api_key=9be7b239-557b-4c10-9775-78cadfc555e9&format=csv");
		String edString=url.openConnection().getContentType();
		System.out.println(edString);
	}

}
