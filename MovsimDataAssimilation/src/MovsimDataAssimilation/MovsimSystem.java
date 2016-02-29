package MovsimDataAssimilation;

import java.util.Random;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;





import movsimSMC.MovsimWrap;
import smc.AbstractMeasurement;
import dataAssimilationFramework.AbstractSpace;
import dataAssimilationFramework.AbstractSpaceTemporalSystem;
import dataAssimilationFramework.SystemFactoryInterface;


public class MovsimSystem extends AbstractSpaceTemporalSystem implements Cloneable{
	
	protected MovsimWrap movsimPF; 
	protected double stepLength = GlobalConstants.STEP_LENTH;	
	
	/**
	 * This constructor is used when constructing the system in experiment class
	 * @param stepLength
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public MovsimSystem( ) throws JAXBException, SAXException {
		super();
		String[] args = { "-f", "../sim/buildingBlocks/startStop.xprj" };
  		movsimPF = new MovsimWrap(args);
  		
	}
	
	/**
	 * Never been used. May need removed 
	 * @param movsimPF
	 */
	public MovsimSystem(MovsimWrap movsimPF){
		try {
			this.movsimPF = movsimPF.duplicate();
			
		} catch (JAXBException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public double distance(AbstractSpaceTemporalSystem sys) {
		// TODO Auto-generated method stub
		return this.movsimPF.CalDistance(((MovsimSystem)sys).movsimPF);
	}

	@Override
	public AbstractSpaceTemporalSystem transition() {
		// TODO Auto-generated method stub
		this.movsimPF.runFor(stepLength);
		return this;
	}

	@Override
	public AbstractSpaceTemporalSystem transitionModel(
			Random rand, double timestep) {
		// TODO Auto-generated method stub
		
		if (GlobalConstants.TRANSITION_RANDOM_SHIFT)
	    {
	    	// move on x direction
	    	movsimPF.shiftTraffic(rand.nextGaussian()*GlobalConstants.SHIFT_X_SIGMA);
	    	
	    	// move on y direction
	    	double yRoll = GlobalConstants.G_RAND.nextDouble();
	    	if(yRoll > GlobalConstants.SHIFT_Y_THRESHOLD)
	    		movsimPF.rollupLane();
	    	else if(yRoll< GlobalConstants.SHIFT_Y_THRESHOLD)
	    		movsimPF.rolldownLane();
	    }
	    
	    if (GlobalConstants.G_RAND.nextDouble() < GlobalConstants.TRANSITION_ACCIDENT_RATE) {
			//place a random obstacle
	    	movsimPF.placeRandomObstacle(GlobalConstants.G_RAND);
		}
		
	    movsimPF.runFor(stepLength);
		
		return this;
	}

	@Override
	public AbstractMeasurement measurementFunction(AbstractSpace Space) {
		// TODO Auto-generated method stub
		return new MovsimMeasurement(this.movsimPF.getSensorReading(),(MovsimSpace)space);
	}

	@Override
	public void propose(AbstractMeasurement measurement) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public AbstractSpace createSystemSpace() {
		// TODO Auto-generated method stub
		return new MovsimSpace(movsimPF.getRoadNetwork());
	}

	public MovsimWrap getMovSimWrap() {
		// TODO Auto-generated method stub
		return movsimPF;
	}

	public void createObstacle(double startTime, int roadId, int laneId) {
		movsimPF.createObstacle(startTime, roadId, laneId);
	}
	
	public void createSelfRecoverObstacle(double startTime, int roadId, int laneId,double endTime) {
		movsimPF.createSelfRecoveryObstacle(startTime, roadId, laneId, endTime);
	}


	
}
