package movSimSMC;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import javax.xml.bind.JAXBException;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.movsim.simulator.roadnetwork.MovSimSensor;
import org.movsim.simulator.roadnetwork.MovSimSensor2;
import org.xml.sax.SAXException;

import movsimSMC.MovsimWrap;
import movsimSMC.Paint.SmcSimulationCanvas;
import smc.AbstractState;
 


public class MovSimState extends AbstractState 
{
	protected MovsimWrap movsimPF; 
	protected double stepLength = 10;			// seconds
	protected List<MovsimArea> areaList = new ArrayList<>();
	protected double max = 0;
	// clone a state
	public MovSimState clone(){
		
		MovSimState c = null;
		try
		{
			c = (MovSimState)super.clone();
			c.movsimPF = this.movsimPF.duplicate();
			
			if (IsInitalState) {
				c = (MovSimState)super.clone();
				c.movsimPF = c.movsimPF.redistributeClone(GlobalConstants.G_RAND);
				c.areaList = (List<MovsimArea>) ((ArrayList<MovsimArea>) (c.areaList)).clone();
			}
			else {
				c = (MovSimState)super.clone();
				c.movsimPF = this.movsimPF.duplicate();			
				c.areaList = (List<MovsimArea>) ((ArrayList<MovsimArea>) (c.areaList)).clone();
			}
//			IsInitalState = false;
			
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
		String[] args = { "-f", baseDir + "/sim/buildingBlocks/ringroad_2lanes.xprj" };
  		movsimPF = new MovsimWrap(args);
  		createArea(-1,0,0);
	}
	
	public void  createArea(int road, double start, double end) {
		areaList.add(new MovsimArea(road,start,end));
		// System.out.println("Area created!");
	}
	
	
	public MovSimState(MovsimWrap movsimPF){
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
		//System.out.println("============================= Calling transition FUNCTION!");
		MovSimState nextState = this.clone();
	    nextState.movsimPF.runFor(stepLength);
	    
    	return nextState;
		
		//return this.transitionModel(this.drawNextRandomComponentSample());
	}

	@Override
	public AbstractState transitionModel(AbstractTransitionRandomComponent random) throws StateFunctionNotSupportedException
	{
		//System.out.println("============================= Calling transition MODEL!");
		
		// the random
		MovSimRandomComponent randomMovSim = (MovSimRandomComponent) random;
		// clone the current state
		MovSimState clonedState = this.clone();
		
//		// set random
//	    if(GlobalConstants.TRANSITION_BEHAVIOR_RANDOM)
//	    {
//	    	//System.out.println("Behavior model randomness added");
//	    	clonedState.movsimPF = clonedState.movsimPF.redistributeClone(GlobalConstants.G_RAND);
//	    	//System.out.println("---------------the random: " + randomMovSim.getRandom());
//	    }
		
		
		// set random
	    if(GlobalConstants.TRANSITION_BEHAVIOR_RANDOM)
	    {
	    	//System.out.println("Behavior model randomness added");
	    	clonedState.movsimPF.addRandomComponent(GlobalConstants.G_RAND.nextDouble());
	    	//System.out.println("---------------the random: " + randomMovSim.getRandom());
	    }
	    
	    
	    
	    if (GlobalConstants.TRANSITION_RANDOM_SHIFT)
	    {
	    	// move on x direction
	    	clonedState.movsimPF.shiftTraffic(GlobalConstants.G_RAND.nextGaussian()*GlobalConstants.SHIFT_X_SIGMA);
	    	
	    	// move on y direction
	    	double yRoll = GlobalConstants.G_RAND.nextDouble();
	    	if(yRoll>GlobalConstants.SHIFT_Y_THRESHOLD)
	    		clonedState.movsimPF.rollupLane();
	    	else if(yRoll<-GlobalConstants.SHIFT_Y_THRESHOLD)
	    		clonedState.movsimPF.rolldownLane();
	    }
	    
	    if (GlobalConstants.G_RAND.nextDouble() < GlobalConstants.TRANSITION_ACCIDENT_RATE) {
			//place a random obstacle
	    	clonedState.movsimPF.placeRandomObstacle(GlobalConstants.G_RAND);
		}
		
	    clonedState.movsimPF.runFor(stepLength);
	    
	    
	    /*new SmcSimulationCanvas(clonedState.movsimPF, "!!!!!!!!!!!!1");
	    
	    MovSimState clonedState2 = this.clone();
	    clonedState2.movsimPF.addRandomComponent(0.05);
	    clonedState2.movsimPF.runFor(stepLength);
	    new SmcSimulationCanvas(clonedState2.movsimPF, "!!!!!!!!!!!!2");
	    
	    MovSimState clonedState3 = this.clone();
	    clonedState3.movsimPF.addRandomComponent(0.07);
	    clonedState3.movsimPF.runFor(stepLength);
	    new SmcSimulationCanvas(clonedState3.movsimPF, "!!!!!!!!!!!!3");*/
	    
	    
		return clonedState;
		
		//return this.transitionFunction();
	}

	public static class MovSimMeasurement extends AbstractMeasurement{
		private List<MovSimSensor> sensors;
		public MovSimMeasurement( List<MovSimSensor> sensors ) { this.sensors = sensors; };
		public List<MovSimSensor> getSensorReading() {
			return sensors;
		}
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

		// Peisheng 20150202 
		for (int iArea = 0; iArea < areaList.size(); iArea++) {		
			for (int i = 0; i < sensorReadings.size(); i++)
			{
				MovsimArea area = areaList.get(iArea);
				if (area.getRoadSeg() == sensorReadings.get(i).getRoadID() 
						||
					area.getRoadSeg() == -1	) {
					
					double normDis = singleSensorNormlizedDistance(sensorReadings.get(i), simulatedSensorReadings.get(i));
					max = max > normDis?max:normDis;
					double normResult = norm.density(normDis);
					double minNorm = 1E-300; // if not doing so, a small value will become 0, and mess up the weight
					if (normResult < minNorm) normResult = minNorm;
					
					// System.out.println("sensor-" + i + " norm dis=" + normDis + "-> L=" + normResult);

					weight = weight.multiply(BigDecimal.valueOf(normResult));	
				}
				
			}

		}
		return weight;
		//return BigDecimal.ONE;
	}
	protected double singleSensorNormlizedDistance( MovSimSensor ss1, MovSimSensor ss2, double start, double end )
	{
		MovSimSensor2 s1 = (MovSimSensor2)ss1;
		MovSimSensor2 s2 = (MovSimSensor2)ss2;
		// normalize speed
		double norSpeedDiff = Math.abs(s1.getAvgSpeed(start,end) - s2.getAvgSpeed(start,end)) / (s1.getMaxSpeed() - s1.getMinSpeed()); 
		// normalize acceleration
		double norAccDiff = Math.abs(s1.getAvgAcc(start,end) - s2.getAvgAcc(start,end)) / 5; /*(s1.getMaxAcc() - s1.getMinAcc());*/
		// normalize vehicle number
		double norCarNumberDiff = Math.abs(s1.getVehNumber(start,end) - s2.getVehNumber(start,end)) / (double)(s1.getMaxVehNumber() - s1.getMinVehNumber());
		
		System.out.println( "speedD=" + norSpeedDiff + ", accD="+norAccDiff+", carNumberD=" + norCarNumberDiff);
		
		// weights on factors
		double numberWeight = 0.5;
		double speedWeight = 0.3;
		double accWeight = 1.0 - numberWeight-speedWeight;
		
		
		return speedWeight*norSpeedDiff + accWeight*norAccDiff + numberWeight*norCarNumberDiff;
		
	}
	
	protected double singleSensorNormlizedDistance( MovSimSensor ss1, MovSimSensor ss2 )
	{
		MovSimSensor2 s1 = (MovSimSensor2)ss1;
		MovSimSensor2 s2 = (MovSimSensor2)ss2;
		// normalize speed
		double norSpeedDiff = Math.abs(s1.getAvgSpeed() - s2.getAvgSpeed()) / (s1.getMaxSpeed() - s1.getMinSpeed()); 
		// normalize acceleration
		double norAccDiff = Math.abs(s1.getAvgAcc() - s2.getAvgAcc()) / 5; /*(s1.getMaxAcc() - s1.getMinAcc());*/
		// normalize vehicle number
		double norCarNumberDiff = Math.abs(s1.getVehNumber() - s2.getVehNumber()) / (double)(s1.getMaxVehNumber() - s1.getMinVehNumber());
		
		System.out.println( "speed2=" + s2.getAvgSpeed() + ", acc2="+s2.getAvgAcc()+", carNumber2=" + s2.getVehNumber());
		//System.out.println( "speedD=" + norSpeedDiff + ", accD="+norAccDiff+", carNumberD=" + norCarNumberDiff);
		
		// weights on factors
		double numberWeight = 0.5;
		double speedWeight = 0.3;
		double accWeight = 1 - numberWeight-speedWeight;
		
		
		return speedWeight*norSpeedDiff + accWeight*norAccDiff + numberWeight*norCarNumberDiff;
		
	}
	
	
	@Override
	public AbstractState propose(AbstractMeasurement measurement) throws StateFunctionNotSupportedException, Exception
	{
		
		MovSimState nextMovSimState = (MovSimState) this.transitionModel(drawNextRandomComponentSample());
		MovSimMeasurement movSimMeasurement =  (MovSimMeasurement) measurement;
		
		
		// about removing accident
		double proposalHighThreshold = 15;
		boolean removeAcc = true;
		
		// about adding accident
		double proposalLowAccThreshold = 8;
		double proposalAccRate = 0.5;
		
		// about speed and acceleration
		boolean changeSpeed = false;
		boolean changeAcceleration = false;
		
		nextMovSimState.movsimPF.setStates(
				movSimMeasurement.sensors,
				proposalLowAccThreshold, 
				proposalHighThreshold,
				GlobalConstants.G_RAND, 
				proposalAccRate, 
				removeAcc,
				changeSpeed, 
				changeAcceleration);

		
		// nextMovSimState = (MovSimState) this.transitionModel(drawNextRandomComponentSample());
		// return nextMovSimState.transitionModel(drawNextRandomComponentSample());
		return nextMovSimState;
	}

	@Override
	public AbstractTransitionRandomComponent drawNextRandomComponentSample()
	{
		
		//MovSimRandomComponent randomComponent = new MovSimRandomComponent(( random.nextDouble()* (0.1-0)));
		double randomDouble = GlobalConstants.G_RAND.nextDouble();
		//System.out.println("-------------------------------------------------------random: " + randomDouble);
		MovSimRandomComponent randomComponent = new MovSimRandomComponent(randomDouble);
		return randomComponent;
	}

	@Override
	public double distance(AbstractState sample)
	{
		MovSimState samplePF = (MovSimState) sample;
		double dis = this.movsimPF.CalDistance(samplePF.movsimPF);
		//System.out.println("State Distance: " + dis + " " + ((long) (dis*100000000)));
		return dis;
	}

	// not-supported functions
	@Override
	public BigDecimal proposalPdf(AbstractMeasurement measurement) throws StateFunctionNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}
	
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
	
	
	/**
	 * used to report the state information
	 */
	public String reportState() {
	
		return movsimPF.getStateReport();
	}
	
	/**
	 * 
	 * @param ss1 
	 * @return
	 */
	public MovsimArea convert2MovsimArea(MovSimSensor ss1) {
		MovSimSensor2 s2 = (MovSimSensor2) ss1;
		return new MovsimArea(s2.getRoadID(),s2.getDeployedPosLeft(),s2.getDeployedPosRight());
	}
}

