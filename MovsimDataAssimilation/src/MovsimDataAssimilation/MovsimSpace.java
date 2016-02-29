package MovsimDataAssimilation;

import java.util.List;

import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;

import smc.AbstractMeasurement;
import dataAssimilationFramework.AbstractSpace;

public class MovsimSpace extends AbstractSpace {
	
	List<MovsimRoadSpace>	roadList;
	
	/**
	 * Create MovsimRoadSpace from entire roadNetwork
	 * @param roadNetwork
	 */
	public MovsimSpace(RoadNetwork roadNetwork) {
		List<RoadSegment> roadSegments = roadNetwork.getRoadSegments();
		for (int i = 0; i < roadSegments.size(); i++) {
			roadList.add(new MovsimRoadSpace(roadSegments.get(i).id(), 0, roadSegments.get(i).roadLength()));
		}
	}
	/**
	 * Construct using a list of MovsimRoadSpace
	 * @param roadlist
	 */
	public MovsimSpace(List<MovsimRoadSpace> roadlist) {
		this.roadList = roadlist;
	}
	
	
	
	/**
	 * @return the roadList
	 */
	public List<MovsimRoadSpace> getRoadList() {
		return roadList;
	}
	@Override
	public AbstractMeasurement getMeasurement() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
