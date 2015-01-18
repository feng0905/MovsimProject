package SubStatePF;
import java.util.Vector;
import smc.*;
public class DataAssociation {
	Vector<AbstractState> vecSubState;
	AbstractState.AbstractMeasurement RealMeasurements;
	
	
	public DataAssociation(){
	
	}
	
	public void AssignMeasurment(Vector<AbstractState> _vecSubState,AbstractState.AbstractMeasurement _RealMeasurements){
		this.vecSubState=_vecSubState;
		this.RealMeasurements=_RealMeasurements;
	}
}
