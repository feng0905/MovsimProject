package MovsimDataAssimilation.XML;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Peisheng
 *
 */
@XmlRootElement( name = "XmlMovsimRoad" )
public class XmlMovsimRoad {
	
	private int roadId;	
	@XmlAttribute( name = "Id", required = true)
	public void setID(int id) {
		roadId = id;
	}
	/**
	 * @return the roadId
	 */
	public int getID() {
		return roadId;
	}
		
	private double start;
	/**
	 * @return the start position
	 */
	public double getStart() {
		return start;
	}
	@XmlAttribute( name = "StartPosition", required = true)
	public void setStart(double start) {
		this.start = start;
	}
		
	private double end;
	/**
	 * @return the end position
	 */
	public double getEnd() {
		return end;
	}
	@XmlAttribute( name = "EndPosition", required = true)
	public void setEnd(double end) {
		this.end = end;
	}
	
	 public static void main(String[] args) {
		 XmlMovsimRoad road = new XmlMovsimRoad();
		 road.setID(1);
		 road.setStart(0);
		 road.setEnd(1200);
		 
		 try {
			JAXBContext jaxbContext = JAXBContext.newInstance(XmlMovsimRoad.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,  true);
			jaxbMarshaller.marshal(road, new File("MovsimSpaceDef.xml"));
			jaxbMarshaller.marshal(road, System.out);
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
}
