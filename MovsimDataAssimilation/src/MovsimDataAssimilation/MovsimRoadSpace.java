package MovsimDataAssimilation;


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
}
