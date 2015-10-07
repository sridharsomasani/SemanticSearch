package semantic.search.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import semantic.search.utilities.Constants;

public class RetrieveFiles {
	private InvertedIndex invertedIndex;
	private Thesaurus thesaurus;
	private ExtractWikipedia wiki;
	private ExtractKeyPhrases wikiKeyPhrases;
	
	public RetrieveFiles(){
		invertedIndex = new InvertedIndex();
		thesaurus = new Thesaurus();
		//wikiKeyPhrases = new ExtractKeyPhases();
		wiki = new ExtractWikipedia();
	}
	
	public void buildIndex(String inputDir){
		invertedIndex.buildInvertedIndex(inputDir, Constants.stopwordsPath_en);
	}
	
	public HashSet<String> searchKey(String key){
		long lStartTime = System.nanoTime();
		long temp = 0;
		HashSet<String> fileList = invertedIndex.searchInvertedIndex(key);
		HashSet<String> list;
		//temp = System.nanoTime() - lStartTime;
		HashSet<String> synonyms = thesaurus.getSynonyms(key);
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
		HashSet<String> wikiKeys = extractKeysFromWiki(key);
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
	
	public HashSet<String> extractKeysFromWiki(String key){
		wikiKeyPhrases = new ExtractKeyPhrases();
		int status = 0;
		try {
			status = wiki.searchWiki(key);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
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
//							System.out.println(currentLine + " --> " + file.getName());
						}
						br.close();
					}
				}
				return keys;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("No Articles Found In Wikipedia For: " + key);
		return null;
	}
}
