package SubStatePF;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import smc.*;
import smc.resamplingStrategies.SystematicResampling;


public class SubStateSystematicResampling extends SystematicResampling{
	public  Vector<Particle>  ParticleCombination(Vector<Vector<Particle>> vecSubParticleSet, GenerateSubStates subStateGenerator){
		Vector<Particle> vecCombinedParticleSet=new Vector<Particle>();
	 
		int N=vecSubParticleSet.get(0).size(); //N is the sample size
//		//initialization of new particle vector
//		for(int j=0;j<N;j++){
//			state
//			Particle p=new Particle(new AbstractState(), BigDecimal.valueOf(0));
//			try {
//				vecCombinedParticleSet.add(new Particle( (AbstractState)p.state.clone(), BigDecimal.valueOf(1.0/N) ));
//			} catch (CloneNotSupportedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		
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
			AbstractState fullState = null;
			try {
				fullState = subStateGenerator.formFullState(subStates);
			} catch (JAXBException | SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			vecCombinedParticleSet.add(new Particle( fullState, BigDecimal.valueOf(1.0/N) ));
			//vecCombinedParticleSet.get(j).state=fullState;
		}
			
		
		return vecCombinedParticleSet;
	}
	
	public int[] RandomSelectParticles(int[] selectedParticles){
		int[] randomselectedParticles=new int[selectedParticles.length];
		LinkedList<Integer> selectedParticlesList=new LinkedList<Integer>();
		System.out.println("Standard selected particles:");
	
		for (int i = 0; i < randomselectedParticles.length; i++) {
			selectedParticlesList.add(selectedParticles[i]);
			System.out.print(" "+selectedParticles[i]);
			
		}
		
		System.out.println();
		
	
		Random random=new Random(11111);
	
		for (int i = 0; i < randomselectedParticles.length; i++) {
			int idx=random.nextInt(selectedParticles.length-i);
			
			randomselectedParticles[i]=(Integer)selectedParticlesList.get(idx);
			selectedParticlesList.remove(idx);
			
			
		}
	
		
		return randomselectedParticles;
	}
	
	
	public int[] RandomSelectParticles(int n){
		int[] randomselectedParticles=new int[n];
		LinkedList<Integer> selectedParticlesList=new LinkedList<Integer>();
		
//		System.out.println("Standard selected particles:");
		for (int i = 0; i < randomselectedParticles.length; i++) {
			selectedParticlesList.add(i);
//			System.out.print(" "+i);
		}
//		System.out.println();
		
	
		Random random=GlobalConstants.RAND;
	
		for (int i = 0; i < randomselectedParticles.length; i++) {
			int idx=random.nextInt(n-i);
			
			randomselectedParticles[i]=(Integer)selectedParticlesList.get(idx);
			selectedParticlesList.remove(idx);
		}
		
		System.out.println("Randomly selected new particles:");
		for (int i = 0; i < randomselectedParticles.length; i++) {
			 
			System.out.print(" "+randomselectedParticles[i]);
			
		}
		System.out.println();
		
	
		
		return randomselectedParticles;
	}
	
}
