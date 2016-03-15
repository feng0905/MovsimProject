package MovsimDataAssimilation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;


public class MovsimRoadSpace {
	private int roadId;
	/**
	 * @return the roadId
	 */
	public int getRoadId() {
		return roadId;
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

	private double start;
	private double end;
	
	public MovsimRoadSpace(int id,double start, double end) {
		this.roadId = id;
		this.start = start;
		this.end = end;
	}
	
	public MovsimRoadSpace intersection(MovsimRoadSpace space) {
		if (this.roadId != space.getRoadId()) {
			return null;
		}
		if (this.start > space.getEnd() || this.end < space.getStart()) {
			return null;
		}
		double newstart = this.start > space.getStart() ? this.start:space.getStart();
		double newend = this.end > space.getEnd() ? space.getEnd():this.end;
		
		return new MovsimRoadSpace(roadId, newstart, newend);
	}
	
	public List<MovsimRoadSpace> union(MovsimRoadSpace space) {
		List<MovsimRoadSpace> union = new ArrayList<MovsimRoadSpace>();
		
		// if these are two different road space
		if (this.roadId != space.getRoadId()) {
			union.add(this);
			union.add(space);
		}
		// if same road without intersection
		else if (this.start > space.getEnd() || this.end < space.getStart()) {
			union.add(this);
			union.add(space);
		}
		// if they intersects each other
		else {
			double newstart = this.start < space.getStart() ? this.start:space.getStart();
			double newend = this.end < space.getEnd() ? space.getEnd():this.end;
			union.add(new MovsimRoadSpace(roadId, newstart, newend));
		}
		return union;
	}
	
	public List<MovsimRoadSpace> union(List<MovsimRoadSpace> raodlist) {
		// assume there is no same road in the list.
		List<MovsimRoadSpace> union = new ArrayList<MovsimRoadSpace>();
		union.add(this);
		
		for (Iterator iterator = raodlist.iterator(); iterator.hasNext();) {
			MovsimRoadSpace movsimRoadSpace = (MovsimRoadSpace) iterator.next();
			if (this.intersection(movsimRoadSpace) == null) {
				// if this space doesn't intersect the other, add the other in
				union.add(movsimRoadSpace);
			}
			else {
				// if not, then the union will only contains one space, and should add it. 
				union.add(this.union(movsimRoadSpace).get(0));
				union.remove(0);
			}
		}
		return raodlist;
	}
}
