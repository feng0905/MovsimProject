package SubStatePF;
import smc.*;

import java.util.Vector;

import Movsim_SubState.*;
import Movsim_SubState.MovSimSubState;
public  class GenerateSubStates {

	public Vector<AbstractState> DivideState(AbstractState fullState){
		Vector<AbstractState> vecSubStates= new Vector<>();
		//needs to be overridden
		final int size = 4;		// currently set the size to be constant
		for (int i=0;i<4;i++) {
			MovSimSubState subState = new MovSimSubState( ((MovSimFullState) fullState).clone(),i);
			vecSubStates.add((AbstractState)subState);
		}	
		
		return vecSubStates;
	}

	public AbstractState formFullState(AbstractState[] subStates){
		//needs to be overridden
		AbstractState fullState=null;
		final int size = subStates.length;		// currently set the size to be constant
		
		
		return fullState;
	}
}
