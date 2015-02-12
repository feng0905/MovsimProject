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
	
	Vector<Particle> bestSubStateParticleBeforeResampling;
	
	public AbstractSubStateParticleSystem( SamplingStrategy sampler, WeightUpdatingStrategy weightUpdater, SubStateSystematicResampling resampler, GenerateSubStates substateGenerator, DataAssociation dataAssociationStrategy, Vector<Particle> particleSet)
	{
		super(sampler,weightUpdater,resampler,particleSet);
		this.substate_resampler=resampler;
		this.subStateGenerateStrategy=substateGenerator;
		this.subStateNumber=substateGenerator.getSubStateNumber();
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
					Particle subParticle=new Particle(vecSubStates.get(j), BigDecimal.valueOf(1)) ;
					vecSubParticles.get(j).set(i, subParticle);
				}
			}
			
		
	}
	
	@Override
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
		bestSubStateParticleBeforeResampling=new Vector<Particle>();
		
		for(int subStateIdx=0;subStateIdx<this.vecSubParticles.size();subStateIdx++){
			System.out.println("SubState-"+subStateIdx+":");
			Vector<Particle> subParticleSet=this.vecSubParticles.get(subStateIdx);
			//WeightUpdating
			weightUpdater.updateWeights(subParticleSet, measurement, sampler);
			bestSubStateParticleBeforeResampling.add(getHighestWeightParticle(subParticleSet));
			//Re-sampling
			subParticleSet = substate_resampler.resampling(subParticleSet);
			this.vecSubParticles.set(subStateIdx, subParticleSet);
			
		}
		//Record the best sample
		bestParticleBeforeResampling = this.getHighestWeightParticle();
		
		this.particleSet=this.substate_resampler.ParticleCombination(this.vecSubParticles, this.subStateGenerateStrategy);
        
		
	}
	
	/**
	 * 
	 * @return the particle with the highest weight among defined particle set. Note: it is meanlingless after a resampling
	 */
	public Particle getHighestWeightParticle(Vector<Particle> ParticelSet)
	{
		Particle maxP = null;
		BigDecimal max = BigDecimal.ZERO;
		for( Particle p  : ParticelSet)
		{
			if( max.compareTo(p.weight)<0)
			{
				maxP = p;
				max = p.weight;
			}
		}
		return maxP;
	}
	
	/**
	 * 
	 * @return the combined particle with the highest weight . Note: it is meanlingless after a resampling
	 */
	@Override
	public Particle getHighestWeightParticle()
	{
		Particle maxP = null;
		BigDecimal max = BigDecimal.ZERO;
		
		AbstractState[] subStates=new AbstractState[this.subStateNumber];
		AbstractState fullState=null;
		 
		int i=0;
		for( Particle p  : this.bestSubStateParticleBeforeResampling)
		{
			subStates[i++]=p.state;
			
		}
		
		fullState=this.subStateGenerateStrategy.formFullState(subStates);
		maxP=new Particle(fullState,max);
		
		return maxP;
	}
}
