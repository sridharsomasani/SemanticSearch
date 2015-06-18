package semantic.search.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.StringTokenizer;

import semantic.search.utilities.PorterStemmer;
import semantic.search.utilities.Stopwords;

public class InvertedIndex {
	private Map<String, LinkedHashSet<String>> postingList;
	private Stopwords stopWords;
	private PorterStemmer stemmer;
	
	public InvertedIndex(){
		postingList = new HashMap<String, LinkedHashSet<String>>();
		stemmer = new PorterStemmer();
//		this.folderPath = folderPath;
	}
	
	// build inverted index from folder containing .key files and path to stopwords
	public void buildInvertedIndex(String folderPath, String stopWordsPath){
		File dir = new File(folderPath);
		try {
			stopWords = new Stopwords(stopWordsPath);
			String[] files = dir.list();
			LinkedHashSet<String> tmp_list;
			String fileName;
			String currentLine;
			BufferedReader br = null;
			for(String file: files){
//				if(file.endsWith(".txt") || file.endsWith(".key")){
				if(file.endsWith(".key")){
					System.out.println(file);
					br = new BufferedReader(new FileReader(folderPath + "/" + file));
					fileName = file.substring(0, file.length()-4);
					while((currentLine = br.readLine()) != null){
						if(file.endsWith(".key")){
							if(postingList.containsKey(currentLine)){
								tmp_list = postingList.get(currentLine);
								tmp_list.add(fileName);
								postingList.put(currentLine, tmp_list);
							}else{
								tmp_list = new LinkedHashSet<String>();
								tmp_list.add(fileName);
								postingList.put(currentLine, tmp_list);
							}
						}
					}
				}
//				br.close();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Search Inverted Index
	public HashSet<String> searchInvertedIndex(String key){
		
		if(! key.equals("") || !key.equals(" ") || key != null){
			key = stemmer.stem(key);
			if(postingList.containsKey(key)){
				return postingList.get(key);
			}
		}
		return null;
	}
}
