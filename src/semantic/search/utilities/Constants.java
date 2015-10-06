package semantic.search.utilities;

public class Constants {
	
	    // -l option for input folder
		// -m option for model
		// -t option for stemmer to be used
		// -v option for Vocabulary to be used
		// -n option for number of key phrases to be extracted
		
		
		
		public static String tempLocale = "data/temp/";
		
		public static String wikiLocale = "data/wiki/";
		
		public static String stopwordsPath_en = "data/stopwords/stopwords_en.txt";
		
		public static String[] getTopicExtrOption(String path) {
			String [] mauiKeyOptions = {
				"-l", "path", "-m", "keyPhaseExtractModel", "-t", "PorterStemmer", "-v", "none", "-n", "5"
			};
//			String [] options = mauiKeyOptions;
			mauiKeyOptions[1] = path;
			return mauiKeyOptions;
		}
		
		public static void commandLineUsage(){
			System.err.println("Invalid Command Line Arguments");
			System.err.println("Usage: <text_docs_base> <file_type>");
			System.err.println("text_docs_base: files to be indexed OR files to extract key phrases and index");
			System.err.println("file_type = text --> text_docs_base directory contains text files \n \t\t\t(implies extract and construct index)");
			System.err.println("file_type = key --> text_docs_base directory contains key files to be indexed");
		}

}
