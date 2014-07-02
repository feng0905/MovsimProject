package movSimSMC;


import smc.AbstractState.AbstractTransitionRandomComponent;

public class MovSimRandomComponent extends AbstractTransitionRandomComponent {

	double random = 0; 
	public MovSimRandomComponent(double rand) {
		// TODO Auto-generated constructor stub
		random = rand;
	}
	
	public double getRandom(){
		return random;
	}
}
