package Movsim_SubState;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.JAXBException;

import movSimSMC.MovSimState;
import movSimSMC.MovSimState.MovSimMeasurement;
import movsimSMC.MovsimWrap;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.movsim.simulator.roadnetwork.MovSimSensor;
import org.xml.sax.SAXException;

public class MovSimSubState extends MovSimState{
	private final int subIndex;
	
	public MovSimSubState(double stepLength, int subindex) throws JAXBException, SAXException {
		super(stepLength);
		// TODO Auto-generated constructor stub
		subIndex = subindex;
	}
	
	public MovSimSubState(MovSimState fullState, int subindex) {
		super(fullState.getMovSimWrap());
		// TODO Auto-generated constructor stub
		subIndex = subindex;
	}
	
	@Override
	public MovSimSubState clone() {
		// TODO Auto-generated method stub
		// MovSimSubState sub = (MovSimSubState)super.clone();
		// subindex = this.subIndx;
		return (MovSimSubState)super.clone();
	}

	public MovSimSubState(MovsimWrap movsimPF, int subindex) {
		super(movsimPF);
		// TODO Auto-generated constructor stub
		subIndex = subindex;
	}

	/* (non-Javadoc)
	 * @see movSimSMC.MovSimState#measurementPdf(smc.AbstractState.AbstractMeasurement)
	 */
	@Override
	public BigDecimal measurementPdf(AbstractMeasurement measurement)
			throws StateFunctionNotSupportedException {
		// TODO Auto-generated method stub
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
	
	
}
