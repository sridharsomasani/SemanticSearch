package semantic.search.utilities;

/**
 * The <i>Constants</i> class is used to store various constants of the application
 * @author Sridhar Somasani
 *
 */
public class Constants {
	
	    // -l option for input folder
		// -m option for model
		// -t option for stemmer to be used
		// -v option for Vocabulary to be used
		// -n option for number of key phrases to be extracted
		
		
		
		/**
		 * temporary directory used by the application
		 */
		public static String tempLocale = "data/temp/";
		
		/**
		 * folder to store wikipedia articles
		 */
		public static String wikiLocale = "data/wiki/";
		
		/**
		 * english stop words file path
		 */
		public static String stopwordsPath_en = "data/stopwords/stopwords_en.txt";
		
		/**
		 * @param path = input directory
		 * @return returns Maui 1.2 options with input directory
		 */
		public static String[] getTopicExtrOption(String path) {
			String [] mauiKeyOptions = {
				"-l", "path", "-m", "keyPhaseExtractModel", "-t", "PorterStemmer", "-v", "none", "-n", "10", "-a"
			};
			mauiKeyOptions[1] = path;
			return mauiKeyOptions;
		}
		
		/**
		 * Minimum document score to include in inverted index
		 */
		public static double minDocumentScore = 0.0;
		
		/**
		 * prints the command line options and usage
		 */
		public static void commandLineUsage(){
			System.err.println("Invalid Command Line Arguments");
			System.err.println("Usage: <text_docs_base> <file_type>");
			System.err.println("text_docs_base: files to be indexed OR files to extract key phrases and index");
			System.err.println("file_type = text --> text_docs_base directory contains text files \n \t\t\t(implies extract and construct index)");
			System.err.println("file_type = key --> text_docs_base directory contains key files to be indexed");
		}

}
