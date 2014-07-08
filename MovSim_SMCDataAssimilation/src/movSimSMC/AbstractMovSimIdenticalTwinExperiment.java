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
import movsimSMC.Paint.SmcSimulationCanvas;

import org.xml.sax.SAXException;

import smc.AbstractState;
import smc.Particle;
import identicalTwinExperiments.AbstractIdenticalTwinExperiment;

public abstract class AbstractMovSimIdenticalTwinExperiment extends AbstractIdenticalTwinExperiment
{
	
	int stepLength = 10; // unit: seconds
	
	public AbstractMovSimIdenticalTwinExperiment(int stepLength) { this.stepLength = stepLength; }
	
	@Override
	// create and return the simulated system
	protected AbstractState createSimulatedSystem()
	{
		MovSimState sim = null;
		try {
			sim = new MovSimState(stepLength);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		return sim;
	}

	@Override
	
	// create and return the real system
	public AbstractState createRealSystem()
	{
		MovSimState sim = null;
		try {
			sim = new MovSimState(stepLength);
			sim.createObstacle(250, 3, 2);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return sim;
	}

	// record/display results
	int reportTime = 0; // the time, after it results will be recorded
	boolean reportFigure = GlobalConstants.SHOW_FIG; // the flag indicating if display a figure
	boolean reportError = true; // the flag indicating if record errors
	
	static class MovSimSMCResult{
		public double currentTime;  // the current time
		public double simError; // the distance between real system and simulated system
		public double bestParticleError;
		public double filteredError; // the distance from real system and the best particle
		
		List<List<Double>> segmentDensities = new ArrayList<List<Double>>(); // the density on each segment, 0-real, 1-sim, 2-best particle
		List<List<Double>> segmentAvgSpeeds = new ArrayList<List<Double>>(); // the speed on each segment, 0-real, 1-sim, 2-best particle
	}
	List<MovSimSMCResult> expResults = new ArrayList<MovSimSMCResult>(); // the container containing results
	
	@Override
	protected void reportOnStep(int step) throws Exception
	{	
		//System.out.println("Reporting results for step-" + step);
		// the real, sim, and filtered system
		MovsimWrap realSys = ((MovSimState)this.realSystem).getMovSimWrap(); // the real MovsimWrap object
		MovsimWrap simSys = ((MovSimState)this.simulatedSystem).getMovSimWrap(); // the simulated MovsimWrap object
		MovsimWrap bestParticleSys = ((MovSimState) particleSystem.getHighestWeightParticle().state).getMovSimWrap();
		
		Vector<Particle> particleSet = this.particleSystem.getParticleSet(); // the particles 
		MovsimWrap[] movSimParticleSystems = new MovsimWrap[particleSet.size()]; // the systems on particles
		for (int i = 0; i < movSimParticleSystems.length; i++)
			movSimParticleSystems[i] = ((MovSimState) particleSet.elementAt(i).state).getMovSimWrap();

		
		// record results
		MovSimSMCResult result = new MovSimSMCResult();// create a MovSimSMCResult
		// put result into "result"
		result.currentTime = step * stepLength;
		result.simError = realSys.CalDensityDistance(simSys, 0);
		result.bestParticleError = realSys.CalDensityDistance(bestParticleSys, 0);
		result.filteredError = 0;
		for( Particle p : this.particleSystem.getParticleSet()){
			MovsimWrap pSys = ((MovSimState)p.state).getMovSimWrap();
			result.filteredError += realSys.CalDensityDistance(pSys, 0) * p.weight.doubleValue();
		}
		
		result.segmentAvgSpeeds.add(realSys.getAvgSpeeds());
		result.segmentAvgSpeeds.add(simSys.getAvgSpeeds());
		result.segmentAvgSpeeds.add(bestParticleSys.getAvgSpeeds());
		result.segmentDensities.add(realSys.getSegmentDensities());
		result.segmentDensities.add(simSys.getSegmentDensities());
		result.segmentDensities.add(bestParticleSys.getSegmentDensities());
		expResults.add(result);
		
		// print particle weights
		int currentTime = stepLength * step;
		System.out.print("SMC =============================== Step" + step + " done! Current time = " + currentTime + " Sim-Error: " + String.format("%3.4f", result.simError) + " Fil-Error: " + String.format("%3.4f", result.filteredError) );
		System.out.println(" Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");
		
		if (currentTime > reportTime)
		{
			// if reportFigure : Draw accident map 
			if (reportFigure && (step==1 || step==4 || step==7 || step==10 ))
			{
				// display the results (draw the accident map)
				// assume this call is after a re-samling, i.e. the weights on all particles are equal
				// Peisheng will implement it
				ObstacleCanvas obstacleCanvas = new ObstacleCanvas(new ArrayList<MovsimWrap>(Arrays.asList(movSimParticleSystems)),"Obstacle Canvas, step "+ step+ " time " + step*stepLength);
				obstacleCanvas.addRealObstacle(realSys);
				new SmcSimulationCanvas(realSys,"Real System, step "+step+ " time " + step*stepLength + " simulatd time" + realSys.getSimulationTime());
				new SmcSimulationCanvas(simSys,"Simulated System, step " +step+ " time " + step*stepLength + " simulatd time" + simSys.getSimulationTime());
				new SmcSimulationCanvas(bestParticleSys, "Filtered System, step "+step+ " time " + step*stepLength + " simulatd time" + bestParticleSys.getSimulationTime());
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
					writer.println(this.particleSystem.getClass());
					writer.println( " N=" + this.particleSystem.getParticleSet().size() + " Seed=" + GlobalConstants.RANDOM_SEED);
					writer.println( " TransitionAccRate=" + GlobalConstants.TRANSITION_ACCIDENT_RATE);
					
					writer.println( "Time\tSimulated Error\tBest Particle Error\tFiltered Error");
					for( MovSimSMCResult r : this.expResults)
						writer.println(r.currentTime + "\t" + String.format("%2.4f", r.simError) + "\t" + String.format("%2.4f", r.bestParticleError)+ "\t" + String.format("%2.4f", r.filteredError));
					
					writer.close();
					System.out.println("Saved numeric results.");
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
		//System.out.println("Reported results for step-" + step);
	}

}
