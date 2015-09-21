package moa.classifiers.functions;

import moa.ArffToVW;
import moa.classifiers.AbstractClassifier;
import moa.core.Measurement;
import moa.options.FlagOption;
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
	
	private boolean m_debug = false;
	public FlagOption m_debugOption = new FlagOption("debugOption", 'd', "Debug mode");
	
	private String m_modelDesc = null;
	
	public VW getVw() {
		return m_vw;
	}

	@Override
	public boolean isRandomizable() {
		return false;
	}
	
	public void setLossFunction(String s) {
		m_lossFunction = s;
	}
	
	public String getLossFunction() {
		return m_lossFunction;
	}
	
	public boolean getDebug() {
		return m_debug;
	}
	
	public void setDebug(boolean b) {
		m_debug = b;
	}
	
	/**
	 * Given an instance (which has important meta-data),
	 * instantiate an appropriate VW.
	 * @param inst
	 */
	private void prepareVw(Instance inst) {
		StringBuilder sb = new StringBuilder();
		if( inst.numClasses() > 2) {
			sb.append("--ect " + inst.numClasses() + " ");
		} else {
			//sb.append("--binary ");
		}
		sb.append("--loss_function " + getLossFunction() + " ");
		m_modelDesc = sb.toString();
		m_vw = new VW(m_modelDesc);
	}
	
	public void close() {
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
			double pred = m_vw.predict( ArffToVW.process(inst) );
			if(m_debug) System.err.println( ArffToVW.process(inst) );	
			/*
			 * This gets a bit confusing. In the two-class case, VW will return
			 * predictions in [-1,1], so they must be thresholded to {0,1}. In
			 * the multi-class case, VW returns class indices but they aren't
			 * zero-indexed, so we have to subtract 1.
			 */
			if( inst.numClasses() == 2 ) {
				if(m_debug) System.err.print("pred = " + pred + ", ");
				if(pred < 0) pred = 0;
				else if(pred >= 0) pred = 1;	
				if(m_debug) System.err.println("transformed pred = " + pred + ", actual = " + inst.classValue());
			} else if( inst.numClasses() > 2 ) {
				if(m_debug) System.err.print("pred = " + pred + ", ");			
				pred = pred - 1;			
				if(m_debug) System.err.println("transformed pred = " + pred + ", actual = " + inst.classValue());
			} else {
				throw new Exception("Number of classes < 2!");
			}
			
			double[] dist = new double[ inst.numClasses() ];
			dist[(int)pred] = 1;
			return dist;
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void resetLearningImpl() {
		m_hasStarted = false;
		setLossFunction( m_lossFunctionOption.getChosenLabel() );
		setDebug( m_debugOption.isSet() );
	}

	@Override
	public void trainOnInstanceImpl(Instance inst) {
		if( !m_hasStarted ) {
			prepareVw(inst);
			m_hasStarted = true;
		}
		try {
			m_vw.learn( ArffToVW.process(inst) );
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected Measurement[] getModelMeasurementsImpl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getModelDescription(StringBuilder out, int indent) {
		// TODO Auto-generated method stub
	}
}
