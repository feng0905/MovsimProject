package SubStatePF;
import smc.*;

import java.util.Vector;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import Movsim_SubState.*;
import movSimSMC.MovSimState;
import movsimSMC.*;

public  class GenerateSubStates {

	public Vector<AbstractState> DivideState(AbstractState fullState){
		Vector<AbstractState> vecSubStates= new Vector<>();
		//needs to be overridden
		final int size = 4;		// currently set the size to be constant
		for (int i=0;i<4;i++) {
			MovSimState subState = ((MovSimState) fullState).clone();
			vecSubStates.add((AbstractState)subState);
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
			fullState = (AbstractState)  new MovSimState( MovsimWrap.combineMovsim(wraps));
		} catch (JAXBException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fullState;
	}
}
