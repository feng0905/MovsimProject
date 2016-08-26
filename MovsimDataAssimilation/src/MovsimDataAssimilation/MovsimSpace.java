package MovsimDataAssimilation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;

import MovsimDataAssimilation.XML.XmlMovsimRoadSpace;
import smc.AbstractMeasurement;
import dataAssimilationFramework.SpatialTemporalSystem.AbstractSpace;

public class MovsimSpace extends AbstractSpace {
	
	List<MovsimRoadSpace>	roadList = new ArrayList<MovsimRoadSpace>();
	
	/**
	 * Create MovsimRoadSpace from entire roadNetwork
	 * @param roadNetwork
	 */
	public MovsimSpace(RoadNetwork roadNetwork) {
		List<RoadSegment> roadSegments = roadNetwork.getRoadSegments();
		for (int i = 0; i < roadSegments.size(); i++) {
			if (roadList == null) {
				roadList = new ArrayList<MovsimRoadSpace>();
			}
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
	
	public MovsimSpace(String filename) {
		XmlMovsimRoadSpace space = null;
    	JAXBContext context;
		try {
			context = JAXBContext.newInstance(XmlMovsimRoadSpace.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			space = (XmlMovsimRoadSpace) unmarshaller.unmarshal(new File(filename));
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < space.getRoadList().size(); i++) {
			if (roadList == null) {
				roadList = new ArrayList<MovsimRoadSpace>();
			}
			roadList.add(new MovsimRoadSpace(space.getRoadList().get(i).getID(), space.getRoadList().get(i).getStart(), space.getRoadList().get(i).getEnd()));
		}
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

	public MovsimSpace unionSpace(MovsimSpace spaceB) { 
		List<MovsimRoadSpace> unionRoads = new ArrayList<MovsimRoadSpace>();
		List<MovsimRoadSpace> roadlistA = this.getRoadList();
		List<MovsimRoadSpace> roadlistB = spaceB.getRoadList();
		if (roadlistA.size() == 1 && roadlistB.size() == 1) {
			return new MovsimSpace(roadlistA.get(0).union(roadlistB.get(0)));
		}
		unionRoads.addAll(roadlistA);
		roadlistA.addAll(roadlistB);
		for (int i = 0; i < roadlistA.size(); i++) {
			for (int j = 0; j < roadlistA.size(); i++) {
				MovsimRoadSpace A = roadlistA.get(i);
				MovsimRoadSpace B = roadlistB.get(j);
				if (A.intersection(B) == null) {
					
				}
			}
		}
		
		return new MovsimSpace(unionRoads);
	}
	
}
