package SubStatePF;
import smc.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import MovsimDataAssimilation.MovsimRoadSpace;
import MovsimDataAssimilation.MovsimSpace;
import MovsimDataAssimilation.MovsimSystem;
import dataAssimilationFramework.SpatialTemporalSystem.SystemState;
import movsimSMC.*;

public  class GenerateSubStates {
	
	private int subStateNumber=4;
	
	public int getSubStateNumber(){
		return this.subStateNumber;
	}

	public Vector<AbstractState> DivideState(AbstractState fullState){
		Vector<AbstractState> vecSubStates= new Vector<>();
		//change this method so it can divide the system space according to how many roads there are. 
		try {
				
				MovsimSystem sys = ((SystemState<MovsimSystem>) fullState).getSimSystem();
				List<MovsimRoadSpace> listRods = ((MovsimSpace)sys.getSpace()).getRoadList();
				
				for (int i=0;i<listRods.size();i++) {
					SystemState<MovsimSystem> subState = (SystemState<MovsimSystem>) fullState.clone();
					List<MovsimRoadSpace> subRoadSpace = new ArrayList<MovsimRoadSpace>();
					subRoadSpace.add(listRods.get(i));
					subState.getSimSystem().updateSystemSpace(subRoadSpace);
					vecSubStates.add(subState);
				}
				
			} catch (CloneNotSupportedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			

		return vecSubStates;
	}

	public AbstractState formFullState(AbstractState[] subStates) throws JAXBException, SAXException{
		//needs to be overridden
		AbstractState fullState=null;
		final int size = subStates.length;		// Try combining all substates
		MovsimSystem systems[]  = new MovsimSystem [size]; 
		for (int i = 0; i < size; i++) {
			try {
				systems[i] = (MovsimSystem) ((SystemState<MovsimSystem>)subStates[i]).getSimSystem().clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		MovsimSystem fulSystem = MovsimSystem.MovsimSystem(systems);
		return  new SystemState<MovsimSystem>(fulSystem);
		
	}
}
