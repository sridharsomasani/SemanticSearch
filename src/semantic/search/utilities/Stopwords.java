package semantic.search.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashSet;



/**
 * The <i>Stopwords</i> class is used to load English stop words
 * @author Sridhar Somasani
 *
 */
public class Stopwords implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** The location of the stopwords file **/
	//private static String filePath;
	
	/** The hashtable containing the list of stopwords */
	private HashSet<String> m_Stopwords = null;
	
	public Stopwords(String filePath) {
		if (m_Stopwords == null) {
			m_Stopwords = new HashSet<String>();
			File txt = new File(filePath);	
			InputStreamReader is;
			String sw = null;
			try {
				is = new InputStreamReader(new FileInputStream(txt), "UTF-8");
				BufferedReader br = new BufferedReader(is);				
				while ((sw=br.readLine()) != null)  {
					m_Stopwords.add(sw.toLowerCase());   
				}
				br.close();
				System.out.println("StopWords Loaded");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/** 
	 * Returns true if the given string is a stop word.
	 */
	public boolean isStopword(String str) {
		return m_Stopwords.contains(str.toLowerCase());
	}
}
		
		
