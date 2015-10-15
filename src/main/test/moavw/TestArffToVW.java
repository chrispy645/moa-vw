package moavw;

import static org.junit.Assert.assertEquals;
import moavw.ArffToVW;

import org.junit.Test;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class TestArffToVW {
	
	public static final String[] answers = new String[] {
		"-1 | x1:100.0 is_x3_1 ",
		"1 | x1:200.0 is_x2_1 is_x3_2 ",
		"-1 | x1:1.0 ",
	};
	
	/**
	 * Test to see if ArffToVW can correctly convert a (very simple)
	 * dataset with both nominal and numeric attributes.
	 * @throws Exception
	 */
	@Test
	public void testNominal() throws Exception {
		System.out.println("testNominal()");
		DataSource ds = new DataSource("datasets/nominals.arff");
		Instances data = ds.getDataSet();
		data.setClassIndex( data.numAttributes() - 1);
		for(int x = 0; x < data.numInstances(); x++) {
			String inst = ArffToVW.process( data.get(x) );
			System.out.println(inst);
			assertEquals( inst, answers[x] );
		}
	}
	
	@Test
	public void testNominalSparse() throws Exception {
		System.out.println("testNominalSparse()");
		DataSource ds = new DataSource("datasets/nominals-sparse.arff");
		Instances data = ds.getDataSet();
		data.setClassIndex( data.numAttributes() - 1 );
		for(int x = 0; x < data.numInstances(); x++) {
			String inst = ArffToVW.process( data.get(x) );
			System.out.println(inst);
		}
	}

}
