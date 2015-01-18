package Movsim_SubState;

import java.math.BigDecimal;

import smc.AbstractState;
import SubStatePF.AbstractFullState;

public class MovSim_FullState extends AbstractFullState {

	public MovSim_FullState() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setDescription(String des) {
		// TODO Auto-generated method stub

	}

	@Override
	public AbstractState transitionFunction()
			throws StateFunctionNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractState transitionModel(
			AbstractTransitionRandomComponent random)
			throws StateFunctionNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal transitionPdf(AbstractState nextState)
			throws StateFunctionNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractMeasurement measurementFunction()
			throws StateFunctionNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractMeasurement measurementModel(
			AbstractMeasurementRandomComponent random)
			throws StateFunctionNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal measurementPdf(AbstractMeasurement measurement)
			throws StateFunctionNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal proposalPdf(AbstractMeasurement measurement)
			throws StateFunctionNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractState generateNoisedState()
			throws StateFunctionNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractState propose(AbstractMeasurement measurement)
			throws StateFunctionNotSupportedException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractTransitionRandomComponent drawNextRandomComponentSample() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double distance(AbstractState sample) {
		// TODO Auto-generated method stub
		return 0;
	}

}
