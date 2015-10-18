package semantic.search.main;

import java.util.Arrays;

/**
 * The <i>ProcessKeyPhrases</i> class used to test key phrase extraction with command line
 * arguments <br>
 * Usage: ProcessKeyPhrases taskSwitch taskOptions<br>
 * Options: -model taskOption : Build Model <br>
 * Options: -extract taskOption : Extract KeyPhrases"<br>
 * 
 * <b> Just for testing <b>
 * @author Sridhar Somasani
 *
 */
public class ProcessKeyPhrases {

	public static void main(String[] args) {
		ExtractKeyPhrases buildAndExtract = new ExtractKeyPhrases();
		System.out.println(args);
		
		if(args.length > 2){
			String task = args[1];
			String[] options = Arrays.copyOfRange(args, 1, args.length);
			System.out.println(options);
			if(task.equals("-model")){
				buildAndExtract.buildModel(options);
			}else if(task.equals("-extract")){
				buildAndExtract.extractKeyPhrases(options);
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
