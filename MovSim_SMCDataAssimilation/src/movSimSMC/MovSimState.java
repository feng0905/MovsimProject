package movSimSMC;

import java.math.BigDecimal;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import movsimSMC.MovsimPF;
import smc.AbstractState;


public class MovSimState extends AbstractState 
{
	private MovsimPF movsimPF; 
	private double simStep = 10;			// seconds
	


	MovSimState() throws JAXBException, SAXException {
		String baseDir = System.getProperty("user.dir");
		String[] args = { "-f", baseDir + "/sim/buildingBlocks/startStop.xprj" };
  		movsimPF = new MovsimPF(args);
	}
	
	MovSimState(MovsimPF movsimPF){
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
		MovsimPF nextState = null;
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
		return 0;
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
