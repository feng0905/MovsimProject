package Movsim_SubState;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import movSimSMC.MovSimState;
import movsimSMC.MovsimWrap;

public class MovSimFullState extends MovSimState {

	@Override
	public MovSimFullState clone() {
		// TODO Auto-generated method stub
		// MovSimFullState fullState = (MovSimFullState)super.clone();
		return (MovSimFullState)super.clone();
	} 

	public MovSimFullState(double stepLength ) throws JAXBException, SAXException {
		super(stepLength);
	}
	
	public MovSimFullState(MovsimWrap movsimPF) {
		super(movsimPF);
		// TODO Auto-generated constructor stub
	}
 
}
