package smc.weightUpdatingStrategies;

import java.util.Vector;

import smc.AbstractState.AbstractMeasurement;
import smc.AbstractState.StateFunctionNotSupportedException;
import smc.Particle;
import smc.SamplingStrategy;
import smc.WeightUpdatingStrategy;

public class LikelihoodWeight extends WeightUpdatingStrategy
{

	@Override
	protected void updateUnnormalizedWeights(Vector<Particle> particleSet, AbstractMeasurement measurement, SamplingStrategy sampler)
	{
		try
		{
			int i=0;
			for (Particle p : particleSet)
			{
				System.out.println();
				System.out.println("Particle-"+i+">>>>>>");
				p.weight = p.weight.multiply(p.state.measurementPdf(measurement));
				// System.out.printf("Weight-%d: %5e%n", i++, p.weight);
				i++;
			}
		}
		catch (StateFunctionNotSupportedException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
