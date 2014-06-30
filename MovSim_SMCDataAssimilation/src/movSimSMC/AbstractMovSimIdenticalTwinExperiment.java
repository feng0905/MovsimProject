package movSimSMC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import smc.AbstractParticleSystem;
import smc.AbstractState;
import smc.Particle;
import identicalTwinExperiments.AbstractIdenticalTwinExperiment;

public abstract class AbstractMovSimIdenticalTwinExperiment extends AbstractIdenticalTwinExperiment
{
	
	int stepLength = 3600; // 1 hour
	
	@Override
	protected AbstractState createSimulatedSystem()
	{
		// TODO Auto-generated method stub
		MovSimState sim = null;
		try {
			sim = new MovSimState();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sim.setSimStep(10);
		
		
		
		return sim;
	}

	@Override
	protected AbstractState createRealSystem()
	{
		// TODO Auto-generated method stub
		MovSimState sim = null;
		try {
			sim = new MovSimState();
			sim.setSimStep(10);
			sim.createObstacle(30, 2, 2);
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sim;
	}

	// record/display results
	int reportTime = 0; // the time, after it results will be recorded
	boolean reportFigure = true; // the flag indicating if display a figure
	boolean reportError = true; // the flag indicating if record errors
	
	static class MovSimSMCResult{
		public double currentTime;  // the current time
		public double simError; // the distance between real system and simulated system
		public double filteredError; // the distance from real system and the best particle
		
		List<List<Double>> segmentDensities; // the density on each segment, 0-real, 1-sim, 2-best particle
		List<List<Double>> segmentAvgSpeeds; // the speed on each segment, 0-real, 1-sim, 2-best particle
	}
	List<MovSimSMCResult> expResults = new ArrayList<>(); // the container containing results
	
	@Override
	protected void reportOnStep(int step) throws Exception
	{	
		// record results
		// record the numberical results
		
		// print particle weights
		int currentTime = stepLength * step;
		System.out.print("SMC =============================== Step" + step + " done! Current time = " + currentTime + " Error: " + "TBD");
		System.out.println(" Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");
		
		if (currentTime > reportTime)
		{
			// if reportFigure : Draw accident map 
			if (reportFigure)
			{
				// display the results (draw the accident map)
				// Peisheng will implement it
			}
			
			if(reportError)
			{
				System.out.println("Saving numeric results.");
				String resultFolder = "results";
				File folder = new File(resultFolder);
				if (!folder.exists()) folder.mkdir();
				String filePath = resultFolder+ "/NumbericResults.txt";
				
				// Create file, if not existing
				File resultFile = new File(filePath);
				if (!resultFile.exists())
					try
					{
						resultFile.createNewFile();
					}
					catch (IOException e)
					{
						System.err.println("Failed to create file: " + filePath);
						System.exit(1); 
					}

				// Create PrintWriter
				PrintWriter writer = null;
				try
				{
					writer = new PrintWriter(resultFile);
					
					// write error using  writer
					// Peisheng will implement it
					
					writer.close();
					System.out.println("Saved numeric results.");
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

}
