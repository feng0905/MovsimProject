package movSimSMC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.text.html.ListView;
import javax.xml.bind.JAXBException;

import junit.framework.Test;
import movsimSMC.MovsimWrap;
import movsimSMC.Paint.ObstacleCanvas;
import movsimSMC.Paint.SmcSimulationCanvas;

import org.xml.sax.SAXException;

import smc.AbstractParticleSystem;
import smc.AbstractState;
import smc.Particle;
import smc.AbstractState.StateFunctionNotSupportedException;
import identicalTwinExperiments.AbstractIdenticalTwinExperiment;


public abstract class AbstractMovSimIdenticalTwinExperiment extends AbstractIdenticalTwinExperiment
{
	
	protected int stepLength = 15; // unit: seconds
	protected int ParticleNumber = 30;	// add this variable to save multiple results.
	public AbstractMovSimIdenticalTwinExperiment(int stepLength) { this.stepLength = stepLength; }
	
	@Override
	// create and return the simulated system
	protected AbstractState createSimulatedSystem()
	{
		MovSimState sim = null;
		try {
			sim = new MovSimState(stepLength);
			sim = sim.clone();
			// sim.getMovSimWrap().redistributeClone();
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
			sim.setInititalState(false);
			// sim.setInititalState(false);
			// sim.createSelfRecoverObstacle(30, 1, 2, 80);
			sim.createObstacle(250, 2, 2);
		    // sim.createObstacle(25, 2, 1);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return sim;
	}



	// record/display results
	protected int reportTime = 0; // the time, after it results will be recorded
	protected boolean reportFigure = GlobalConstants.SHOW_FIG; // the flag indicating if display a figure
	protected boolean reportError = true; // the flag indicating if record errors
	
	public static class MovSimSMCResult{
		public double currentTime;  // the current time
		public double simError; // the distance between real system and simulated system
		public double bestParticleError; // the distance from real system and the best particle
		public double filteredError; 	// weighted average error of all particles 
		public double filteredAccidentError; // the distance of accident position.  
		
		
		public List<List<Double>> segmentDensities = new ArrayList<List<Double>>(); // the density on each segment, 0-real, 1-sim, 2-best particle
		public List<List<Double>> segmentAvgSpeeds = new ArrayList<List<Double>>(); // the speed on each segment, 0-real, 1-sim, 2-best particle
		public List<String> particleReportList = new ArrayList<String>();
	}
	protected List<MovSimSMCResult> expResults = new ArrayList<MovSimSMCResult>(); // the container containing results
	
	
	
	
	/* (non-Javadoc)
	 * @see identicalTwinExperiments.AbstractIdenticalTwinExperiment#createParticleSystem(java.util.Vector)
	 */
	@Override
	protected AbstractParticleSystem createParticleSystem(
			Vector<Particle> particleSet) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see identicalTwinExperiments.AbstractIdenticalTwinExperiment#runDataAssimilationExperiement(int, int)
	 */
	@Override
	public void runDataAssimilationExperiement(int stepNumber,
			int particleNumber) throws Exception {
		// TODO Auto-generated method stub
		
		//add assignment
		ParticleNumber = particleNumber;
		
		// Create the real system from its factory method
				this.realSystem = this.createRealSystem();
				realSystem.setDescription("t0_Real");
				
				System.out.println("SMC --------------  Real system created!!! ");
				//System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");
				
				// Create the simulated system from its factory method
				this.simulatedSystem = this.createSimulatedSystem();
				simulatedSystem.setDescription("t0_Sim");
				
				System.out.println("Simulated --------------  Sim system created!!! ");
				//System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");
				
				// Create the initial particle set from the simulated system factory method
				Vector<Particle> initialParticleSet = new Vector<Particle>();
				for(int i=0; i<particleNumber; i++)
				{
					AbstractState s=null;
					try
					{
						simulatedSystem.setInititalState(true);
						s = (AbstractState) simulatedSystem.clone();
					}
					catch (CloneNotSupportedException e)
					{
						e.printStackTrace();
						System.exit(1);
					}
					s.setDescription("t0_"+"Particle" + i);
					initialParticleSet.add(new Particle(s, BigDecimal.valueOf(1.0/particleNumber)));
					
					System.out.println("SMC -------------- Particle" + i + " created!!! ");
					System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");
				}
				
							
				// Peisheng edited, add initial state separation¡£
				simulatedSystem.setInititalState(false);
				for (Particle particle: initialParticleSet) {
					particle.state.setInititalState(false);
				}
				
				// Create the particle system from its factory method
				this.particleSystem = this.createParticleSystem(initialParticleSet);
				
				
				for( int t=1; t<=stepNumber; t++ )
				{
					System.out.println("SMC -------------- Step" + t + " started !!!!!!!!!!!! ");
					//System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");
					try
					{
						// The real system at time t
						realSystem = realSystem.transitionFunction();
						realSystem.setDescription("t"+t+"_"+"Real");
						
						System.out.println("SMC -------------- real sys finished");
						//System.gc();
						//System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");
						
						//The simulated system at time t
						simulatedSystem = simulatedSystem.transitionFunction();
						simulatedSystem.setDescription("t"+t+"_"+"Sim");
				
						System.out.println("SMC -------------- sim sys finished");
						//System.gc();
						//System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");
						
						// Measurement from the real system
						AbstractState.AbstractMeasurement measurement = realSystem.measurementFunction();
						System.out.println("SMC -------------- measurement finished");
						//System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");
						
						// Assimilate data to the particle system
						particleSystem.updateParticle(measurement); 
						particleSystem.setDescription("t"+t); // add a description for the state for each particle
						System.out.println("SMC -------------- particles finished");
						System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");
						
						
						// Report experiment results
						reportOnStep( t );
					}
					catch (StateFunctionNotSupportedException e)
					{
						e.printStackTrace();
					}
					
					
				}
	}

	@Override
	protected void reportOnStep(int step) throws Exception
	{	
		//System.out.println("Reporting results for step-" + step);
		// the real, sim, and filtered system
		MovsimWrap realSys = ((MovSimState)this.realSystem).getMovSimWrap(); // the real MovsimWrap object
		MovsimWrap simSys = ((MovSimState)this.simulatedSystem).getMovSimWrap(); // the simulated MovsimWrap object
		MovsimWrap bestParticleSys = ((MovSimState) particleSystem.getBestParticleBeforeResampling().state).getMovSimWrap();
		
		//added by yuan 2/12/2015
		MovsimWrap ParticleSys = ((MovSimState) particleSystem.getBestParticleBeforeResampling().state).getMovSimWrap();
		//finish adding
		
		Vector<Particle> particleSet = this.particleSystem.getParticleSet(); // the particles 
		MovsimWrap[] movSimParticleSystems = new MovsimWrap[particleSet.size()]; // the systems on particles
		for (int i = 0; i < movSimParticleSystems.length; i++){
			movSimParticleSystems[i] = ((MovSimState) particleSet.elementAt(i).state).getMovSimWrap();
		}

		
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
			result.particleReportList.add(pSys.getStateReport());
		}
		
		result.filteredAccidentError = 0;
		for( Particle p : this.particleSystem.getParticleSet()){
			MovsimWrap pSys = ((MovSimState)p.state).getMovSimWrap();
			result.filteredAccidentError += realSys.CalAccidentDistance(pSys, 0) * p.weight.doubleValue();
			// result.particleReportList.add(pSys.getStateReport());
		}
		// result.filteredAccidentError /= this.particleSystem.getParticleSet().size();
		result.segmentAvgSpeeds.add(realSys.getAvgSpeeds());
		result.segmentAvgSpeeds.add(simSys.getAvgSpeeds());
		result.segmentAvgSpeeds.add(bestParticleSys.getAvgSpeeds());
		result.segmentDensities.add(realSys.getSegmentDensities());
		result.segmentDensities.add(simSys.getSegmentDensities());
		result.segmentDensities.add(bestParticleSys.getSegmentDensities());
		// result.particleReportList.add(pReportStrings);
		expResults.add(result);
		
		// print particle weights
		int currentTime = stepLength * step;
		System.out.print("SMC =============================== Step" + step + " done! Current time = " + currentTime + " Sim-Error: " + String.format("%3.4f", result.simError) + " Fil-Error: " + String.format("%3.4f", result.filteredError) );
		System.out.println(" Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");
		
		if (currentTime > reportTime)
		{
			// if reportFigure : Draw accident map 
			if (reportFigure/* && (step==1 || step==2 || step==3 || step==4 || step==7 || step==8 || step==9 || step==10 )*/)
			{
				// display the results (draw the accident map)
				// assume this call is after a re-samling, i.e. the weights on all particles are equal
				// Peisheng will implement it				
				new SmcSimulationCanvas(realSys,"Real System, step "+step+ " time " + step*stepLength + " simulatd time" + realSys.getSimulationTime());
				//new SmcSimulationCanvas(simSys,"Simulated System, step " +step+ " time " + step*stepLength + " simulatd time" + simSys.getSimulationTime());
				new SmcSimulationCanvas(bestParticleSys, "Filtered System, step "+step+ " time " + step*stepLength + " simulatd time" + bestParticleSys.getSimulationTime());
				ObstacleCanvas obstacleCanvas = new ObstacleCanvas(new ArrayList<MovsimWrap>(Arrays.asList(movSimParticleSystems)),"Obstacle Canvas, step "+ step+ " time " + step*stepLength);
				obstacleCanvas.addRealObstacle(realSys);
			}
			
			if(reportError)
			{
				System.out.println("Saving numeric results.");
				String resultFolder = "results";
				File folder = new File(resultFolder);
				if (!folder.exists()) folder.mkdir();
				String testName = this.getClass().getSimpleName();
				String filePath = resultFolder+"/"+ testName.substring(0, testName.indexOf("MovSim"))+"PN"+ParticleNumber+"_Numberic.txt";
				
				
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
					
					writer.println( "Time\tFiltered Accident Error");
					for( MovSimSMCResult r : this.expResults)
						writer.println(r.currentTime + "\t" + String.format("%2.4f", r.filteredAccidentError));
					
					writer.close();
					System.out.println("Saved numeric results.");
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
			
			// report to file system states (average speed) of each particle 
			if (true) {
				
				String resultFolder = "ParticleReports";
				File folder = new File(resultFolder);
				if (!folder.exists()) folder.mkdir();
				
				String fileName = resultFolder+"/ParticleSpeedReport_" + String.format("%02d", step) + ".txt"; 
				
				// before writing down the reports, clean up the folder first				
				if (folder.listFiles() != null) {
					for (final File fileEntry : folder.listFiles()) {
						int index = fileEntry.getName().indexOf('_'); 
						if (Integer.parseInt(fileEntry.getName().substring(index+1, index+3)) > step) {
							fileEntry.delete();
						}						
				    }
				}
				
				// write down the reports
				try {
									
		             // Assume default encoding.
		             FileWriter fileWriter = new FileWriter(fileName);

		             // Always wrap FileWriter in BufferedWriter.
		             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		             // Write down the test settings
		             bufferedWriter.write("Test Settings\n");
		             bufferedWriter.write(String.format("Random Accident Rate: %.3f\n", GlobalConstants.TRANSITION_ACCIDENT_RATE));
		             bufferedWriter.write(String.format("%s \n", realSys.getObstacleString()));
		             
		             bufferedWriter.write("Particles:\n"+result.particleReportList.size()+"\n");
		             
		             // Note that write() does not automatically
		             // append a newline character.
		             for (int i = 0; i < result.particleReportList.size(); i++) {	      
		            	 bufferedWriter.write(result.particleReportList.get(i));
		            	 bufferedWriter.write("\n");
		             }
		             
		             //write the real system to the end of the result
		             bufferedWriter.write("Real System:\n");
		             bufferedWriter.write(realSys.getStateReport()+"\n");
		             
		             //write the simulated system to the end of the result
		             bufferedWriter.write("Simulated System:\n");
		             bufferedWriter.write(simSys.getStateReport()+"\n");
		             
		             // Always close files.
		             bufferedWriter.close();
		         }
		         catch(IOException ex) {
		             System.out.println(
		                 "Error writing to file '"
		                 + fileName + "'");
		             // Or we could just do this:
		             // ex.printStackTrace();
		         }
			}
		}
		
	}

}
