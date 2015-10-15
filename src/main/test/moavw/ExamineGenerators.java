package moavw;

import weka.core.Attribute;
import moa.core.InstancesHeader;
import moa.options.AbstractOptionHandler;
import moa.streams.InstanceStream;
import moa.streams.generators.AgrawalGenerator;
import moa.streams.generators.HyperplaneGenerator;
import moa.streams.generators.LEDGenerator;
import moa.streams.generators.RandomRBFGenerator;
import moa.streams.generators.RandomTreeGenerator;
import moa.streams.generators.SEAGenerator;
import moa.streams.generators.STAGGERGenerator;
import moa.streams.generators.WaveformGenerator;

/**
 * Class to output summary information about datasets
 * for the tech report. This is technically not a unit
 * testing class.
 * @author cjb60
 */
public class ExamineGenerators {
	
	public static void summariseGenerator(InstanceStream gen) throws Exception {
		((AbstractOptionHandler)gen).prepareForUse();		
		InstancesHeader hdr = gen.getHeader();
		System.out.println(hdr.relationName());
		int numNumeric = 0;
		int numNominal = 0;
		int numClasses = hdr.numClasses();
		for(int x = 0; x < hdr.numAttributes(); x++) {
			String type = Attribute.typeToString(hdr.attribute(x));
			if(type.equals("numeric")) {
				numNumeric += 1;
			} else if(type.equals("nominal")) {
				numNominal += 1;
			} else {
				throw new Exception("Unknown attr type: " + type);
			}
		}
		System.out.println("  Numeric: " + numNumeric);
		System.out.println("  Nominal: " + numNominal);
		System.out.println("  Num classes: " + numClasses);
	}
	
	public static void main(String[] args) throws Exception {
		
		InstanceStream[] streams = new InstanceStream[] {
				new AgrawalGenerator(),
				new HyperplaneGenerator(),
				new LEDGenerator(),
				new RandomRBFGenerator(),
				new RandomTreeGenerator(),
				new SEAGenerator(),
				new STAGGERGenerator(),
				new WaveformGenerator()
		};
		
		for(InstanceStream st : streams) {
			summariseGenerator(st);
		}
	}

}
