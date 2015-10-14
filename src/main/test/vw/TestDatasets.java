package vw;

import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import moa.classifiers.Classifier;
import moa.classifiers.functions.VWSimple;
import static org.junit.Assert.*;

public class TestDatasets {
	
	public double getPercentageCorrect(Instances data, Classifier vw) {
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
		return numCorrect;
	}
	
	@Test
	public void testDiabetesNumeric() throws Exception {
		System.out.println("testDiabetesNumeric()");
		DataSource ds = new DataSource("datasets/diabetes_numeric.arff");
		Instances data = ds.getDataSet();
		data.setClassIndex( data.numAttributes() - 1 );
		VWSimple vw = new VWSimple();
		vw.resetLearning();
		vw.setDebug(true);
		for(int i = 0; i < data.numInstances(); i++) {
			vw.trainOnInstance( data.get(i) );
		}
		//double numCorrect = getPercentageCorrect(data, vw);
		//System.out.println("% correct on training data:" + numCorrect);
		vw.onClassifierFinished();
		
	}
	
	/**
	 * Test the Iris dataset (multi-class classification). This
	 * dataset has been pre-shuffled. Expecting a training set
	 * accuracy of 95% or more.
	 * @throws Exception
	 */
	@Test
	public void iris() throws Exception {
		System.out.println("iris()");
		DataSource ds = new DataSource("datasets/iris_shuf_0.arff");
		Instances data = ds.getDataSet();
		data.setClassIndex( data.numAttributes() - 1 );
		data.randomize( new Random(0) );		
		VWSimple vw = new VWSimple();
		vw.resetLearning();
		//vw.setDebug(true);
		for(int i = 0; i < data.numInstances(); i++) {
			vw.trainOnInstance( data.get(i) );
		}
		double numCorrect = getPercentageCorrect(data, vw);
		System.out.println("Percent correct = " + numCorrect*100);
		assertEquals(numCorrect > 0.95, true);
		vw.onClassifierFinished();
	}
	
	/**
	 * Test the two-class Iris dataset (binary classification). This
	 * dataset has been pre-shuffled. After one pass through this dataset,
	 * it should be able to predict all instances with 100% accuracy.
	 * @throws Exception
	 */
	@Test
	public void irisBinary() throws Exception {
		System.out.println("irisBinary()");
		DataSource ds = new DataSource("datasets/iris_2class_shuf_0.arff");
		Instances data = ds.getDataSet();
		data.setClassIndex( data.numAttributes() - 1 );	
		VWSimple vw = new VWSimple();
		vw.resetLearning();	
		vw.setLossFunction("logistic");
		//vw.setDebug(true);
		for(int i = 0; i < data.numInstances(); i++) {
			vw.trainOnInstance( data.get(i) );
		}		
		double numCorrect = getPercentageCorrect(data, vw);
		System.out.println("Percent correct = " + numCorrect*100);
		assertEquals(1.0, numCorrect, 1e-5);	
		vw.onClassifierFinished();
	}

}
