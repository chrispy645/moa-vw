package moa;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Utility class for converting an instance in ARFF format
 * to an instance in the VW format.
 * @author cjb60
 */
public class ArffToVW {
	
	/**
	 * Convert an ARFF instance to a VW instance.
	 * @param x a dense or sparse instance
	 * @return a string returning the instance in VW format
	 * @throws Exception if the dataset has a non-numeric or
	 * non-nominal attribute (e.g. date, string)
	 */
	public static String process(Instance x) throws Exception {
		StringBuilder sb = new StringBuilder();
		if( x.classAttribute().isNominal() ) {
			if( x.numClasses() > 2 ) {
				sb.append( ((int) (x.classValue()+1)) + " | " );
			} else if( x.numClasses() == 2 ) {
				// turn {0,1} into {-1,1}
				if( x.classValue() == 0.0 ) {
					sb.append("-1 | ");
				} else {
					sb.append("1 | ");
				}
			}
		} else {
			sb.append( x.classValue() + " | ");
		}		
		if( x instanceof DenseInstance) {
			for(int i = 0; i < x.numAttributes(); i++) {
				if( i == x.classIndex() ) continue;
				if( x.attribute(i).isNumeric() ) {
					sb.append( x.attribute(i).name() + ":" + x.value(i) + " ");
				} else if (x.attribute(i).isNominal() ) {
					if( (int)x.value(i) != 0 ) {
						String name = "is_" + x.attribute(i).name() + "_" + ((int)x.value(i)) + " ";
						sb.append(name);
					}
				} else {
					throw new Exception("Attribute type not supported: " +
							Attribute.typeToString( x.attribute(i) ));
				}
			}
		} else {
			for(int i = 0; i < x.numValues(); i++) {
				int idx = x.index(i);
				if( idx == x.classIndex() ) continue;
				if( x.attribute(idx).isNumeric() ) {
					sb.append( x.attribute(idx).name() + ":" + x.valueSparse(i) + " " );
				} else if( x.attribute(idx).isNominal() ) {
					String name = "is_" + x.attribute(idx).name() + "_" + ((int)x.valueSparse(i)) + " ";
					sb.append(name);
				} else {
					throw new Exception("Attribute type not supported: " +
							Attribute.typeToString( x.attribute(i) ));
				}
			}
		}
		
		return sb.toString();
	}
	
	private static void writeStringToFile(String body, String outFile) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(outFile) );
		bw.write(body);
		bw.close();
	}
	
	public static void convertArff(String filename) throws Exception {
		
		// for now we assume that if the dataset has nominal
		// attributes then they are binary
		DataSource ds = new DataSource(filename);
		Instances instances = ds.getDataSet();
		instances.setClassIndex( instances.numAttributes() - 1);
		StringBuilder sb = new StringBuilder();
		for(Instance inst : instances) {
			sb.append( process(inst) );
			sb.append("\n");
		}
		writeStringToFile(sb.toString(), filename.replace(".arff", ".txt"));
	}
	
	public static void main(String[] args) throws Exception {
		convertArff("datasets/diabetes_numeric.arff");
		convertArff("datasets/iris.arff");
		convertArff("datasets/iris_2class.arff");
	}

}
