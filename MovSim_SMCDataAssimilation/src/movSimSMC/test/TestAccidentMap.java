package movSimSMC.test;

import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import smc.AbstractState.StateFunctionNotSupportedException;

public class TestAccidentMap {

public static void main(String[] args) throws JAXBException, SAXException, StateFunctionNotSupportedException{
		
		/*
		 * Accident display Test 
		 */
		/*ArrayList<MovsimWrap> list = new ArrayList<MovsimWrap>();
		for (int i = 1; i < 40; i++) {
			MovSimState sim = new MovSimState(10);
			//list.add(sim);
			sim = (MovSimState) sim.transitionFunction();
			sim.getMovSimWrap().placeRandomObstacle(GlobalConstants.G_RAND);
			list.add(sim.getMovSimWrap());
		}

		new ObstacleCanvas(list);*/
	
		System.out.println(String.format("%5.4f", 0.05));
	}

}
