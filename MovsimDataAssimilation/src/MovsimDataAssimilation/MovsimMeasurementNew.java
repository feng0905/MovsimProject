package MovsimDataAssimilation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import movsimSMC.MovSimSensor;
import movsimSMC.MovSimSensor2;
import smc.AbstractMeasurement;

public class MovsimMeasurementNew extends MovsimMeasurement{
	//This new movsim measurement class aims to calculate the observation when system space is part of the entire space 
	List<MovsimRoadSpace>	refinedRoadSpace;
	public MovsimMeasurementNew(List<MovSimSensor> sensors,
			MovsimSpace movsimSpace) {
		super(sensors, movsimSpace);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public BigDecimal weightUpdate(AbstractMeasurement measurement) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see testFramework.MovsimMeasurement#singleSensorNormlizedDistance(movsimSMC.MovSimSensor, movsimSMC.MovSimSensor)
	 */
	@Override
	protected double singleSensorNormlizedDistance(MovSimSensor ss1,
			MovSimSensor ss2) {
		// TODO Auto-generated method stub
		return super.singleSensorNormlizedDistance(ss1, ss2);
	}
	
	private void refineMovsimRoadSpace() {		
		
		for (Iterator<MovSimSensor> iter = sensors.iterator(); iter.hasNext() ; ) {
			MovSimSensor2 sensor = (MovSimSensor2) iter.next() ;
			List<MovsimRoadSpace> roadList = movsimSpace.getRoadList();
			//using this flag to indicate a match of sensor and road
			boolean match = false;
			for (int j = 0; j < roadList.size() ; j++) {
				MovsimRoadSpace road = roadList.get(j);
				if (sensor.getRoadID() == road.getRoadId()) {
					MovsimRoadSpace refinedRoad = road.intersection(new MovsimRoadSpace(sensor.getRoadID(), sensor.getDeployedPosLeft(), sensor.getDeployedPosRight()));
					if (refinedRoad != null) {
						addRefinedRoad(refinedRoad);
						match = true;
						break;
					}
				}
			}
			// if couldn't find a match, remove the sensor from the list
			if (!match) {
				iter.remove();
			}
		}
	}

	private void addRefinedRoad(MovsimRoadSpace refinedRoad) {
		if (refinedRoadSpace == null) {
			refinedRoadSpace = new ArrayList<MovsimRoadSpace>();
		}
		refinedRoadSpace.add(refinedRoad);
	}
}
