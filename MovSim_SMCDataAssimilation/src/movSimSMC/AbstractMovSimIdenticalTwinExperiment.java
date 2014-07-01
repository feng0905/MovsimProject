package movSimSMC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBException;

import movsimSMC.MovsimWrap;
import movsimSMC.Paint.ObstacleCanvas;

import org.xml.sax.SAXException;

import smc.AbstractState;
import smc.Particle;
import identicalTwinExperiments.AbstractIdenticalTwinExperiment;

public abstract class AbstractMovSimIdenticalTwinExperiment extends AbstractIdenticalTwinExperiment
{
	
	int stepLength = 3600; // 1 hour
	
	@Override
	// create and return the simulated system
	protected AbstractState createSimulatedSystem()
	{
		MovSimState sim = null;
		try {
			sim = new MovSimState();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		sim.setSimStep(10);
		
		
		
		return sim;
	}

	@Override
	// create and return the real system
	protected AbstractState createRealSystem()
	{
		MovSimState sim = null;
		try {
			sim = new MovSimState();
			sim.setSimStep(10);
			sim.createObstacle(30, 2, 2);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
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
		// the real, sim, and filtered system
		MovsimWrap realSys = ((MovSimState)this.realSystem).getMovSimWrap(); // the real MovsimWrap object
		MovsimWrap simSys = ((MovSimState)this.simulatedSystem).getMovSimWrap(); // the simulated MovsimWrap object
		MovsimWrap filteredSys = ((MovSimState) particleSystem.getHighestWeightParticle().state).getMovSimWrap();
		
		Vector<Particle> particleSet = this.particleSystem.getParticleSet(); // the particles 
		MovsimWrap[] movSimParticleSystems = new MovsimWrap[particleSet.size()]; // the systems on particles
		for (int i = 0; i < movSimParticleSystems.length; i++)
			movSimParticleSystems[i] = ((MovSimState) particleSet.elementAt(i).state).getMovSimWrap();

		
		// record results
		MovSimSMCResult result = new MovSimSMCResult();// create a MovSimSMCResult
		// put result into "result"
		result.currentTime = step * stepLength;
		result.simError = realSys.CalDensityDistance(simSys,0);
		result.filteredError = realSys.CalDensityDistance(filteredSys, 0);
		result.segmentAvgSpeeds.add(realSys.getAvgSpeeds());
		result.segmentAvgSpeeds.add(simSys.getAvgSpeeds());
		result.segmentAvgSpeeds.add(filteredSys.getAvgSpeeds());
		result.segmentDensities.add(realSys.getSegmentDensities());
		result.segmentDensities.add(simSys.getSegmentDensities());
		result.segmentDensities.add(filteredSys.getSegmentDensities());
		
		//result.segmentD
		
		// Peisheng ...
		// ...
		// add the result in
		expResults.add(result);
		
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
				// assume this call is after a re-samling, i.e. the weights on all particles are equal
				// Peisheng will implement it
				new ObstacleCanvas(new ArrayList<>(Arrays.asList(movSimParticleSystems)));
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
					writer.println( "time\tSim Error\tFiltered Error");
					for( MovSimSMCResult r : this.expResults)
						writer.println(r.currentTime + "\t" + r.simError + "\t" + r.filteredError);
					
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
