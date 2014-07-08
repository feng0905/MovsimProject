package movSimSMC;

import java.math.BigDecimal;
import java.util.Vector;

import smc.AbstractParticleSystem;
import smc.Particle;
import smc.specialParticleSystems.SenSimFilter;
import smc.weightUpdatingStrategies.KernelEstimationProposalWeight.KernelFunction;

public class SenSimMovSimIdenticalTwinExperiment extends AbstractMovSimIdenticalTwinExperiment
{
	private KernelFunction kernel;
	private BigDecimal bandWidth;
	private int kernelParticleNumbers; 
	

	public SenSimMovSimIdenticalTwinExperiment(int stepLength, int kernelParticleNumbers, KernelFunction kernel, BigDecimal bandWidth)
	{
		super(stepLength);
		
		this.kernel = kernel;
		this.bandWidth = bandWidth;
		this.kernelParticleNumbers = kernelParticleNumbers;
	}

	@Override
	protected AbstractParticleSystem createParticleSystem(Vector<Particle> particleSet)
	{
		return new SenSimFilter(particleSet, kernelParticleNumbers, this.kernel, this.bandWidth);
	}
}
