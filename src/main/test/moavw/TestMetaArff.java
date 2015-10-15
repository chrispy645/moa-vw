package moavw;

import static org.junit.Assert.assertEquals;

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
		
		VWSimple vw = new VWSimple();
		vw.prepareForUse();
		vw.setMeta(true);
		
		MetaArffFileStream stream = new MetaArffFileStream("datasets/iris.meta.arff", -1);
		ArffFileStream stream2 = new ArffFileStream("datasets/iris.arff", -1);
		
		stream.prepareForUse();
		stream2.prepareForUse();
		int c = 1;
		while( stream2.hasMoreInstances() ) {
			Instance metaInst = stream.nextInstance();
			Instance normInst = stream2.nextInstance();
			System.out.println(metaInst);
			c++;
			System.out.println(ArffToVW.process(normInst));
			
			assertEquals( metaInst.stringValue(0), ArffToVW.process(normInst) );
			//vw.trainOnInstance(metaInst);
		}
		
	}

}
