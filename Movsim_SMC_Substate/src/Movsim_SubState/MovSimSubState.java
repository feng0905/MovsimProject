package Movsim_SubState;

import javax.xml.bind.JAXBException;

import movSimSMC.MovSimState;
import movsimSMC.MovsimWrap;

import org.xml.sax.SAXException;

public class MovSimSubState extends MovSimState{
	private final int subIndex;
	
	public MovSimSubState(double stepLength, int subindex) throws JAXBException, SAXException {
		super(stepLength);
		// TODO Auto-generated constructor stub
		subIndex = subindex;
	}
	
	public MovSimSubState(MovSimState fullState, int subindex) {
		super(fullState.getMovSimWrap());
		// TODO Auto-generated constructor stub
		subIndex = subindex;
	}
	
	@Override
	public MovSimSubState clone() {
		// TODO Auto-generated method stub
		// MovSimSubState sub = (MovSimSubState)super.clone();
		// subindex = this.subIndx;
		return (MovSimSubState)super.clone();
	}

	public MovSimSubState(MovsimWrap movsimPF, int subindex) {
		super(movsimPF);
		// TODO Auto-generated constructor stub
		subIndex = subindex;
	}
}
