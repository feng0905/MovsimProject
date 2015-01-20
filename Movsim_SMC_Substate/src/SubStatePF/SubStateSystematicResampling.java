package smc.resamplingStrategies;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import smc.AbstractState;
import smc.GenerateSubStates;
import smc.Particle;
import smc.AbstractFullState;

public class SubStateSystematicResampling extends SystematicResampling{
	public  Vector<Particle>  ParticleCombination(Vector<Vector<Particle>> vecSubParticleSet, GenerateSubStates subStateGenerator){
		Vector<Particle> vecCombinedParticleSet=new Vector<Particle>();
	 
		int N=vecSubParticleSet.get(0).size(); //N is the sample size
		//initialization of new particle vector
		for(int j=0;j<N;j++){
			Particle p=new Particle(null, null);
			try {
				vecCombinedParticleSet.add(new Particle( (AbstractState)p.state.clone(), BigDecimal.valueOf(1.0/N) ));
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//for each substate
		int randomSelectList[][]=new int[vecSubParticleSet.size()][N];
		for(int i=0;i<vecSubParticleSet.size();i++){
			randomSelectList[i]=RandomSelectParticles(N);
		}
		
		
			
		for(int j=0;j<N;j++){
			AbstractState subStates[]=new AbstractState[vecSubParticleSet.size()];
			for(int i=0;i<vecSubParticleSet.size();i++){
				subStates[i]=vecSubParticleSet.get(i).get(j).state;
			}
			AbstractState fullState=subStateGenerator.formFullState(subStates);
			vecCombinedParticleSet.get(j).state=fullState;
		}
			
		
		return vecCombinedParticleSet;
	}
	
	public int[] RandomSelectParticles(int n){
		int[] randomselectedParticles=new int[n];
		LinkedList<Integer> selectedParticlesList=new LinkedList<Integer>();
		System.out.println("Standard selected particles:");
	
		for (int i = 0; i < randomselectedParticles.length; i++) {
			selectedParticlesList.add(i);
			System.out.print(" "+i);
			
		}
		
		System.out.println();
		
	
		Random random=new Random(11111);
	
		for (int i = 0; i < randomselectedParticles.length; i++) {
			int idx=random.nextInt(n-i);
			
			randomselectedParticles[i]=(Integer)selectedParticlesList.get(idx);
			selectedParticlesList.remove(idx);
		}
	
		
		return randomselectedParticles;
	}
	
}
