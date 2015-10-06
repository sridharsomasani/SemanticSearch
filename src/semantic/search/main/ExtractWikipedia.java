package semantic.search.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;

import semantic.search.utilities.Constants;

public class ExtractWikipedia {
	final String endPoint = "http://en.wikipedia.org/wiki/";
	final String APIBase = "https://en.wikipedia.org/w/api.php?";
	final String APIParams = "format=json&action=query&generator=search&indexpageids=&prop=extracts&exintro&explaintext&exlimit=max";
	private String searchLimit = "5";

	public int getWikiContent(String word) {
		System.out.println("extracting wiki");
		String data = "";
		// url for wikipedia
		StringTokenizer st = new StringTokenizer(word);
		String key = "";
		File file = new File(Constants.wikiLocale + "wiki.txt");
		if(file.exists()) file.delete();
		if (st.countTokens() > 1) {
			key = st.nextToken();
			while (st.hasMoreTokens()) {
				key = key + "_" + st.nextToken();
			}
		} else {
			key = word;
		}
		try {
			// download Wikipedia article and extract keyphrases
			URL url = new URL(endPoint + key);
			data = Jsoup.parse(url, 10000).text();
			System.out.println(url.toExternalForm());
			PrintWriter writer = new PrintWriter(Constants.wikiLocale + "wiki.txt", "UTF-8");
			writer.print(data);
			writer.close();
		} catch (IOException e) {
			System.err.println("Invalid wiki url for search string : " + word + " url : " + endPoint + key);
			// e.printStackTrace();
			return -1;
		}
		return data.trim().length();
	}
	
	public int searchWiki(String query) throws UnsupportedEncodingException, IOException{
		StringBuilder builder = new StringBuilder();
		builder.append(APIBase).append(APIParams);
		builder.append("&gsrlimit=").append(searchLimit).append("&gsrsearch=").append(URLEncoder.encode(query, "UTF-8"));
		String APIUrl = builder.toString();
		System.out.println(APIUrl);
		FileUtils.cleanDirectory(new File(Constants.wikiLocale));
		String responseText = readResponseText(APIUrl);
		int status = writeWikiExtract(responseText);
		
		return status;
	}
	
	private int writeWikiExtract(String responseText){
		JSONObject response;
		int pageCount = -1;
		try {
			response = (JSONObject) new JSONParser().parse(responseText);
			JSONObject query = (JSONObject) response.get("query");
			if(query == null){
				return -1;
			}
			JSONArray pageids = (JSONArray) query.get("pageids");
			pageCount = pageids.size();
			Iterator<String> pages = pageids.iterator();
			JSONObject pageNodes = (JSONObject) query.get("pages");
			while(pages.hasNext()){
				String pageid = pages.next();
				JSONObject page = (JSONObject) pageNodes.get(pageid);
				String data = (String) page.get("extract");
				if( data == null || data.trim().equals("")){
					data = (String)page.get("title");
				}
				String indexFile = ((Long) page.get("index")).toString();
				writeExtractToFile(data, indexFile);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageCount;
	}
	
	private void writeExtractToFile(String data, String fileName){
		 String filePath = Constants.wikiLocale + fileName + ".txt";
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(filePath, "UTF-8");
			writer.print(data);
		} catch (Exception e) {
			System.err.println("Error Writing Wiki Search Extract to File!");
			e.printStackTrace();
		} 
		writer.close();
	}
	
	private String readResponseText(String url) throws MalformedURLException, IOException{
		InputStream in = new URL(url).openStream();
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
			StringBuilder builder = new StringBuilder();
			int charRet;
			while( (charRet = reader.read()) != -1){
				builder.append((char) charRet);
			}
			return builder.toString();
		} finally{
			in.close();
		}
		
	}
}
