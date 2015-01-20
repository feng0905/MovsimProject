package smc.specialParticleSystems;

import java.util.Vector;

import smc.AbstractParticleSystem;
import smc.Particle;
import smc.resamplingStrategies.SystematicResampling;
import smc.samplingStrategies.PriorSampling;
import smc.weightUpdatingStrategies.LikelihoodWeight;


/**
 * The SMC system using the transition prior as the proposal, the likelihood as incremental weight.
 *  
 * @author Haidong
 *
 */
public class BootstrapFilter extends AbstractParticleSystem
{
	public BootstrapFilter(Vector<Particle> particleSet )
	{
		super( new PriorSampling(), new LikelihoodWeight(), new SystematicResampling(), particleSet);
	}

}
