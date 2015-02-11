package movSimSMC;

import java.io.Serializable;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Lanes.LaneSection.Left;

// system area Movsim State: try to add the space property to movsim state. 
public class MovsimArea implements Cloneable {

	private int roadSeg; 
	private double start;
	private double end;
	public MovsimArea() {
		// TODO Auto-generated constructor stub
		roadSeg = -1; // indicate the area is the whole area of the simulator.
		start = 0;
		end = 0;
	}
	public MovsimArea(int road,double start,double end) {
		
		roadSeg = road;
		this.start = start;
		this.end = end;
	}
	/**
	 * @return the roadSeg
	 */
	public int getRoadSeg() {
		return roadSeg;
	}
	/**
	 * @return the start
	 */
	public double getStart() {
		return start;
	}
	/**
	 * @return the end
	 */
	public double getEnd() {
		return end;
	}
	
	
	// calculate how much portion the intersection covers the area (_area)   
	public double getIntersection(MovsimArea _area) {
		double intersec = 0;
		if (roadSeg == -1) {
			// if area1 is the whole space 
			intersec = 1.0;
		}
		else if (_area.getRoadSeg() == -1) {
			// define the maximum range of the area is 1000.
			System.out.println("This area cannot be the whole space");
			intersec = (end - start) / 1000;
		}
		else if (roadSeg == _area.getRoadSeg()){
			// if the road is the same, calculate the left and right of the intersection
			// then the ratio of inter/area2
			double interLeft, interRight;
			if (start > _area.getStart()) {
				interLeft = start;
			}
			else {
				interLeft = _area.getStart();
			}
			if (end < _area.getEnd()) {
				interRight = end;
			}
			else {
				interRight = _area.getEnd();
			}
			intersec = (interRight - interLeft) / (_area.getEnd() - _area.getStart());
		}
		return intersec;
	}
}
