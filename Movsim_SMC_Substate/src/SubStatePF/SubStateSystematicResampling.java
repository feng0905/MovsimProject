package SubStatePF;

import smc.*;
import smc.resamplingStrategies.*;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;



public class SubStateSystematicResampling extends SystematicResampling{
	public  Vector<Particle>  ParticleCombination(Vector<Vector<Particle>> vecSubParticleSet){
		Vector<Particle> vecCombinedParticleSet=new Vector<Particle>();
	 
		int N=vecSubParticleSet.get(0).size();
		for(int j=0;j<N;j++){
			Particle p=new Particle(null, null);
			try {
				vecCombinedParticleSet.add(new Particle( (AbstractState)p.state.clone(), BigDecimal.valueOf(1.0/N) ));
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(int i=0;i<vecSubParticleSet.size();i++){
			int randomSelectList[]=RandomSelectParticles(N);
			
			for(int j=0;j<N;j++){
				Particle p= vecCombinedParticleSet.get(j);
				((AbstractFullState)(p.state)).setSubState(i, vecSubParticleSet.get(i).get(randomSelectList[j]).state);
			}
			
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
