package MovsimDataAssimilation.XML;

import java.awt.print.Printable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement( name = "MovsimRoadSpaceDefinition" )
public class XmlMovsimRoadSpace {
	private List<XmlMovsimRoad> roadList;
	@XmlElement (name = "XmlMovsimRoad")
	/**
	 * @return the roadList
	 */
	public List<XmlMovsimRoad> getRoadList() {
		return roadList;
	}
	/**
	 * @param roadList the roadList to set
	 */
	public void setRoadList(List<XmlMovsimRoad> roadList) {
		this.roadList = roadList;
	}

	public void add (XmlMovsimRoad road) {
		if (this.roadList == null) {
			this.roadList = new ArrayList<XmlMovsimRoad>();
		}
		roadList.add(road);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XmlMovsimRoadSpace space = null;
    	JAXBContext context;
		try {
			context = JAXBContext.newInstance(XmlMovsimRoadSpace.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			space = (XmlMovsimRoadSpace) unmarshaller.unmarshal(new File("MovsimSpaceDef.xml"));
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
