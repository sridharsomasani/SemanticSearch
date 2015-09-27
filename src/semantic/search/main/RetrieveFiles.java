package semantic.search.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

import semantic.search.utilities.Constants;

public class RetrieveFiles {
	private InvertedIndex invertedIndex;
	private Thesaurus thesaurus;
	private ExtractWikipedia wiki;
	private ExtractKeyPhases wikiKeyPhrases;
	
	public RetrieveFiles(){
		invertedIndex = new InvertedIndex();
		thesaurus = new Thesaurus();
		//wikiKeyPhrases = new ExtractKeyPhases();
		wiki = new ExtractWikipedia();
	}
	
	public void buildIndex(String inputDir){
		invertedIndex.buildInvertedIndex(inputDir, "data/stopwords/stopwords_en.txt");
	}
	
	public HashSet<String> searchKey(String key){
		long lStartTime = System.nanoTime();
		long temp = 0;
		HashSet<String> fileList = invertedIndex.searchInvertedIndex(key);
		HashSet<String> list;
		//temp = System.nanoTime() - lStartTime;
		ArrayList<String> synonyms = thesaurus.getSynonyms(key);
		if(fileList ==null){
			fileList = new HashSet<String>();
		}
		//lStartTime = System.nanoTime();
		for(String synonym: synonyms){
			list = invertedIndex.searchInvertedIndex(synonym);
			if(list != null && list.size()>0){
				fileList.addAll(list);
			}
			
		}
		//temp = temp + System.nanoTime() - lStartTime;
		ArrayList<String> wikiKeys = extractKeysFromWiki(key);
		//lStartTime = System.nanoTime();
		if(wikiKeys != null){
			for(String wikiKey: wikiKeys){
				list = invertedIndex.searchInvertedIndex(wikiKey);
				if(list != null && list.size()>0){
					fileList.addAll(list);
				}
				
			}
		}
		long lEndTime = System.nanoTime();
		temp = temp + (lEndTime - lStartTime);
		System.err.println("Elapsed seconds: " + temp/1000000);
		return fileList;
	}
	
	public ArrayList<String> extractKeysFromWiki(String key){
		wikiKeyPhrases = new ExtractKeyPhases();
		int status = wiki.getWikiContent(key);
		if(status > 0){
			try {
				File file = new File("data/tmp/wiki.key");
				if(file.exists()){ 
					file.delete();
				}
				wikiKeyPhrases.extractKeyPhases(Constants.mauiKeyOptions);
				file = new File("data/tmp/wiki.key");
				if(file.exists()){
					ArrayList<String> keys = new ArrayList<String>();
					String currentLine;
					BufferedReader br = new BufferedReader(new FileReader("data/tmp/wiki.key"));
					while((currentLine = br.readLine()) != null){
						keys.add(currentLine);
						System.out.println(currentLine);
						//StringTokenizer tokens = new StringTokenizer(currentLine);
//						while(tokens.hasMoreTokens()){
//							keys.add(tokens.nextToken().trim());
//						}
					}
					br.close();
					return keys;
				}else {
					return null;
				}
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		return null;
	}
}
