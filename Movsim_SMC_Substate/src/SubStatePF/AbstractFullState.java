package SubStatePF;
import smc.*;
import java.util.Vector;

public abstract class AbstractFullState extends AbstractState{
	Vector<AbstractState>  vecSubStates;
	
	public void GenerateSubState(){
	//Generate substate list from a file
		
	}
	
	public void setSubState(int idx,AbstractState state){
	//Constitute a full state by multiple substates---change vecSubStates.
		this.vecSubStates.set(idx,  state);
	}
	
	
	
}
