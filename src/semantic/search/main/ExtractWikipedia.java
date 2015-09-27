package semantic.search.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import org.jsoup.Jsoup;

public class ExtractWikipedia {
	final String endPoint = "http://en.wikipedia.org/wiki/";

	public int getWikiContent(String word){
		System.out.println("extracting wiki");
		String data = "";
		// url for wikipedia
		if(word.split("\\s+").length>1){
			word = word.replaceAll("\\s+", "_");
		}
		try {
			//download WIkipedia article and extract keyphrases
			URL url = new URL(endPoint+word);
			data = Jsoup.parse(url, 10000).text();
			System.out.println(url.toExternalForm());
			PrintWriter writer = new PrintWriter("data/tmp/wiki.txt", "UTF-8");
			writer.print(data);
			writer.close();
		} catch (IOException e) {
			
			e.printStackTrace();
			return -1;
		}
		return data.trim().length();
	}
}
