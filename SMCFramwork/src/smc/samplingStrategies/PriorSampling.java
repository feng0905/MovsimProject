package smc.samplingStrategies;

import smc.AbstractState;
import smc.AbstractState.StateFunctionNotSupportedException;
import smc.SamplingStrategy;

/**
 * Draw a sample from the transition prior. Given s_t = f(s_t-1, v), draw a sample from p(v), then apply f to get s_t.
 * @author Haidong Xue
 *
 */
public class PriorSampling extends SamplingStrategy
{
	@Override
	public AbstractState sampling(AbstractState currentState,  AbstractState.AbstractMeasurement measurement)
	{
		AbstractState s=null;
		try
		{
			s = currentState.transitionModel(currentState.drawNextRandomComponentSample());
		}
		catch (StateFunctionNotSupportedException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		return s;
		
	}

}
