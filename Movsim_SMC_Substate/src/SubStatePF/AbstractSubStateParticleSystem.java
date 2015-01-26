package SubStatePF;

import java.math.BigDecimal;
import java.util.Vector;
import smc.*;

public abstract class AbstractSubStateParticleSystem extends AbstractParticleSystem{
	private Vector<Vector<Particle>> vecSubParticles;
	private Vector<Vector<AbstractState>> vecSubStates;
	private int subStateNumber;
	private GenerateSubStates subStateGenerateStrategy;
	private DataAssociation dataAssociationStrategy;
	
	private SubStateSystematicResampling substate_resampler;
	public AbstractSubStateParticleSystem( SamplingStrategy sampler, WeightUpdatingStrategy weightUpdater, SubStateSystematicResampling resampler, GenerateSubStates substateGenerator, DataAssociation dataAssociationStrategy, Vector<Particle> particleSet)
	{
		super(sampler,weightUpdater,resampler,particleSet);
		this.substate_resampler=resampler;
		this.subStateGenerateStrategy=substateGenerator;
		this.dataAssociationStrategy=dataAssociationStrategy;
	}
	
	void setSubStateGenerateStrategy(GenerateSubStates subStateGenerator){
		this.subStateGenerateStrategy=subStateGenerator;
	}
	
	void setDataAssociationStrategy(DataAssociation dataAssociationStrategy){
		this.dataAssociationStrategy=dataAssociationStrategy;
	}
	
	
	public void GenerateSubStateParticle(){
	
		vecSubParticles=new Vector<Vector<Particle>>();
		vecSubStates=new  Vector<Vector<AbstractState>>();
			for(int i=0;i<subStateNumber;i++){
				Vector<Particle> vecSubParticle=new Vector<Particle>();
				for(int j=0;j<this.particleSet.size();j++){
					Particle subParticle=new Particle(null,null) ;
					vecSubParticle.add(subParticle);
				}
				vecSubParticles.add(vecSubParticle);
				 
			}
				
			for ( int i=0;i<this.particleSet.size();i++)
			{
				Particle p=this.particleSet.get(i);
				Vector<AbstractState> vecSubStates=this.subStateGenerateStrategy.DivideState(p.state); 
				(this.vecSubStates).add(vecSubStates);
				for(int j=0;j<subStateNumber;j++){
					Particle subParticle=new Particle(vecSubStates.get(i), BigDecimal.valueOf(1)) ;
					vecSubParticles.get(i).set(j, subParticle);
				}
			}
			
		
	}
	public void updateParticle( AbstractState.AbstractMeasurement measurement )
	{	
		//Sampling
		int i=0;
		System.out.print("Sampling: ");
		for ( Particle p:particleSet)
		{
			System.out.print(i++ + " ");
			AbstractState temp = p.state;
			p.state = sampler.sampling(p.state, measurement);
			p.state.previousState = temp;
			p.state.previousState.previousState = null;  // very important, or it forms a linked list, and consume memory fast
		}
		System.out.println();
		
		GenerateSubStateParticle();
		for(i=0;i<this.particleSet.size();i++){
			this.dataAssociationStrategy.AssignMeasurment(this.vecSubStates.get(i), measurement);
		}
	
		
		
		
		//for each substate
		for(int subStateIdx=0;i<this.vecSubParticles.size();i++){
			Vector<Particle> subParticleSet=this.vecSubParticles.get(subStateIdx);
			//WeightUpdating
			weightUpdater.updateWeights(subParticleSet, measurement, sampler);
					
			//Re-sampling
			subParticleSet = substate_resampler.resampling(subParticleSet);
			this.vecSubParticles.set(subStateIdx, subParticleSet);
			
		}
		
		this.particleSet=this.substate_resampler.ParticleCombination(this.vecSubParticles, this.subStateGenerateStrategy);
        
		//Record the best sample
		bestParticleBeforeResampling = this.getHighestWeightParticle();
		
	}
}
