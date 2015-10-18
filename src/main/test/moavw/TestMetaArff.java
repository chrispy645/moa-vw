package moavw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import weka.core.Instance;
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
		
		VWSimple vwMeta = new VWSimple();
		vwMeta.prepareForUse();
		//vwMeta.setLossFunction("logistic");
		vwMeta.setMeta(true);
		
		VWSimple vwDense = new VWSimple();
		vwDense.prepareForUse();
		//vwDense.setLossFunction("logistic");
		vwDense.setMeta(false);
		
		MetaArffFileStream metaStream = new MetaArffFileStream("datasets/iris.meta.arff", -1);
		ArffFileStream denseStream = new ArffFileStream("datasets/iris.arff", -1);
		
		//MetaArffFileStream metaStream = new MetaArffFileStream("/tmp/rbf100k_meta.arff", -1);
		//ArffFileStream denseStream = new ArffFileStream("/tmp/rbf100k.arff", -1);
		
		//metaStream.prepareForUse();
		//denseStream.prepareForUse();
		int c = 0;
		while( metaStream.hasMoreInstances() ) {
			c++;
			//System.out.println("iter " + c);
			
			Instance metaInst = metaStream.nextInstance();
			Instance denseInst = denseStream.nextInstance();
			//System.out.println("metaInst: " + metaInst.stringValue(0).replace("'", ""));
			//System.out.println("densInst: " + ArffToVW.process(denseInst));
			
			assertEquals( metaInst.stringValue(0).replace("'", ""), ArffToVW.process(denseInst) );
			//assertEquals( metaInst.toString(), ArffToVW.process(normInst) );			
			
			double[] densePred = vwDense.getVotesForInstance(denseInst);
			double[] metaPred = vwMeta.getVotesForInstance(metaInst);			
			assertTrue( Arrays.equals(densePred, metaPred) );
			
			vwDense.trainOnInstance(denseInst);
			vwMeta.trainOnInstance(metaInst);
		
		}
		
	}

}
