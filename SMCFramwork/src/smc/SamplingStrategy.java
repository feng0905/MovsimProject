package smc;

public abstract class SamplingStrategy
{ 
	/**
	 * 
	 * @param currentState
	 * @param measurement
	 * @return
	 */
	public abstract AbstractState sampling(AbstractState currentState,  AbstractState.AbstractMeasurement measurement);

}
