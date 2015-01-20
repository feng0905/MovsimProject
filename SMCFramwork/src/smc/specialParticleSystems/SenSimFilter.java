package smc.specialParticleSystems;

import java.math.BigDecimal;
import java.util.Vector;

import smc.AbstractParticleSystem;
import smc.Particle;
import smc.resamplingStrategies.SystematicResampling;
import smc.samplingStrategies.ProposalSampling;
import smc.weightUpdatingStrategies.KernelEstimationProposalWeight;
import smc.weightUpdatingStrategies.KernelEstimationProposalWeight.KernelFunction;

/**
 * The SMC system using SenSim proposal (as proposed in Haidong's PhD dissertation), KernelEstimation weight updating.
 * @author Haidong
 *
 */
public class SenSimFilter extends AbstractParticleSystem
{
	public SenSimFilter( Vector<Particle> particleSet, int particleNumberForEstimation, KernelFunction kernelFunction, BigDecimal bandWidth )
	{
		super(new ProposalSampling(), new KernelEstimationProposalWeight(particleNumberForEstimation, kernelFunction, bandWidth), new SystematicResampling(), particleSet);
		//super(new ProposalSampling(), new LikelihoodWeight(), new SystematicResampling(), particleSet);
	}

}
