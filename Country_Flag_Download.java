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
    	// 读取countries.txt文件中的国家名，储存在ArrayList中
    	ArrayList<String> countries = readFileByLines(fileName);
    	
    	for(String country: countries) {
    		String page = doPost(country); // 获取国家所在的网页
    		if (page.indexOf("html") >= 0) { // 获取成功
    			getContent(page);            // 下载该国国家的国旗
    		}
    	}
    	
    	System.out.println("国旗下载完毕！");
    	
    }

    /* 发送HTTP的POST请求，获取指定国家的网页地址
     * 传入参数：country(国家): String类型
     */
    public static String doPost(String country){

        String url = "http://country.911cha.com/";

        try {
        	// 设置网址，打开连接
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            
            // 设置POST请求头和请求体，请求体的参数为国家(country)
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36";
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            String postParams = String.format("q=%s", country);

            // 传入POST请求体的参数
            conn.setDoOutput(true);
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
            os.write(postParams);
            os.flush();
            os.close();
            
            // 获取响应结果状态码
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { //如果响应状态码为200

                // 将HTML内容解析成UTF-8格式
                Document doc = Jsoup.parse(conn.getInputStream(), "utf-8", url);
                // 刷选需要的网页内容
                String page = doc.select("div.mcon").get(1)
                		                .selectFirst("ul")
                		                .selectFirst("li")
                		                .selectFirst("a")
                		                .attr("href");
                return page;

            } 
            else { // 如果响应状态码不是200， 则返回"Get page failed!"
                return "Get page failed.!";
            }
        }
        catch(Exception e){
            return "Get page failed.";
        }
    }
    
    // getContent()函数主要实现下载指定国家的国旗
    public static void getContent(String page){
    	
    	String base_url = "http://country.911cha.com/";
    	String url = base_url+page;
        
        try{
        	// 利用URL解析网址
            URL urlObj =  new URL(url);
            // URL连接
            URLConnection urlCon = urlObj.openConnection(); // 打开URL连接
            // 将HTML内容解析成UTF-8格式
            Document doc = Jsoup.parse(urlCon.getInputStream(), "utf-8", url);
            // 刷选需要的网页内容
            Element image = doc.selectFirst("img");
            String flag_name = image.attr("alt").replace("国旗", "");
            String flag_url = image.attr("src");
            
            URL httpurl = new URL(base_url+'/'+flag_url);
            // 利用FileUtils.copyURLToFile()实现图片下载
            FileUtils.copyURLToFile(httpurl, new File("E://flag/"+flag_name+".gif"));
            
            System.out.println(String.format("%s国旗下载成功~", flag_name));
            
        }
        catch(Exception e){
        	e.printStackTrace();
            System.out.println("下载失败！");
            
        }

    }
    
    // 以行读取文件，返回ArrayList, 里面的元素为每个国家的名称
    public static ArrayList<String> readFileByLines(String fileName) {  

    	File file = new File(fileName);  
    	BufferedReader reader = null;  // 设置reader为null
    	ArrayList<String> countries = new ArrayList<String>();

    	try {  

    		reader = new BufferedReader(new FileReader(file));  
    		String tempString = null;  
    		
    		// 一次读入一行，直到读入null为文件结束  
    		while ((tempString = reader.readLine()) != null)
    			countries.add(tempString); // 在列表中添加国家名称
    		 
    		reader.close(); // 关闭reader
    		
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
