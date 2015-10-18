package semantic.search.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import maui.stemmers.PorterStemmer;
import semantic.search.utilities.Constants;

/**
 * The <i> InvertedIndex</i> class is used to construct inverted index from the extracted key phrases
 * It reads .key files from the folder and parse them and stores the posting list and their score.
 * The score is calculated based on the keyphrasedness.
 * @author Sridhar Somasani
 *
 */
public class InvertedIndex {
	private Map<String, Map<String, Double>> postingList;
	private PorterStemmer stemmer;
	private double minDocScore;
	public boolean hasScore = false;
	
	/**
	 * @param documentScore = minimum threshold score to include in inverted index
	 */
	public InvertedIndex(double documentScore){
		postingList = new HashMap<String, Map<String,Double>>();
		stemmer = new PorterStemmer();
		minDocScore = documentScore;
		if(minDocScore > 1.0){
			minDocScore = 0.0;
			System.out.println("Minimimum Document Score is greater than 1 \n setting doc score threshold to 0.0");
		}
	}
	
	// build inverted index from folder containing .key files and path to stopwords
	/**
	 * Builts inverted index from input folder reading only .key files
	 * 
	 * @param folderPath = input folder path (obtained from command line when program starts)
	 * @param stopWordsPath = path to stopwords file {@link Constants#stopwordsPath_en}
	 */
	public void buildInvertedIndex(String folderPath, String stopWordsPath){
		File dir = new File(folderPath);
		try {
			String[] files = dir.list();
			Map<String, Double> tmp_list;
			String fileName;
			String currentLine;
			BufferedReader br = null;
			for(String file: files){
				// read only key files
				if(file.endsWith(".key")){
					//System.out.println(file);
					br = new BufferedReader(new FileReader(folderPath + "/" + file));
					fileName = file.substring(0, file.length()-4);
					while((currentLine = br.readLine()) != null){
						currentLine = currentLine.toLowerCase();
						String [] tokens = currentLine.split("\t");
						
						double score = 0.0;
						// parse the key files with keyphrase and score
						// there are two types of .key files. 
						// format #1 : <keyphrase>
						// format #2 : <keyphrase> \t <keyphrase> \t <score>
						
						// for format #1
						String phrase = currentLine;
						
						// for format #2
						if(tokens.length == 3){
							phrase = tokens[0];
							score = Double.parseDouble(tokens[2]);
							hasScore = true;
						}
						
						//minimum score to include in inverted index
						if(score >= minDocScore ){
							if(postingList.containsKey(phrase)){
								tmp_list = postingList.get(phrase);
								tmp_list.put(fileName, score);
								postingList.put(phrase, tmp_list);
							}else{
								tmp_list = new HashMap<String, Double>();
								tmp_list.put(fileName, score);
								postingList.put(phrase, tmp_list);
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Search inverted index
	 * @param key to search in the inverted index
	 * @return
	 */
	public Map<String, Double> searchInvertedIndex(String key){
		
		if(! key.equals("") || !key.equals(" ") || key != null){
			key = stemmer.stem(key);
			if(postingList.containsKey(key)){
				return postingList.get(key);
			}
		}
		return null;
	}
}
