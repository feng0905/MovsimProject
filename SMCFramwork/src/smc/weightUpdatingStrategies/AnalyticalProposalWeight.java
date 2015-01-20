package smc.weightUpdatingStrategies;

import java.util.Vector;

import smc.AbstractState.AbstractMeasurement;
import smc.AbstractState.StateFunctionNotSupportedException;
import smc.Particle;
import smc.SamplingStrategy;
import smc.WeightUpdatingStrategy;

public class AnalyticalProposalWeight extends WeightUpdatingStrategy
{

	@Override
	protected void updateUnnormalizedWeights(Vector<Particle> particleSet, AbstractMeasurement measurement, SamplingStrategy sampler)
	{
		try
		{
			for(Particle p:particleSet )
				p.weight = p.weight.multiply(p.state.proposalPdf(measurement));
		}
		catch (StateFunctionNotSupportedException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
	}

}
