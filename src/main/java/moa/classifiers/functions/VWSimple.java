package moa.classifiers.functions;

import moavw.ArffToVW;
import moa.classifiers.AbstractClassifier;
import moa.core.Measurement;
import moa.options.FlagOption;
import moa.options.FloatOption;
import moa.options.IntOption;
import moa.options.MultiChoiceOption;
import vw.VW;
import weka.core.Instance;

public class VWSimple extends AbstractClassifier {
	
	private static final long serialVersionUID = -1574850758865399839L;
	
	private VW m_vw = null;
	private boolean m_hasStarted = false;
	
	private String m_lossFunction = null;
	public MultiChoiceOption m_lossFunctionOption = new MultiChoiceOption("lossFunction", 'L', "Loss function",
			new String[] { "squared", "hinge", "logistic", "quantile" }, 
			new String[] { "squared", "hinge", "logistic", "quantile" }, 0 );
	
	private String m_multiClassMode = null;
	public MultiChoiceOption m_multiClassModeOption = new MultiChoiceOption("multiClassMode", 'M', "Multi-class mode",
			new String[] { "ect", "oaa" }, new String[] { "ect", "oaa" }, 0 );
	
	private double m_l1 = 0.0;
	public FloatOption m_l1Option = new FloatOption("l1", '1', "L1 regularisation", 0, 0, Double.MAX_VALUE);
	
	private double m_l2 = 0.0;
	public FloatOption m_l2Option = new FloatOption("l2", '2', "L2 regularisation", 0, 0, Double.MAX_VALUE);
	
	private boolean m_debug = false;
	public FlagOption m_debugOption = new FlagOption("debug", 'd', "Debug mode");
	
	private boolean m_quadraticFeatures = false;
	public FlagOption m_quadraticFeaturesOption = new FlagOption("quadraticFeatures", 'q', "Compute cross-features?");
	
	private boolean m_cubicFeatures = false;
	public FlagOption m_cubicFeaturesOption = new FlagOption("cubicFeatures", 'c', "Compute cubic features?");
	
	private int m_numBits = 0;
	public IntOption m_numBitsOption = new IntOption("numBits", 'b', "Number of bits for hashing function", 18, 0, 18);
	
	private boolean m_meta = false;
	public FlagOption m_metaOption = new FlagOption("meta", 'm', "Use meta ARFF file?");
	
	private String m_modelDesc = null;
	
	public VW getVw() {
		return m_vw;
	}

	@Override
	public boolean isRandomizable() {
		return false;
	}
	
	public void setQuadraticFeatures(boolean b) {
		m_quadraticFeatures = b;
	}
	
	public boolean getQuadraticFeatures() {
		return m_quadraticFeatures;
	}
	
	public void setCubicFeatures(boolean b) {
		m_cubicFeatures = b;
	}
	
	public boolean getCubicFeatures() {
		return m_cubicFeatures;
	}
	
	public void setLossFunction(String s) {
		m_lossFunction = s;
	}
	
	public String getLossFunction() {
		return m_lossFunction;
	}
	
	public void setMultiClassMode(String s) {
		m_multiClassMode = s;
	}
	
	public String getMultiClassMode() {
		return m_multiClassMode;
	}
	
	public double getL1() {
		return m_l1;
	}
	
	public void setL1(double l1) {
		m_l1 = l1;
	}
	
	public double getL2() {
		return m_l2;
	}
	
	public void setL2(double l2) {
		m_l2 = l2;
	}
	
	public int getNumBits() {
		return m_numBits;
	}
	
	public void setNumBits(int n) {
		m_numBits = n;
	}
	
	public boolean getMeta() {
		return m_meta;
	}
	
	public void setMeta(boolean b) {
		m_meta = b;
	}
	
	public boolean getDebug() {
		return m_debug;
	}
	
	public void setDebug(boolean b) {
		m_debug = b;
	}
	
	/**
	 * Given an instance (which has important meta-data),
	 * instantiate an appropriate VW from the options
	 * provided (e.g. loss function, l1, l2, etc.).
	 * @param inst
	 */
	private void prepareVw(Instance inst) {
		StringBuilder sb = new StringBuilder();
		if( !getDebug() ) {
			sb.append("--quiet" + " ");
		}
		
		sb.append("--bit_precision " + getNumBits() + " ");
		sb.append("--l1 " + getL1() + " ");
		sb.append("--l2 " + getL2() + " ");
		
		if( getQuadraticFeatures() ) {
			sb.append("--quadratic ::" + " ");
		}
		if( getCubicFeatures() ) {
			sb.append("--cubic :::" + " ");
		}
		
		if( inst.numClasses() > 2) {
			// TODO: what about tournament?
			sb.append("--" + getMultiClassMode() + " " + inst.numClasses() + " ");
		} else {
			//sb.append("--binary ");
		}
		
		if( !getLossFunction().equals("logistic") ) {
			sb.append("--loss_function " + getLossFunction() + " ");
		} else {
			// if logistic, then add --link logistic for probabilities between 0 and 1
			sb.append("--loss_function " + getLossFunction() + " --link logistic" + " ");
		}
		
		m_modelDesc = sb.toString();
		System.err.println("VW config: " + m_modelDesc);
		m_vw = new VW(m_modelDesc);
	}
	
	@Override
	public void onClassifierFinished() {
		System.err.println("graceful closing...");
		if(m_vw != null) {
			m_vw.close();
		}
	}

	@Override
	public double[] getVotesForInstance(Instance inst) {
		// TODO:
		// http://stats.stackexchange.com/questions/103857/how-to-get-confidence-on-classification-predictions-with-multi-class-vowpal-wabb
		if( !m_hasStarted ) {
			prepareVw(inst);
			m_hasStarted = true;
		}
		try {
			double pred = 0;
			if( !getMeta() ) {
				pred = m_vw.predict( ArffToVW.process(inst) );
			}
			else {
				//pred = m_vw.predict( inst.attribute(0).value(0) );
				pred = m_vw.predict( inst.toString(0).replace("'", "") );
			}

			if( inst.numClasses() == 2 ) {
				if(m_debug) System.err.print("pred = " + pred + ", ");
				/*
				if(pred < 0) pred = 0;
				else if(pred >= 0) pred = 1;
				*/
				//if(m_debug) System.err.println("transformed pred = " + pred + ", actual = " + inst.classValue());
				
				double[] dist = new double[ inst.numClasses() ];
				dist[1] = pred;
				dist[0] = 1-pred;
				return dist;
				
			} else if( inst.numClasses() > 2 ) {
				if(m_debug) System.err.print("pred = " + pred + ", ");			
				pred = pred - 1;			
				if(m_debug) System.err.println("transformed pred = " + pred + ", actual = " + inst.classValue());
				
				double[] dist = new double[ inst.numClasses() ];
				dist[(int)pred] = 1;
				return dist;
				
			} else {
				// this is a regression
				double[] dist = new double[1];
				dist[0] = pred;
				return dist;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void resetLearningImpl() {
		m_hasStarted = false;
		setLossFunction( m_lossFunctionOption.getChosenLabel() );
		setMultiClassMode( m_multiClassModeOption.getChosenLabel() );
		setL1( m_l1Option.getValue() );
		setL2( m_l2Option.getValue() );
		setQuadraticFeatures( m_quadraticFeaturesOption.isSet() );
		setCubicFeatures( m_cubicFeaturesOption.isSet() );
		setNumBits( m_numBitsOption.getValue() );
		setMeta( m_metaOption.isSet() );
		setDebug( m_debugOption.isSet() );
	}

	@Override
	public void trainOnInstanceImpl(Instance inst) {
		if( !m_hasStarted ) {
			prepareVw(inst);
			m_hasStarted = true;
		}
		try {
			if( !getMeta() ) {
				m_vw.learn( ArffToVW.process(inst) );
			}
			else {
				//System.err.println( inst.toString(0).replace("'", "") );
				//System.err.println("    " + inst.attribute(0).value(0) );
				m_vw.learn( inst.toString(0).replace("'", "") );
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected Measurement[] getModelMeasurementsImpl() {
		return null;
	}

	@Override
	public void getModelDescription(StringBuilder out, int indent) { }
}
