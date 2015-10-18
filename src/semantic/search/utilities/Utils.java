package semantic.search.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Application utility functions
 * 
 * @author Sridhar Somasani
 *
 */
public class Utils {
	
	/**
	 * Sort map based on their values
	 * @param map of String double values
	 * @return list of sorted Map.Entry based on their value
	 */
	public static List<Map.Entry<String, Double>> sortMapByValue(Map<String, Double> map){
		
		List<Entry<String, Double>> list_ranked = new ArrayList<Entry<String, Double>>(map.entrySet());
		Collections.sort( list_ranked, new Comparator<Map.Entry<String, Double>>(){
			@Override
			public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 ){
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        });
		
		return list_ranked;
	}

}
