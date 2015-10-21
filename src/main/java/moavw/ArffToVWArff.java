package moavw;

import moavw.ArffToVW;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Class to convert an ARFF file into a "meta ARFF", which is simply
 * an ARFF file with a string attribute (a VW instance, represented
 * as a string), and the class attribute from the original ARFF.
 * @author cjb60
 */
public class ArffToVWArff {
	
	public static void main(String[] args) throws Exception {
		
		DataSource ds = new DataSource(args[0]);
		Instances data = ds.getDataSet();
		data.setClassIndex( data.numAttributes() - 1 );
		System.out.println("@relation " + data.relationName() + "_ArffToVWArff");
		System.out.println("@attribute str string");
		System.out.println(data.classAttribute().toString());
		System.out.println("@data");
		for(int x = 0; x < data.numInstances(); x++) {
			System.out.println( "\"" + ArffToVW.process(data.get(x)) + "\"" + "," + data.get(x).toString(data.classIndex()));
		}
		
	}

}
