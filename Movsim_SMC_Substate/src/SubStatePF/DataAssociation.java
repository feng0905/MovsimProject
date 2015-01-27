package SubStatePF;
import java.util.List;
import java.util.Vector;

import org.movsim.simulator.roadnetwork.MovSimSensor;

import smc.*;
public class DataAssociation {
	Vector<AbstractState> vecSubState;
	AbstractState.AbstractMeasurement RealMeasurements;
	// List<MovSimSensor>
	
	public DataAssociation(){
	
	}
	
	public void AssignMeasurment(Vector<AbstractState> _vecSubState,AbstractState.AbstractMeasurement _RealMeasurements){
		this.vecSubState=_vecSubState;
		this.RealMeasurements=_RealMeasurements;
	}
}
