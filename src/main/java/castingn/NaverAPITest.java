package castingn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NaverAPITest {

	 public static StringBuilder sb;//
	 
	    public static void main(String[] args) {

	    	String clientId = "HduUydUapj3YHGUmutex";
	        String clientSecret = "AwQAYItyE0";

	        int display = 100; // 검색결과갯수. 최대100개

	        try {
	            String text = URLEncoder.encode("복사용지A4(밀크75g/500매X5권/BOX/한국제지)", "utf-8");
	            String apiURL = "https://openapi.naver.com/v1/search/shop.json?query=" + text + "&display=" + display + "&";
	 
	            URL url = new URL(apiURL);
	            HttpURLConnection con = (HttpURLConnection) url.openConnection();
	            con.setRequestMethod("GET");
	            con.setRequestProperty("X-Naver-Client-Id", clientId);
	            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
	            int responseCode = con.getResponseCode();
	            BufferedReader br;
	            if (responseCode == 200) { 
	                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	            } else { 
	                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	            }
	            sb = new StringBuilder();
	            String line;
	 
	            while ((line = br.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            br.close();
	            con.disconnect();
	            System.out.println(sb);
	        } catch (Exception e) {
	            System.out.println(e);
	        }
	 
	    }

}
