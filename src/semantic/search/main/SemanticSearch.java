package semantic.search.main;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import maui.stemmers.PorterStemmer;
import semantic.search.utilities.Constants;
import semantic.search.utilities.Utils;

/**
 * The <i>Semantic Search</i> Class is the entry point for the application.
 * It takes two command line arguments<br>
 * Usage: SemanticSearch input_folder key/text files
 * 
 * @author Sridhar Somasani
 *
 */
public class SemanticSearch {

	public static void main(String[] args) {
		
		RetrieveFiles semanticSearch = new RetrieveFiles();
		PorterStemmer stemmer = new PorterStemmer();
		// validate command line arguments
		if(args.length != 2){
			Constants.commandLineUsage();
			System.exit(0);
		}
		// if text file option is used, first extract key phrases from the input folder
		if(args[1].equalsIgnoreCase("text")){
			new ExtractKeyPhrases().extractKeyPhrases(Constants.getTopicExtrOption(args[0]));
		}else if(!args[1].equalsIgnoreCase("key")){
			System.err.println("Invalid Second Argument: Enter key or text appropriately");
			Constants.commandLineUsage();
			System.exit(0);
		}
		
		// build inverted index from the input folder
		semanticSearch.buildIndex(args[0]);
		Map<String, Double> files;
		Scanner scan;
		while(true){
			System.out.println("Enter '-quit' To Exit");
			System.out.println("Enter Phrases to Seach:\n");
			scan = new Scanner(System.in);
			String key = scan.nextLine();
			if(key.equals("-quit")){
				System.err.println("Exiting Semantic Search!");
				break;
			}
			key = key.toLowerCase();
			// stem the key
			System.out.println(stemmer.stem(key));
			// search inverted index
			files = semanticSearch.searchKey(key);
			if(files != null && files.size() >0){
				// rank documents based on their score
				List<Map.Entry<String, Double>> ranked_docs = Utils.sortMapByValue(files);
				System.out.println("Found " + key + " In Following Files");
				for(Map.Entry<String, Double> entry: ranked_docs){
					System.out.println(entry.getKey() + "-->" + entry.getValue());
				}
			}else {
				System.out.println("No Files Found For " + key);
			}
		}
		scan.close();
	}

}
