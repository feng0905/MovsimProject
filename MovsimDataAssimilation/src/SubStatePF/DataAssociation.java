package SubStatePF;
import java.util.List;
import java.util.Vector;

import smc.*;
public class DataAssociation {
	Vector<AbstractState> vecSubState;
	AbstractMeasurement RealMeasurements;
	// List<MovSimSensor>
	
	public DataAssociation(){
	
	}
	
	public void AssignMeasurment(Vector<AbstractState> _vecSubState, AbstractMeasurement _RealMeasurements){
		this.vecSubState=_vecSubState;
		this.RealMeasurements=_RealMeasurements;
	}
}
