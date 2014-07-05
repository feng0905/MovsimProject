package movSimSMC;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBException;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.movsim.simulator.roadnetwork.MovSimSensor;
import org.xml.sax.SAXException;

import movsimSMC.MovsimWrap;
import smc.AbstractState;



public class MovSimState extends AbstractState 
{
	private MovsimWrap movsimPF; 
	private double stepLength = 10;			// seconds
	
	// clone a state
	public MovSimState clone(){
		
		MovSimState c = null;
		try
		{
			c = (MovSimState)super.clone();
			c.movsimPF = this.movsimPF.duplicate();
		}
		catch (CloneNotSupportedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JAXBException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return c;
	}
	
	public MovsimWrap getMovSimWrap(){
		return movsimPF;
	} 
	
	public MovSimState( double stepLength ) throws JAXBException, SAXException {
		
		this.stepLength = stepLength;
		String baseDir = System.getProperty("user.dir");
		String[] args = { "-f", baseDir + "\\sim\\buildingBlocks\\startStop.xprj" };
  		movsimPF = new MovsimWrap(args);
	}
	
	MovSimState(MovsimWrap movsimPF){
		this.movsimPF = movsimPF;
	}
	
	public void createObstacle(double startTime, int roadId, int laneId) {
		movsimPF.createObstacle(startTime, roadId, laneId);
	}
	@Override
	public void setDescription(String des)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractState transitionFunction() throws StateFunctionNotSupportedException
	{
		MovsimWrap nextState = null;
		try {
			nextState = movsimPF.duplicate();
		} catch (JAXBException | SAXException e) {
			e.printStackTrace();
		}
//	    if (nextState == null) {
//	    	return null;
//		}
		
	    nextState.runFor(stepLength);
	    //System.out.println("transition finished");
    	return new MovSimState(nextState);
	}

	@Override
	public AbstractState transitionModel(AbstractTransitionRandomComponent random) throws StateFunctionNotSupportedException
	{
		// the random
		MovSimRandomComponent randomMovSim = (MovSimRandomComponent) random;
		// clone the current state
		MovSimState clonedState = this.clone();
		
		// set random
	    if(GlobalConstants.TRANSITION_MOVE_RANDOMNESS)
	    	clonedState.movsimPF.addRandomComponent(randomMovSim.getRandom());
	    
	    if (GlobalConstants.G_RAND.nextDouble() <= GlobalConstants.TRANSITION_ACCIDENT_RATE) {
			//place a random obstacle
	    	clonedState.movsimPF.placeRandomObstacle(GlobalConstants.G_RAND);
		}
	    
		return clonedState.transitionFunction();
		
		//return this.transitionFunction();
	}

	static class MovSimMeasurement extends AbstractMeasurement{
		List<MovSimSensor> sensors;
		public MovSimMeasurement( List<MovSimSensor> sensors ) { this.sensors = sensors; };
	}
	@Override
	public AbstractMeasurement measurementFunction() throws StateFunctionNotSupportedException
	{
		return new MovSimMeasurement(this.movsimPF.getSensorReading());
	}

	@Override
	public BigDecimal measurementPdf(AbstractMeasurement measurement) throws StateFunctionNotSupportedException
	{
		List<MovSimSensor> sensorReadings = ((MovSimMeasurement)measurement).sensors;
		List<MovSimSensor> simulatedSensorReadings = this.movsimPF.getSensorReading();
		//double sigma = sensorReadings.get(0).getMaxValue() / 4.0; 
		double sigma = 130;
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
		for (int i = 0; i < sensorReadings.size(); i++)
		{
			double normResult = norm.density(sensorReadings.get(i).distance(simulatedSensorReadings.get(i)));
			double minNorm = 1E-300; // if not doing so, a small value will become 0, and mess up the weight
			if (normResult < minNorm) normResult = minNorm;
			
			//System.out.println("sensor-" + i + "!!!!!!! " + sensorReadings.get(i).getAvgSpeed() + " -- " + simulatedSensorReadings.get(i).getAvgSpeed() + " -- dis" +  sensorReadings.get(i).distance(simulatedSensorReadings.get(i)) + " -> " + normResult);

			weight = weight.multiply(BigDecimal.valueOf(normResult));
		}

		return weight;
		//return BigDecimal.ONE;
	}
	
	static class MovSimSensorReadings extends AbstractMeasurement{
		
	}
	
	@Override
	public BigDecimal proposalPdf(AbstractMeasurement measurement) throws StateFunctionNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractState propose(AbstractMeasurement measurement) throws StateFunctionNotSupportedException, Exception
	{
		MovSimState nextMovSimState = (MovSimState) this.transitionModel(drawNextRandomComponentSample());
		MovSimMeasurement movSimMeasurement =  (MovSimMeasurement) measurement;
		nextMovSimState.movsimPF.setStates(movSimMeasurement.sensors, 5);
		
		// return nextMovSimState;
		return this.transitionFunction();
	}

	@Override
	public AbstractTransitionRandomComponent drawNextRandomComponentSample()
	{
		
		//MovSimRandomComponent randomComponent = new MovSimRandomComponent(( random.nextDouble()* (0.1-0)));
		MovSimRandomComponent randomComponent = new MovSimRandomComponent(GlobalConstants.G_RAND.nextDouble()*((0.1-0)));
		return randomComponent;
	}

	@Override
	public long distance(AbstractState sample)
	{
		MovSimState samplePF = (MovSimState) sample;
		double dis = this.movsimPF.CalDistance(samplePF.movsimPF);
		//System.out.println("State Distance: " + dis + " " + ((long) (dis*100000000)));
		return (long) (dis*100000000);
	}

	// not-supported functions
	@Override
	public BigDecimal transitionPdf(AbstractState nextState) throws StateFunctionNotSupportedException
	{
		throw new StateFunctionNotSupportedException();
	}
	
	@Override
	public AbstractMeasurement measurementModel(AbstractMeasurementRandomComponent random) throws StateFunctionNotSupportedException
	{
		throw new StateFunctionNotSupportedException();
	}
	
	@Override
	public AbstractState generateNoisedState() throws StateFunctionNotSupportedException
	{
		throw new StateFunctionNotSupportedException();
	}
	// end of not-supported functions
	
	
	/**
	 * @return the simStep
	 */
	public double getSimStep() {
		return stepLength;
	}


	/**
	 * @param simStep the simStep to set
	 */
	public void setSimStep(double simStep) {
		this.stepLength = simStep;
	}
}

