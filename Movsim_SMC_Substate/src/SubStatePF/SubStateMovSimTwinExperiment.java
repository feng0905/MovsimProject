package SubStatePF;

import java.util.Vector;

import smc.AbstractParticleSystem;
import smc.Particle;
import smc.specialParticleSystems.SenSimFilter;
import movSimSMC.AbstractMovSimIdenticalTwinExperiment;
import movSimSMC.BootstrapMovSimIdenticalTwinExperiment;

public class SubStateMovSimTwinExperiment extends
		AbstractMovSimIdenticalTwinExperiment {

	public SubStateMovSimTwinExperiment(int stepLength) {
		super(stepLength);
		 
	}

	@Override
	protected AbstractParticleSystem createParticleSystem(
			Vector<Particle> particleSet) {
		return new SubStateFilter(particleSet);
		 
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int particleN = 5;
		int stepLength = 15;
		int stepN = 5;
		
		SubStateMovSimTwinExperiment exp = new SubStateMovSimTwinExperiment(stepLength);
		try
		{
			exp.runDataAssimilationExperiement(stepN, particleN);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
