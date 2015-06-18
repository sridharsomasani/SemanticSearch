package semantic.search.main;


import semantic.search.utilities.Constants;
import maui.main.MauiModelBuilder;
import maui.main.MauiTopicExtractor;

public class ExtractKeyPhases {
	
	private String opts[];
	
	private MauiModelBuilder modelBuilder;
	private MauiTopicExtractor extractTopics;
	
	public void setOptions(String [] opts){
		this.opts = opts;
	}
	
	public String[] getOptions(){
		return opts;
	}
	
	public ExtractKeyPhases(){
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
	
	public void extractKeyPhases(String[] _opts){
		this.opts = _opts;
		if(opts != null){
			extractTopics = new MauiTopicExtractor();
			//buildModel();
			//System.out.println(getOptions());
			extractTopics.topicExtractor(Constants.mauiKeyOptions);
			//System.out.println(extractTopics.getOptions());
		}else {
			System.err.println("Please Provide Options to Extract KeyPhases");
		}
	}
	
	
}
