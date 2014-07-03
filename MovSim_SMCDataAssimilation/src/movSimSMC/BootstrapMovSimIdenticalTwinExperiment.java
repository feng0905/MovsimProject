package movSimSMC;

import java.util.Vector;

import smc.AbstractParticleSystem;
import smc.Particle;
import smc.specialParticleSystems.BootstrapFilter;

public class BootstrapMovSimIdenticalTwinExperiment extends AbstractMovSimIdenticalTwinExperiment
{

	public BootstrapMovSimIdenticalTwinExperiment(int stepLength)
	{
		super(stepLength);
	}

	@Override
	protected AbstractParticleSystem createParticleSystem(Vector<Particle> particleSet)
	{
		return new BootstrapFilter(particleSet);
	}

}
