package vw;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Enumeration;

import org.junit.Test;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import moa.ArffToVW;
import moa.classifiers.functions.VWSimple;
import moa.streams.ArffFileStream;
import moa.streams.MetaArffFileStream;

public class TestMetaArff {
	
	/**
	 * Test that the iris "meta ARFF" instances are the same as the
	 * converted instances in the original ARFF.
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		
		VWSimple vw = new VWSimple();
		vw.prepareForUse();
		vw.setMeta(true);
		
		MetaArffFileStream stream = new MetaArffFileStream("datasets/iris.meta.arff", -1);
		ArffFileStream stream2 = new ArffFileStream("datasets/iris.arff", -1);
		
		//stream.prepareForUse();
		while( stream.hasMoreInstances() ) {
			Instance metaInst = stream.nextInstance();
			Instance normInst = stream2.nextInstance();
			
			assertEquals( metaInst.stringValue(0), ArffToVW.process(normInst) );
			vw.trainOnInstance(metaInst);
		}
		
	}

}
