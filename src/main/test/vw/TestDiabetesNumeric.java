package vw;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import moa.ArffToVW;

public class TestDiabetesNumeric {
	
	public static void main(String[] args) throws Exception {
		
		DataSource ds = new DataSource("datasets/diabetes_numeric.arff");
		Instances data = ds.getDataSet();
		data.setClassIndex( data.numAttributes() - 1 );
		
		VW learner = new VW("");
		
		for(int i = 0; i < data.numInstances(); i++) {
			String inst = ArffToVW.process( data.get(i) );
			System.out.println( learner.learn(inst) );
		}
		
		System.out.println("-----");
		
		System.out.println( learner.predict( ArffToVW.process(data.get(0)) ) );
		
		learner.close();
		
	}

}
