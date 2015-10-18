package semantic.search.main;
import java.io.BufferedReader; 
import java.net.HttpURLConnection; 
import java.net.URL; 
import java.net.URLEncoder; 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.json.simple.*; // json package, download at http://code.google.com/p/json-simple/ 


/**
 * The <i>Thesaurus</i> class is used to extract synonyms of a word 
 * from {@linkplain <a href=http://thesaurus.altervista.org> http://thesaurus.altervista.org </a>} 
 * online dictionary using web service.
 * <pre>
 * {
    "response": [{
        "list": {
            "category": "(noun)",
            "synonyms": "hullo|hi|howdy|how-do-you-do|greeting|salutation"
        }
    }]
}
 * </pre>
 * @author Sridhar Somasani
 *
 */
class Thesaurus { 
  final String endpoint = "http://thesaurus.altervista.org/thesaurus/v1"; 
  private final String key = "QNsf0gOgUuMlu1LhosP7";
  private String language = "en_US";

 /**
 * Parse JSON containing synonyms
 * @param word = root word to obtain synonyms
 * @return HashSet<String> with synonyms
 */
public HashSet<String> getSynonyms(String word) {
	HashSet<String> synonyms = new HashSet<String>();
    try { 
      URL serverAddress = new URL(endpoint + "?word="+URLEncoder.encode(word, "UTF-8")+"&language="+language+"&key="+key+"&output=json"); 
      System.out.println(serverAddress.toExternalForm());
      HttpURLConnection connection = (HttpURLConnection)serverAddress.openConnection(); 
      connection.connect(); 
      int rc = connection.getResponseCode(); 
      if (rc == 200) { 
        String line = null; 
        BufferedReader br = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream())); 
        StringBuilder sb = new StringBuilder(); 
        while ((line = br.readLine()) != null) 
          sb.append(line + '\n'); 
        JSONObject obj = (JSONObject) JSONValue.parse(sb.toString()); 
        JSONArray array = (JSONArray)obj.get("response"); 
        for (int i=0; i < array.size(); i++) { 
          JSONObject list = (JSONObject) ((JSONObject)array.get(i)).get("list"); 
          System.out.println(list.get("category")+":"+list.get("synonyms"));
          StringTokenizer tokens = new StringTokenizer((String)list.get("synonyms"), "|");
          while(tokens.hasMoreTokens()){
        	  synonyms.add(tokens.nextToken().toLowerCase());
          }
        } 
      } else {
    	  System.out.println("HTTP error:"+rc); 
      } 
      connection.disconnect();
      
    } catch (java.net.MalformedURLException e) { 
      e.printStackTrace(); 
    } catch (java.net.ProtocolException e) { 
      e.printStackTrace(); 
    } catch (java.io.IOException e) { 
      e.printStackTrace(); 
    }
    return synonyms;
  } 
}