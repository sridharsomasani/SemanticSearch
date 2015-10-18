package semantic.search.main;

import java.util.HashSet;

import semantic.search.utilities.Constants;
import maui.main.MauiModelBuilder;
import maui.main.MauiTopicExtractor;

/**
 * The <i>ExtractKeyPhrases</i> class extracts key phrases from the documents
 * It internally calls Maui 1.2 tool for key phrase extraction
 * It takes options specified in {@link Constants#getTopicExtrOption(String)}
 * 
 * @author Sridhar Somasani
 * 
 * @see {@link Constants#getTopicExtrOption(String)}
 */

public class ExtractKeyPhrases {
	
	
	private MauiModelBuilder modelBuilder;
	private MauiTopicExtractor extractTopics;
	
	
	
	public ExtractKeyPhrases(){
		modelBuilder = new MauiModelBuilder();
		extractTopics = new MauiTopicExtractor();
	}
	
	/**
	 * -l option for input folder <br>
 	 * -m option for model name to save <br>
	 * -t option for stemmer to be used <br>
	 * @param options provide options for building model: same as Maui 1.2 options
	 */
	public void buildModel(String[] options){
		if(options != null){
			// change to 1 for short documents
			modelBuilder.minNumOccur = 1;

			HashSet<String> fileNames;
			try {
				modelBuilder.setOptions(options);
				fileNames = modelBuilder.collectStems();
				modelBuilder.buildModel(fileNames);
				modelBuilder.saveModel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			System.err.println("Please provide Options to build Model");
		}
	}
	
	
	/**
	 * -l option for input folder <br>
 	 * -m option for model name <br>
	 * -t option for stemmer to be used <br>
	 * -v option for Vocabulary to be used <br>
	 * -n option for number of key phrases to be extracted <br>
	 * -a additional information in generated key files <br>
	 * Usage: "-l", "path", "-m", "keyPhaseExtractModel", "-t", "PorterStemmer", "-v", "none", "-n", "10", "-a"  <br>
	 * @param options Provide options for key phrase extraction
	 */
	public void extractKeyPhrases(String[] options){
		if(options != null){
			try {
				extractTopics.setOptions(options);
				extractTopics.loadModel();
				HashSet<String> fileNames = extractTopics.collectStems();
				extractTopics.extractKeyphrases(fileNames);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			System.err.println("Please Provide Options to Extract KeyPhases");
		}
	}
	
	
}
