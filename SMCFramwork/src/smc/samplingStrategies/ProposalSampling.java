package smc.samplingStrategies;

import smc.AbstractState;
import smc.AbstractState.AbstractMeasurement;
import smc.AbstractState.StateFunctionNotSupportedException;
import smc.SamplingStrategy;

public class ProposalSampling extends SamplingStrategy
{
	@Override
	public AbstractState sampling(AbstractState currentState, AbstractMeasurement measurement)
	{
		try
		{
			return currentState.propose(measurement);
		}
		catch (StateFunctionNotSupportedException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
