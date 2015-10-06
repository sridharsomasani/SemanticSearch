package semantic.search.main;

import java.util.HashSet;
import java.util.Scanner;

import semantic.search.utilities.Constants;
import semantic.search.utilities.PorterStemmer;

public class SemanticSearch {

	public static void main(String[] args) {
		
		RetrieveFiles semanticSearch = new RetrieveFiles();
		PorterStemmer stemmer = new PorterStemmer();
		if(args.length != 2){
			Constants.commandLineUsage();
			System.exit(0);
		}
		if(args[1].equalsIgnoreCase("text")){
			new ExtractKeyPhrases().extractKeyPhrases(Constants.getTopicExtrOption(args[0]));
		}else if(!args[1].equalsIgnoreCase("key")){
			System.err.println("Invalid Second Argument: Enter key or text appropriately");
			Constants.commandLineUsage();
			System.exit(0);
		}
		
		
		semanticSearch.buildIndex(args[0]);
		HashSet<String> files;
		Scanner scan;
		while(true){
			System.out.println("Enter '-quit' To Exit");
			System.out.println("Enter Phrases to Seach:");
			scan = new Scanner(System.in);
			String key = scan.nextLine();
			if(key.equals("-quit")){
				System.err.println("Exiting Semantic Search!");
				break;
			}
			key = key.toLowerCase();
			System.out.println(stemmer.stem(key));
			files = semanticSearch.searchKey(key);
			if(files != null && files.size()>0){
				System.out.println("Found " + key + " In Following Files");
				for(String file: files){
					System.out.println(file);
				}
				
			}else {
				System.out.println("No Files Found For " + key);
			}
			
		}
		scan.close();
	}

}
