package moa;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Convert an ARFF file to a VW file
 * @author cjb60
 */
public class ArffToVW {
	
	public static String process(Instance x) throws Exception {
		// class label | feature_name:val nominal_feature ...
		StringBuilder sb = new StringBuilder();
		if( x.classAttribute().isNominal() ) {
			sb.append( ((int) (x.classValue()+1)) + " | " );
		} else {
			sb.append( x.classValue() + " | ");
		}
		for(int i = 0; i < x.numAttributes(); i++) {
			if( i == x.classIndex() ) continue;
			if( x.attribute(i).isNumeric() ) {
				sb.append( x.attribute(i).name() + ":" + x.value(i) + " ");
			} else if (x.attribute(i).isNominal() ) {
				int idx = x.attribute(i).index();
				String name = "is_" + x.attribute(i).name() + "_" + idx + " ";
				sb.append(name);
			} else {
				throw new Exception("Attribute type not supported: " +
						Attribute.typeToString( x.attribute(i) ));
			}
		}
		return sb.toString();
	}
	
	private static void writeStringToFile(String body, String outFile) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(outFile) );
		bw.write(body);
		bw.close();
	}
	
	public static void main(String[] args) throws Exception {
		
		// for now we assume that if the dataset has nominal
		// attributes then they are binary
		DataSource ds = new DataSource("datasets/diabetes_numeric.arff");
		Instances instances = ds.getDataSet();
		instances.setClassIndex( instances.numAttributes() - 1);
		StringBuilder sb = new StringBuilder();
		for(Instance inst : instances) {
			sb.append( process(inst) );
			sb.append("\n");
		}
		writeStringToFile(sb.toString(), "datasets/diabetes_numeric.txt");
		
		
	}

}
