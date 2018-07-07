package wikiScrape;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.apache.commons.io.FileUtils;

public class Country_Flag_Download {

    public static void main(String[] args){
    	
    	String fileName ="E://flag/countries.txt";
    	// ��ȡcountries.txt�ļ��еĹ�������������ArrayList��
    	ArrayList<String> countries = readFileByLines(fileName);
    	
    	for(String country: countries) {
    		String page = doPost(country); // ��ȡ�������ڵ���ҳ
    		if (page.indexOf("html") >= 0) { // ��ȡ�ɹ�
    			getContent(page);            // ���ظù����ҵĹ���
    		}
    	}
    	
    	System.out.println("����������ϣ�");
    	
    }

    /* ����HTTP��POST���󣬻�ȡָ�����ҵ���ҳ��ַ
     * ���������country(����): String����
     */
    public static String doPost(String country){

        String url = "http://country.911cha.com/";

        try {
        	// ������ַ��������
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            
            // ����POST����ͷ�������壬������Ĳ���Ϊ����(country)
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36";
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            String postParams = String.format("q=%s", country);

            // ����POST������Ĳ���
            conn.setDoOutput(true);
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
            os.write(postParams);
            os.flush();
            os.close();
            
            // ��ȡ��Ӧ���״̬��
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { //�����Ӧ״̬��Ϊ200

                // ��HTML���ݽ�����UTF-8��ʽ
                Document doc = Jsoup.parse(conn.getInputStream(), "utf-8", url);
                // ˢѡ��Ҫ����ҳ����
                String page = doc.select("div.mcon").get(1)
                		                .selectFirst("ul")
                		                .selectFirst("li")
                		                .selectFirst("a")
                		                .attr("href");
                return page;

            } 
            else { // �����Ӧ״̬�벻��200�� �򷵻�"Get page failed!"
                return "Get page failed.!";
            }
        }
        catch(Exception e){
            return "Get page failed.";
        }
    }
    
    // getContent()������Ҫʵ������ָ�����ҵĹ���
    public static void getContent(String page){
    	
    	String base_url = "http://country.911cha.com/";
    	String url = base_url+page;
        
        try{
        	// ����URL������ַ
            URL urlObj =  new URL(url);
            // URL����
            URLConnection urlCon = urlObj.openConnection(); // ��URL����
            // ��HTML���ݽ�����UTF-8��ʽ
            Document doc = Jsoup.parse(urlCon.getInputStream(), "utf-8", url);
            // ˢѡ��Ҫ����ҳ����
            Element image = doc.selectFirst("img");
            String flag_name = image.attr("alt").replace("����", "");
            String flag_url = image.attr("src");
            
            URL httpurl = new URL(base_url+'/'+flag_url);
            // ����FileUtils.copyURLToFile()ʵ��ͼƬ����
            FileUtils.copyURLToFile(httpurl, new File("E://flag/"+flag_name+".gif"));
            
            System.out.println(String.format("%s�������سɹ�~", flag_name));
            
        }
        catch(Exception e){
        	e.printStackTrace();
            System.out.println("����ʧ�ܣ�");
            
        }

    }
    
    // ���ж�ȡ�ļ�������ArrayList, �����Ԫ��Ϊÿ�����ҵ�����
    public static ArrayList<String> readFileByLines(String fileName) {  

    	File file = new File(fileName);  
    	BufferedReader reader = null;  // ����readerΪnull
    	ArrayList<String> countries = new ArrayList<String>();

    	try {  

    		reader = new BufferedReader(new FileReader(file));  
    		String tempString = null;  
    		
    		// һ�ζ���һ�У�ֱ������nullΪ�ļ�����  
    		while ((tempString = reader.readLine()) != null)
    			countries.add(tempString); // ���б�����ӹ�������
    		 
    		reader.close(); // �ر�reader
    		
    		return countries;
    	} 
    	catch (IOException e) {  
    		return countries;  
    	} 
    	finally {  
    		if (reader != null) {  
    			try {  
    				reader.close();  
    			}
    			catch (IOException e1) {  
    				e1.printStackTrace();
    			}  

    		}  

    	}  

    } 
    

}
