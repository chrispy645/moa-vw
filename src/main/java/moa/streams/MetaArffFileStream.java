package moa.streams;

import java.io.IOException;

import weka.core.Instance;
import weka.core.Instances;

/**
 * Meta ARFF file stream, i.e. ARFF files with just two
 * attributes: a string attribute, and a class. The string
 * attribute is some encoding of the feature vector.
 * ArffFileStream has an issue with instances with strings
 * so this class is a "hacky fix" for this issue.
 * @author cjb60
 */
public class MetaArffFileStream extends ArffFileStream {

	private static final long serialVersionUID = 2190789007689325368L;
	

	
	public MetaArffFileStream(String arffFileName, int classIndex) {
		super(arffFileName, classIndex);
	}
	
	public MetaArffFileStream() {
		super();
	}
	
    @Override
    public Instance nextInstance() {
    	
        Instance prevInstance = this.lastInstanceRead;
        
    	Instances tmp = new Instances(prevInstance.dataset(), 1).stringFreeStructure();
    	tmp.attribute(0).addStringValue(prevInstance.attribute(0).value(0));
    	tmp.add(prevInstance);
        
        this.hitEndOfFile = !readNextInstanceFromFile();
        return tmp.get(0);
    }
    
}
