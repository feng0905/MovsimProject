package MovsimDataAssimilation;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import dataAssimilationFramework.SystemFactoryInterface;

public class MovsimSystemFactory implements SystemFactoryInterface<MovsimSystem>, Cloneable {
	@Override
	public MovsimSystem createRealSystem() {
		// TODO Auto-generated method stub
		MovsimSystem sim = null;
		try {
			sim = new MovsimSystem();
			sim = (MovsimSystem) sim.clone();
			sim.createObstacle(23, 2, 2);
			// sim.getMovSimWrap().redistributeClone();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sim;
	}

	@Override
	public MovsimSystem createSimulatedSystem() {
		// TODO Auto-generated method stub
		MovsimSystem sim = null;
		try {
			sim = new MovsimSystem();
			sim = (MovsimSystem) sim.clone();
			// sim.getMovSimWrap().redistributeClone();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sim;

	}
}
