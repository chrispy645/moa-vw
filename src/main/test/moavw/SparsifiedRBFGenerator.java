package moavw;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.NonSparseToSparse;
import moavw.ArffToVW;
import moa.streams.generators.RandomRBFGenerator;

public class SparsifiedRBFGenerator {

	public static void main(String[] args) throws Exception {
		
		PrintWriter pwDense = new PrintWriter("/tmp/rbf100k.arff");
		PrintWriter pwSparse = new PrintWriter("/tmp/rbf100k_sparse.arff");
		PrintWriter pwMeta = new PrintWriter("/tmp/rbf100k_meta.arff");	
		
		RandomRBFGenerator rbf = new RandomRBFGenerator();
		rbf.numAttsOption.setValue(1000);
		rbf.prepareForUse();
		pwDense.write( rbf.getHeader().toString() );
		pwSparse.write( rbf.getHeader().toString() );
		pwMeta.write("@relation rbf100k_sparse\n@attribute str string\n");
		pwMeta.write(rbf.getHeader().classAttribute().toString());
		pwMeta.write("\n@data\n");
		
		List<Integer> indices = new ArrayList<Integer>();
		for(int x = 0; x < rbf.getHeader().numAttributes()-1; x++) {
			indices.add(x);
		}
		
		NonSparseToSparse filter = new NonSparseToSparse();
		filter.setInputFormat(rbf.getHeader());
		
		for(int x = 0; x < 100000; x++) {
			
			Collections.shuffle(indices);
			
			// write to dense file
			Instance inst = rbf.nextInstance();
			
			// zero first 25% attrs
			for(int i = 0; i < inst.numAttributes()*0.5; i++) {
				inst.setValue(indices.get(i), 0);
			}
			
			pwDense.write(inst.toString());
			pwDense.write("\n");
			
			// write to sparse file
			Instances tmp = new Instances(inst.dataset(), 1);
			tmp.add(inst);
			Instances result = Filter.useFilter(tmp, filter);
			pwSparse.write( result.get(0).toString() );
			pwSparse.write("\n");
			
			pwMeta.write( "\"" + ArffToVW.process(inst) + "\"" + "," + inst.toString(inst.classIndex()) );
			pwMeta.write("\n");
		}
		
		pwDense.flush();
		pwSparse.flush();
		pwMeta.flush();
		
		pwDense.close();
		pwSparse.close();
		pwMeta.close();
		
	}
	
}
