package SubStatePF;

import java.math.BigDecimal;
import java.util.Vector;


import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

//import movSimSMC.MovSimState;
import smc.*;
import smc.AbstractState.StateFunctionNotSupportedException;

public class AbstractSubStateParticleSystem extends AbstractParticleSystem{
	private Vector<Vector<Particle>> vecSubParticles;
	private Vector<Vector<AbstractState>> vecSubStates;
	private int subStateNumber;
	private GenerateSubStates subStateGenerateStrategy;
	private DataAssociation dataAssociationStrategy;
	
	public Vector<Particle> vecBeforeUpdateParticlesSet;// Particle set storing the particles before updating. This set is used for debugging.
	
	private SubStateSystematicResampling substate_resampler;
	
	Vector<Particle> bestSubStateParticleBeforeResampling;
	private AbstractMeasurement measurement;
	
	public AbstractSubStateParticleSystem( SamplingStrategy sampler, WeightUpdatingStrategy weightUpdater, SubStateSystematicResampling resampler, GenerateSubStates substateGenerator, DataAssociation dataAssociationStrategy, Vector<Particle> particleSet)
	{
		super(sampler,weightUpdater,resampler);
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
	
	
	public void GenerateSubStateParticle(AbstractMeasurement measurement){
	
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
				System.out.println("====================================");
				try {
					p.state.measurementPdf(measurement);
				} catch (StateFunctionNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Before Generating the substates.....");
//				
				
				Vector<AbstractState> vecSubStates=this.subStateGenerateStrategy.DivideState(p.state); 
				if(i==this.particleSet.size()-1 ){
					System.out.println();
					try {
						vecSubStates.get(3).measurementPdf(measurement);
					} catch (StateFunctionNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Before updating the weight.....");
					
					AbstractState[] arraySubstates=new AbstractState[vecSubStates.size()];
					for(int j=0;j<arraySubstates.length;j++){
						arraySubstates[j]=vecSubStates.get(j);
					}
					
					AbstractState testState = null;
					try {
						testState = this.subStateGenerateStrategy.formFullState(arraySubstates);
					} catch (JAXBException | SAXException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					System.out.println();
					try {
						testState.measurementPdf(measurement);
					} catch (StateFunctionNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Before form full state.....");
					
				}
				
				(this.vecSubStates).add(vecSubStates);
				for(int j=0;j<subStateNumber;j++){
					Particle subParticle=new Particle(vecSubStates.get(j), BigDecimal.valueOf(1)) ;
					vecSubParticles.get(j).set(i, subParticle);
				}
			
			}
			
			
			
		
	}
	
	@Override
	public void updateParticle( AbstractMeasurement measurement )
	{	
		this.measurement=measurement;
		
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
		//Storing the orginal particle for debugging
		vecBeforeUpdateParticlesSet=this.particleSet;
		
		GenerateSubStateParticle(measurement);
		
		
		
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
        
		 
//		System.out.println();
//		try {
//			vecBeforeUpdateParticlesSet.get(this.particleSet.size()-1).state.measurementPdf(measurement);
//		} catch (StateFunctionNotSupportedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("At the end of  updating the weight.......................");
		
	}
	
	/**
	 * 
	 * @return the particle with the highest weight among defined particle set. Note: it is meanlingless after a resampling
	 */
	public Particle getHighestWeightParticle(Vector<Particle> ParticelSet)
	{
		Particle maxP = new Particle(null,null);
		BigDecimal max = BigDecimal.ZERO;
		int maxP_idx=-1,i=0;
		for( Particle p  : ParticelSet)
		{
			
			if( max.compareTo(p.weight)<0)
			{
				maxP.state = p.state;
				max = p.weight;
				maxP_idx=i;
				
			}
			i++;
			
		}
		 
		System.out.println("Best particle---"+maxP_idx+ "     ID="+  System.identityHashCode(maxP.state));
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
			System.out.println("State ID ="+System.identityHashCode(p.state));
			
			System.out.println();
			try {
				p.state.measurementPdf(this.measurement);
			} catch (StateFunctionNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("In getHighestWeightParticle.......................");
			
			
			
		}
		
		try {
			fullState=this.subStateGenerateStrategy.formFullState(subStates);
		} catch (JAXBException | SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println();
		System.out.println("Debugging best particle");
		try {
			fullState.measurementPdf(measurement);
		} catch (StateFunctionNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("End debugging .......................");
		
		
		maxP=new Particle(fullState,max);
		
		return maxP;
	}
	
	public Particle getParticle(int idx){
		
		return this.particleSet.get(idx);
	}
}
