package moa.streams;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import moa.core.InputStreamProgressMonitor;
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

	private boolean m_firstTime = true;
	
	public MetaArffFileStream(String arffFileName, int classIndex) {
		super(arffFileName, classIndex);
	}
	
	public MetaArffFileStream() {
		super();
	}
	
	@Override
    public boolean hasMoreInstances() {
		try {
	        if (this.instances.readInstance(this.fileReader)) {
	            this.lastInstanceRead = this.instances.instance(0);
	            this.instances.delete(); // keep instances clean
	            this.numInstancesRead++;
	            return true;
	        }
	        if (this.fileReader != null) {
	            this.fileReader.close();
	            this.fileReader = null;
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
        return false;
    }
	
	@Override
    public Instance nextInstance() {
        return this.lastInstanceRead;
    }
	
    @Override
    public void restart() {
        try {        		
            if (this.fileReader != null) {
                this.fileReader.close();
            }
            InputStream fileStream = new FileInputStream(this.arffFileOption.getFile());
            this.fileProgressMonitor = new InputStreamProgressMonitor(
                    fileStream);
            this.fileReader = new BufferedReader(new InputStreamReader(
                    this.fileProgressMonitor));
            this.instances = new Instances(this.fileReader, 1);
            if (this.classIndexOption.getValue() < 0) {
                this.instances.setClassIndex(this.instances.numAttributes() - 1);
            } else if (this.classIndexOption.getValue() > 0) {
                this.instances.setClassIndex(this.classIndexOption.getValue() - 1);
            }
            this.numInstancesRead = 0;
            this.lastInstanceRead = null;
            //this.hitEndOfFile = !readNextInstanceFromFile();     	
        } catch (IOException ioe) {
            throw new RuntimeException("ArffFileStream restart failed.", ioe);
        }
    }
}
