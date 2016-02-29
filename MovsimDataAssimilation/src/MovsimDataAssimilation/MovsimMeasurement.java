package MovsimDataAssimilation;

import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.math3.distribution.NormalDistribution;
import movsimSMC.MovSimSensor;
import movsimSMC.MovSimSensor2;
import smc.AbstractMeasurement;

public class MovsimMeasurement extends AbstractMeasurement{
	protected MovsimSpace movsimSpace;
	protected List<MovSimSensor> sensors;
	
	public MovsimMeasurement( List<MovSimSensor> sensors ) { 
		this.sensors = sensors; 
		
	}
	public MovsimMeasurement( List<MovSimSensor> sensors, MovsimSpace movsimSpace ) { 
		this.sensors = sensors; 
		this.movsimSpace = movsimSpace;
	}
	
	public List<MovSimSensor> getSensorReading() {
		return sensors;
	}
	
	@Override
	public BigDecimal weightUpdate(AbstractMeasurement measurement) {
		// TODO Auto-generated method stub
		MovsimMeasurement m2 = (MovsimMeasurement) measurement;
		
		List<MovSimSensor> sensorReadings = ((MovsimMeasurement)measurement).sensors;
		List<MovSimSensor> simulatedSensorReadings = this.getSensorReading();
		// TODO Auto-generated method stub
		double sigma = 0.12; //original - 0.12;
		/*
		 * double variance = sigma*sigma;
		 * 
		 * double[][] cov = new double[sensorProfiles.length][sensorProfiles.length]; for(int i=0; i<sensorProfiles.length; i++) for(int j=0; j<sensorProfiles.length; j++) { if(i==j) cov[i][j] =
		 * variance; else cov[i][j] =0; }
		 * 
		 * MultivariateNormalDistribution mn = new MultivariateNormalDistribution(simTrueReadings, cov); BigDecimal weight = BigDecimal.valueOf(mn.density(sensorReadings));
		 */

		NormalDistribution norm = new NormalDistribution(0, sigma);
		BigDecimal weight = BigDecimal.ONE;
		
		for (int i = 0; i < sensorReadings.size(); i++)
		{
			//MovsimArea area = areaList.get(iArea);
			//if (area.getRoadSeg() == sensorReadings.get(i).getRoadID() 
			//		||
			//	area.getRoadSeg() == -1	) {
				
				double normDis = singleSensorNormlizedDistance(sensorReadings.get(i), simulatedSensorReadings.get(i));
				// normDis=normDis*20; //modified by yuan 2/13/15
				// max = max > normDis?max:normDis;
				double normResult = norm.density(normDis);
				double minNorm = 1E-300; // if not doing so, a small value will become 0, and mess up the weight
				if (normResult < minNorm) normResult = minNorm;
				
				//System.out.println("sensor-" + i + " norm dis=" + normDis + "-> L=" + normResult);
				//System.out.println();

				weight = weight.multiply(BigDecimal.valueOf(normResult));	
			//}
			
		}
		
		return weight;
	}
	
	protected double singleSensorNormlizedDistance( MovSimSensor ss1, MovSimSensor ss2 )
	{
		MovSimSensor2 s1 = (MovSimSensor2)ss1;
		MovSimSensor2 s2 = (MovSimSensor2)ss2;
		// normalize speed
		double norSpeedDiff = Math.abs(s1.getAvgSpeed() - s2.getAvgSpeed()) / (s1.getMaxSpeed() - s1.getMinSpeed()); 
		// normalize acceleration
		double norAccDiff = Math.abs(s1.getAvgAcc() - s2.getAvgAcc()) / 5; /*(s1.getMaxAcc() - s1.getMinAcc());*/
		// normalize vehicle number
		double norCarNumberDiff = Math.abs(s1.getVehNumber() - s2.getVehNumber()) / (double)(s1.getMaxVehNumber() - s1.getMinVehNumber());
		
		//System.out.println( "speed1=" + s1.getAvgSpeed() + ", acc1="+s1.getAvgAcc()+", carNumber1=" + s1.getVehNumber());
		//System.out.println( "speedD=" + norSpeedDiff + ", accD="+norAccDiff+", carNumberD=" + norCarNumberDiff);
		
		//System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
		// weights on factors
		double numberWeight = 0.5;
		double speedWeight = 0.3;
		double accWeight = 1 - numberWeight-speedWeight;
		return speedWeight*norSpeedDiff + accWeight*norAccDiff + numberWeight*norCarNumberDiff;
		
	}
}
