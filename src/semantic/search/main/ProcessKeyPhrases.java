package semantic.search.main;

import java.util.Arrays;

public class ProcessKeyPhrases {

	public static void main(String[] args) {
		ExtractKeyPhases buildAndExtract = new ExtractKeyPhases();
		System.out.println(args);
		
		
		
		if(args.length > 2){
			String task = args[1];
			String[] options = Arrays.copyOfRange(args, 1, args.length);
			System.out.println(options);
			if(task.equals("-model")){
				buildAndExtract.buildModel(options);
			}else if(task.equals("-extract")){
				buildAndExtract.extractKeyPhases(options);
			}else {
				System.err.println("Invalid Options");
			}
		}else {
			System.err.println("Please Provide Arguments");
			System.err.format("Usage: %s taskSwitch taskOptions....", args[0]);
			System.err.println("Options: -model taskOption : Build Model");
			System.err.println("Options: -extract taskOption : Extract KeyPhrases");
		}
	}
}
