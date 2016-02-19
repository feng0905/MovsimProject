package Movsim_SubState;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.JAXBException;

import movSimSMC.MovSimState;
import movSimSMC.MovSimState.MovSimMeasurement;
import movsimSMC.MovSimSensor;
import movsimSMC.MovsimArea;
import movsimSMC.MovsimWrap;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.xml.sax.SAXException;

import smc.AbstractState;

public class MovSimSubState extends MovSimState{
	private int subIndex = -1;		// start from 0 
	
	public MovSimSubState(double stepLength, int subindex) throws JAXBException, SAXException {
		super(stepLength);
		// TODO Auto-generated constructor stub
		subIndex = subindex;
	}
	
	public MovSimSubState(MovSimState fullState, int subindex) {
		super(fullState.getMovSimWrap());
		// TODO Auto-generated constructor stub
		this.subIndex = subindex;
		stateArea = new MovsimArea(subindex+1, 0, 250);
	}
	
	@Override
	public MovSimSubState clone() {
		// TODO Auto-generated method stub
		MovSimSubState sub = (MovSimSubState)super.clone();
		sub.subIndex = this.subIndex;
		return sub;
		
	}

	public MovSimSubState(MovsimWrap movsimPF) {
		super(movsimPF);
		// TODO Auto-generated constructor stub
		subIndex = -1;
	}
	
	public MovSimSubState(MovSimSubState movsimSubState, int subindex) {
		super(movsimSubState.movsimPF);
		// TODO Auto-generated constructor stub
		subIndex = subindex;
	}

	/* (non-Javadoc)
	 * @see movSimSMC.MovSimState#measurementPdf(smc.AbstractState.AbstractMeasurement)
	 */
	//@Override
	public BigDecimal measurementPdf(AbstractMeasurement measurement)
			throws StateFunctionNotSupportedException {
		// TODO Auto-generated method stub
		if(subIndex == -1) 
			return super.measurementPdf(measurement);
		
		List<MovSimSensor> sensorReadings = ((MovSimMeasurement)measurement).getSensorReading();
		
		List<MovSimSensor> simulatedSensorReadings = this.movsimPF.getSensorReading();
		double sigma = 0.12;
		/*
		 * double variance = sigma*sigma;
		 * 
		 * double[][] cov = new double[sensorProfiles.length][sensorProfiles.length]; for(int i=0; i<sensorProfiles.length; i++) for(int j=0; j<sensorProfiles.length; j++) { if(i==j) cov[i][j] =
		 * variance; else cov[i][j] =0; }
		 * 
		 * MultivariateNormalDistribution mn = new MultivariateNormalDistribution(simTrueReadings, cov); BigDecimal weight = BigDecimal.valueOf(mn.density(sensorReadings));
		 */

		NormalDistribution norm = new NormalDistribution(0, sigma);
		BigDecimal weight = BigDecimal.ONE;

		double normDis = singleSensorNormlizedDistance(sensorReadings.get(subIndex), simulatedSensorReadings.get(subIndex));
		max = max > normDis?max:normDis;
		double normResult = norm.density(normDis);
		double minNorm = 1E-300; // if not doing so, a small value will become 0, and mess up the weight
		if (normResult < minNorm) normResult = minNorm;
		
		// System.out.println("sensor-" + i + " norm dis=" + normDis + "-> L=" + normResult);

		weight = weight.multiply(BigDecimal.valueOf(normResult));
		
		// System.out.println("Max Difference:" + max);
		return weight;
		
		
		// return super.measurementPdf(measurement);
	}

	/* (non-Javadoc)
	 * @see movSimSMC.MovSimState#transitionModel(smc.AbstractState.AbstractTransitionRandomComponent)
	 */
	@Override
	public AbstractState transitionModel(
			AbstractTransitionRandomComponent random)
			throws StateFunctionNotSupportedException {
		// TODO Auto-generated method stub
		return super.transitionModel(random);
	}
	
	
	/**
	 * The default createMovsimArea method will create an complete area when subIndex is -1;
	 * And when the subIndex is no less than 0, it will create an area of the entire road segment whose id is subIndex+1 
	 */
	protected void createMovsimArea() {
		if (subIndex == -1) {
			super.createMovsimArea();
		}
		else {
			int startid, endid;		
			startid = subIndex+1;
			endid = subIndex+1;
			
			stateArea = new MovsimArea(startid,endid,0,movsimPF.getRoadNetwork().getRoadLength(startid));			
		}
	}
	
	/**
	 * using the area parameters to create the state area. 
	 * @param startId
	 * @param endId
	 * @param startpos
	 * @param endpos
	 */
	public void createMovsimArea(int startId,int endId, double startpos, double endpos) {
		stateArea = new MovsimArea(startId,endId,startpos,endpos);
	}
	
}
