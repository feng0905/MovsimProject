package SubStatePF;

import java.util.Vector;

import smc.Particle;
import smc.SamplingStrategy;
import smc.WeightUpdatingStrategy;
import smc.resamplingStrategies.SystematicResampling;
import smc.samplingStrategies.PriorSampling;
import smc.weightUpdatingStrategies.LikelihoodWeight;

public class SubStateFilter extends AbstractSubStateParticleSystem {

	public SubStateFilter(SamplingStrategy sampler,
			WeightUpdatingStrategy weightUpdater,
			SubStateSystematicResampling resampler,
			GenerateSubStates substateGenerator,
			DataAssociation dataAssociationStrategy,
			Vector<Particle> particleSet) {
		super(sampler, weightUpdater, resampler, substateGenerator,
				dataAssociationStrategy, particleSet);
		// TODO Auto-generated constructor stub
	}

	public SubStateFilter(Vector<Particle> particleSet )
	{
		super( new PriorSampling(), new LikelihoodWeight(), new SubStateSystematicResampling(), new GenerateSubStates(),new DataAssociation(),  particleSet);
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
