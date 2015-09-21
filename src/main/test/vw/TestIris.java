package vw;

import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import moa.classifiers.functions.VWSimple;
import static org.junit.Assert.*;

public class TestIris {
	
	@Test
	public void iris() throws Exception {
		DataSource ds = new DataSource("datasets/iris_shuf_0.arff");
		Instances data = ds.getDataSet();
		data.setClassIndex( data.numAttributes() - 1 );
		data.randomize( new Random(0) );		
		VWSimple vw = new VWSimple();
		vw.resetLearning();
		vw.setDebug(false);
		for(int i = 0; i < data.numInstances(); i++) {
			vw.trainOnInstance( data.get(i) );
		}
		double numCorrect = 0;
		for(int i = 0; i < data.numInstances(); i++) {
			double[] preds = vw.getVotesForInstance( data.get(i) );
			double pred = Utils.maxIndex(preds);
			double actual = data.get(i).classValue();
			if( pred == actual ) {
				numCorrect += 1;
			}
		}
		numCorrect = numCorrect / data.numInstances();
		//System.out.println("num correct = " + (numCorrect / data.numInstances()) );
		assertEquals(numCorrect > 0.95, true);
		vw.close();
	}
	
	/**
	 * Test the two-class Iris dataset (binary classification). After
	 * one pass through the dataset, it should be able to predict all
	 * instances with 100% accuracy.
	 * @throws Exception
	 */
	@Test
	public void irisBinary() throws Exception {
		DataSource ds = new DataSource("datasets/iris_2class_shuf_0.arff");
		Instances data = ds.getDataSet();
		data.setClassIndex( data.numAttributes() - 1 );
		//data.randomize( new Random(0) );	
		VWSimple vw = new VWSimple();
		vw.resetLearning();	
		vw.setLossFunction("logistic");
		vw.setDebug(false);
		for(int i = 0; i < data.numInstances(); i++) {
			vw.trainOnInstance( data.get(i) );
		}		
		double numCorrect = 0;
		for(int i = 0; i < data.numInstances(); i++) {
			double[] preds = vw.getVotesForInstance( data.get(i) );
			double pred = Utils.maxIndex(preds);
			double actual = data.get(i).classValue();
			if( pred == actual ) {
				numCorrect += 1;
			}
		}
		numCorrect = numCorrect / ((double)data.numInstances());
		assertEquals(1.0, numCorrect, 1e-5);	
		vw.close();
	}

}
