package semantic.search.main;

import maui.main.MauiModelBuilder;
import maui.main.MauiTopicExtractor;

public class ExtractKeyPhrases {
	
	private String opts[];
	
	private MauiModelBuilder modelBuilder;
	private MauiTopicExtractor extractTopics;
	
	public void setOptions(String [] opts){
		this.opts = opts;
	}
	
	public String[] getOptions(){
		return opts;
	}
	
	public ExtractKeyPhrases(){
		modelBuilder = new MauiModelBuilder();
		extractTopics = new MauiTopicExtractor();
	}
	
	public void buildModel(String[] _opts){
		this.opts = _opts;
		if(opts != null){
			modelBuilder.buildAndSaveModel(opts);
		}else {
			System.err.println("Please provide Options to build Model");
		}
	}
	
	public void extractKeyPhrases(String[] _opts){
		this.opts = _opts;
		if(opts != null){
			//buildModel();
			extractTopics.topicExtractor(opts);
			//System.out.println(extractTopics.getOptions());
		}else {
			System.err.println("Please Provide Options to Extract KeyPhases");
		}
	}
	
	
}
