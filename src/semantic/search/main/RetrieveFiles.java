package semantic.search.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import semantic.search.utilities.Constants;

/**
 * The <i>RetrieveFiles</i> class is used to search using various methods: <b>InvertedIndex, 
 * Synonyms and Wikipedia </b> articles
 * @author Sridhar Somasani
 *
 */
public class RetrieveFiles {
	private InvertedIndex invertedIndex;
	private Thesaurus thesaurus;
	private ExtractWikipedia wiki;
	private ExtractKeyPhrases wikiKeyPhrases;
	
	public RetrieveFiles(){
		invertedIndex = new InvertedIndex(Constants.minDocumentScore);
		thesaurus = new Thesaurus();
		wiki = new ExtractWikipedia();
	}
	
	/**
	 * Build inverted index from input directory containing .key files
	 * @param inputDir
	 */
	public void buildIndex(String inputDir){
		invertedIndex.buildInvertedIndex(inputDir, Constants.stopwordsPath_en);
	}
	
	/**
	 * @param key = search query
	 * @return returns map of filename, score for matched key on inverted index, synonyms 
	 * and key phrases from wikipedia articles 
	 * 
	 * @see ExtractWikipedia
	 * @see Thesaurus
	 */
	public Map<String, Double> searchKey(String key){
		long lStartTime = System.nanoTime();
		long temp = 0;
		Map<String, Double> fileList = invertedIndex.searchInvertedIndex(key);
		Map<String, Double> list;
		
		// get synonyms of the word
		HashSet<String> synonyms = thesaurus.getSynonyms(key);
		if(fileList == null){
			fileList = new HashMap<String, Double>();
		}
		// search synonyms on inverted index
		for(String synonym: synonyms){
			list = invertedIndex.searchInvertedIndex(synonym);
			if(list != null && list.size()>0){
				fileList.putAll(list);
			}
		}
		
		// extract key phrases from the wikipedia documents stored ExtractWikipedia class
		HashSet<String> wikiKeys = extractKeysFromWiki(key);
		// search using key phrases of wikipedia
		if(wikiKeys != null){
			for(String wikiKey: wikiKeys){
				list = invertedIndex.searchInvertedIndex(wikiKey);
				if(list != null && list.size()>0){
					fileList.putAll(list);
				}
				
			}
		}
		return fileList;
	}
	
	
	/**
	 * search wikipedia articles and extract key phrases from wikipedia articles stored in data/wiki/
	 * 
	 * @param query = search query in wikipedia articles
	 * @return wikipedia articles key phrases
	 */
	public HashSet<String> extractKeysFromWiki(String query){
		wikiKeyPhrases = new ExtractKeyPhrases();
		int status = 0;
		try {
			status = wiki.searchWiki(query);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(status > 0){
			try {
				wikiKeyPhrases.extractKeyPhrases(Constants.getTopicExtrOption(Constants.wikiLocale));
				File file = null;
				File folder = new File(Constants.wikiLocale);
				File [] files = folder.listFiles();
				HashSet<String> keys = new HashSet<String>();
				String currentLine;
				BufferedReader br;
				for(int i=0; i<files.length; i++){
					file = files[i];
					if(!file.isDirectory() && !file.getName().endsWith(".txt")){
						br = new BufferedReader(new FileReader(file));
						while((currentLine = br.readLine()) != null){
							keys.add(currentLine.trim().toLowerCase());
						}
						br.close();
					}
				}
				return keys;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("No Articles Found In Wikipedia For: " + query);
		return null;
	}
}
