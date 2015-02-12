package SubStatePF;
import smc.*;

import java.util.Vector;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import Movsim_SubState.*;
import movSimSMC.MovSimState;
import movsimSMC.*;

public  class GenerateSubStates {
	
	private int subStateNumber=4;
	
	public int getSubStateNumber(){
		return this.subStateNumber;
	}

	public Vector<AbstractState> DivideState(AbstractState fullState){
		Vector<AbstractState> vecSubStates= new Vector<>();
		//needs to be overridden
		final int size = subStateNumber;		// currently set the size to be constant
		for (int i=0;i<subStateNumber;i++) {
			
			//MovSimSubState subState = ((MovSimSubState) fullState).clone();
			//vecSubStates.add((AbstractState)subState);
			
			
		try {
				MovSimSubState subState = new MovSimSubState( (MovSimState)fullState.clone(),i);
				
				vecSubStates.add((AbstractState) subState);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}	
		return vecSubStates;
	}

	public AbstractState formFullState(AbstractState[] subStates){
		//needs to be overridden
		AbstractState fullState=null;
		final int size = subStates.length;		// currently set the size to be constant
		MovsimWrap wraps[]  = new MovsimWrap [size]; 
		for (int i = 0; i < wraps.length; i++) {
			wraps[i] = ((MovSimState)subStates[i] ).getMovSimWrap();
		}
		try {
			fullState = (AbstractState)  new MovSimSubState( MovsimWrap.combineMovsim(wraps));
		} catch (JAXBException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fullState;
	}
}
