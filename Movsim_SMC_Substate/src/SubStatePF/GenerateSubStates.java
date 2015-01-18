package SubStatePF;
import smc.*;
import java.util.Vector;

public   class GenerateSubStates {
	public Vector<AbstractState> DivideState(AbstractState fullState){
		//needs to be overridden
		Vector<AbstractState> vecSubStates=null;
		return vecSubStates;
	}
	public AbstractState formFullState(AbstractState[] subStates){
		//needs to be overridden
		AbstractState fullState=null;
		return fullState;
	}
}
