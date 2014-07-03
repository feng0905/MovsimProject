package movSimSMC.test;

import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import smc.AbstractState.StateFunctionNotSupportedException;
import movSimSMC.MovSimState;
import movsimSMC.MovsimWrap;
import movsimSMC.Paint.ObstacleCanvas;

public class TestAccidentMap {

public static void main(String[] args) throws JAXBException, SAXException, StateFunctionNotSupportedException{
		
		/*
		 * Accident display Test 
		 */
		ArrayList<MovsimWrap> list = new ArrayList<MovsimWrap>();
		for (int i = 1; i < 10; i++) {
			MovSimState sim = new MovSimState(10);
			//list.add(sim);
			sim = (MovSimState) sim.transitionFunction();
			sim.getMovSimWrap().placeRandomObstacle();
			list.add(sim.getMovSimWrap());
		}

		new ObstacleCanvas(list);
	}

}
