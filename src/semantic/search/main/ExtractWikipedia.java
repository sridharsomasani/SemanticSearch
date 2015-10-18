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

/**
 * The <i>ExtractWikipedia</i> class is used to extract related articles based on search query using Wikimedia API
 * @author Sridhar Somasani
 *
 */
public class ExtractWikipedia {
	
	private final String APIBase = "https://en.wikipedia.org/w/api.php?";
	private final String APIParams = "format=json&action=query&generator=search&indexpageids=&prop=extracts&exintro&explaintext&exlimit=max";
	private String searchLimit = "5";
	
	private final String endPoint = "http://en.wikipedia.org/wiki/";

	/**
	 * Search for word in the wikipedia articles. This method does not use Wikimedia search API.
	 * It directly fetches Wikipedia article if the query has matching url
	 * The content of the matched articles is stored in wiki.txt file
	 * in data/temp/wiki.txt
	 * @param word = search query
	 * @return status of the search. If search is successful, then return int greater than 0 else -1
	 */
	public int getWikiContent(String word) {
		System.out.println("extracting wiki");
		String data = "";
		
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
			
			// Jsoup to parse html and get only text discarding html tags
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
	
	/**
	 * This method uses Wikimedia full text search API on wikipedia articles(both title and corpus)<br>
	 * Key phrases are extracted from fetched articles and are stored in data/wiki/ folder <br>
	 * The search API returns extract of 5 articles from the match
	 * @param query = search query
	 * @return status of the search, if search yields any records then return int greater than 0 else -1
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public int searchWiki(String query) throws UnsupportedEncodingException, IOException{
		StringBuilder builder = new StringBuilder();
		builder.append(APIBase).append(APIParams);
		builder.append("&gsrlimit=").append(searchLimit).append("&gsrsearch=").append(URLEncoder.encode(query, "UTF-8"));
		String APIUrl = builder.toString();
		System.out.println(APIUrl);
		// delete all files in the directory
		FileUtils.cleanDirectory(new File(Constants.wikiLocale));
		// read JSON data from url
		String responseText = readResponseText(APIUrl);
		// parse and write JSON data to file
		int status = writeWikiExtract(responseText);
		
		return status;
	}
	
	/**
	 * Parses JSON article data from API and writes them to folder in data/wiki/
	 * sample JSON response
	 * <pre>
		 {
	*    "batchcomplete": "",
	*    "continue": {
	*        "gsroffset": 5,
	*        "continue": "gsroffset||"
	*    },
	*    "query": {
	*        "pageids": ["18552550", "17739", "36934", "7302046", "1595155"],
	*        "pages": {
	*            "18552550": {
	*                "pageid": 18552550,
	*                "ns": 0,
	*                "title": "Arkansas Radio Network",
	*                "index": 4,
	*                "extract": "Arkansas Radio Network (often abbreviated ARN) is a state-wide radio network serving radio stations in the state of Arkansas."
	*            },
	*            "17739": {
	*                "pageid": 17739,
	*                "ns": 0,
	*                "title": "Local area network",
	*                "index": 5,
	*                "extract": "A local area network (LAN) is a computer network that interconnects computers within a limited area 
	*            },
	*            "36934": {
	*                "pageid": 36934,
	*                "ns": 0,
	*                "title": "Network",
	*                "index": 1,
	*                "extract": "Network and networking may refer to:"
	*            },
	*            "7302046": {
	*                "pageid": 7302046,
	*                "ns": 0,
	*                "title": "Network (lobby group)",
	*                "index": 2,
	*                "extract": "Network (stylized NETWORK) is a Roman Catholic social justice lobby in Washington, D.C."
	*            },
	*            "1595155": {
	*                "pageid": 1595155,
	*                "ns": 0,
	*                "title": "Network administrator",
	*                "index": 3,
	*                "extract": ""
	*            }
	*        }
	*    },
	*    "limits": {
	*        "extracts": 20
	*    }
	*}
	*</pre>
	 * 
	 * @param responseText
	 * @return -1 if search is unsuccessful else >0
	 */
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
				// we use index as the file name to write data
				writeExtractToFile(data, indexFile);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageCount;
	}
	
	/**
	 * writes extracted data to file in data/wiki/
	 * @param data = data to be written
	 * @param fileName = filename
	 */
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
	
	/**
	 * @param url
	 * @return string data from url (responseText)
	 * @throws MalformedURLException
	 * @throws IOException
	 */
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
