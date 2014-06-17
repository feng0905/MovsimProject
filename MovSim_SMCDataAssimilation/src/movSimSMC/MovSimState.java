package movSimSMC;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import movsimSMC.MovsimWrap;
import smc.AbstractState;


public class MovSimState extends AbstractState 
{
	private MovsimWrap movsimPF; 
	private double simStep = 10;			// seconds
	
	public static void main(String[] args) throws JAXBException, SAXException, StateFunctionNotSupportedException{
//		MovSimState movsim = new MovSimState();
//		MovSimState clone = new MovSimState(movsim.movsimPF); 
//		movsim.transitionFunction();	
//		long distance = movsim.distance(clone);
//		System.out.println("The distance between the wrappers is " + distance);
		
		
//		MovSimState sim = new MovSimState();
//		//list.add(sim);
//		sim = (MovSimState) sim.transitionFunction();
//		sim.movsimPF.placeObstacle(1,1);
//		
//		
//		MovSimState sim2 = new MovSimState();
//		//list.add(sim);
//		sim2 = (MovSimState) sim.transitionFunction();
//		sim2.movsimPF.placeObstacle(1,2);
		
		/*
		 * Accident display Test 
		 */
		ArrayList<MovSimState> list = new ArrayList<MovSimState>();
		for (int i = 1; i < 10; i++) {
			MovSimState sim = new MovSimState();
			//list.add(sim);
			sim = (MovSimState) sim.transitionFunction();
			sim.movsimPF.placeObstacle(i%4+1,1);
		}

	}
	
	
	MovSimState() throws JAXBException, SAXException {
		String baseDir = System.getProperty("user.dir");
		String[] args = { "-f", baseDir + "\\sim\\buildingBlocks\\startStop.xprj" };
  		movsimPF = new MovsimWrap(args);
	}
	
	MovSimState(MovsimWrap movsimPF){
		this.movsimPF = movsimPF;
	}
	
	@Override
	public void setDescription(String des)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractState transitionFunction() throws StateFunctionNotSupportedException
	{
		// TODO Auto-generated method stub
		MovsimWrap nextState = null;
		try {
			nextState = movsimPF.duplicate();
		} catch (JAXBException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    if (nextState == null) {
	    	return null;
	    	
		}
		
	    nextState.runFor(simStep);
	    System.out.println("transition finished");
    	return new MovSimState(nextState);
	}

	@Override
	public AbstractState transitionModel(AbstractTransitionRandomComponent random) throws StateFunctionNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal transitionPdf(AbstractState nextState) throws StateFunctionNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractMeasurement measurementFunction() throws StateFunctionNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractMeasurement measurementModel(AbstractMeasurementRandomComponent random) throws StateFunctionNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal measurementPdf(AbstractMeasurement measurement) throws StateFunctionNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal proposalPdf(AbstractMeasurement measurement) throws StateFunctionNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractState generateNoisedState() throws StateFunctionNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractState propose(AbstractMeasurement measurement) throws StateFunctionNotSupportedException, Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractTransitionRandomComponent drawNextRandomComponentSample()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long distance(AbstractState sample)
	{
		// TODO Auto-generated method stub
		MovSimState samplePF = (MovSimState) sample;
		return (long) this.movsimPF.CalDistance(samplePF.movsimPF);
	}

	
	/**
	 * @return the simStep
	 */
	public double getSimStep() {
		return simStep;
	}


	/**
	 * @param simStep the simStep to set
	 */
	public void setSimStep(double simStep) {
		this.simStep = simStep;
	}
}
