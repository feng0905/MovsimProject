package smc.resamplingStrategies;

import java.math.BigDecimal;
import java.util.Random;
import java.util.Vector;

import smc.AbstractState;
import smc.GlobalConstants;
import smc.Particle;
import smc.ResamplingStrategy;

/**
 * The Systematic resampling strategy. Note the weights on input particles have to be normalized.
 * 
 * @author Haidong Xue
 *
 */
public class SystematicResampling extends ResamplingStrategy
{
	private Random rand;

	public SystematicResampling()
	{
		rand = GlobalConstants.RAND;
	}
	
	public SystematicResampling (long randomSeed )
	{
		rand = new Random(randomSeed);
	}
	
	@Override
	public Vector<Particle> resampling(Vector<Particle> particleSet)
	{
		System.out.println("SMC======================Resampling Started");
		for(int i=0; i<particleSet.size();i++)
			System.out.format("%5.4f   ", particleSet.elementAt(i).weight);
		System.out.println();
		
		// Number of particles
		int N = particleSet.size();
		
		// Return the same particle set, if N<=1
		if(N<=1) return particleSet;  
		
		// Determine the number of offsprings of each particle
		BigDecimal u = BigDecimal.valueOf(rand.nextDouble()/N);
		int[] offspringNumber = new int[N]; 
		BigDecimal preSum= BigDecimal.ZERO;
		for(int i=0; i<N; i++)
		{
			offspringNumber[i]=0;
			preSum = preSum.add(particleSet.elementAt(i).weight);
			for(;u.compareTo(preSum)<=0; u=u.add(BigDecimal.valueOf(1.0/N)))
			{
				offspringNumber[i]++;
			}
		}
		
		for(int i=0; i<N; i++)
			System.out.printf("%06d   ", offspringNumber[i]);
		System.out.println();
		
		// Duplicate samples
		Vector<Particle> resampledParticles = new Vector<Particle>();
		for( int i=0; i<N; i++)
		{
			for(int j=0; j<offspringNumber[i]; j++)
			{
				Particle p = particleSet.elementAt(i);
				try
				{
					resampledParticles.add(new Particle( (AbstractState)p.state.clone(), BigDecimal.valueOf(1.0/N) ));
				}
				catch (CloneNotSupportedException e)
				{
					e.printStackTrace();
					System.exit(1); // abnormal exit
				}
			}
		}
		
		return resampledParticles;
	}

}
