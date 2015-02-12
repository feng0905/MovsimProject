package SubStatePF;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import smc.AbstractParticleSystem;
import smc.Particle;
import smc.specialParticleSystems.SenSimFilter;
import movSimSMC.AbstractMovSimIdenticalTwinExperiment;
import movSimSMC.BootstrapMovSimIdenticalTwinExperiment;
import movSimSMC.GlobalConstants;
import movSimSMC.MovSimState;
//import movSimSMC.SubStatePF;
import movSimSMC.AbstractMovSimIdenticalTwinExperiment.MovSimSMCResult;
import movsimSMC.MovsimWrap;
import movsimSMC.Paint.ObstacleCanvas;
import movsimSMC.Paint.SmcSimulationCanvas;

public class SubStateMovSimTwinExperiment extends
		AbstractMovSimIdenticalTwinExperiment {

	public SubStateMovSimTwinExperiment(int stepLength) {
		super(stepLength);
		 
	}

	@Override
	protected AbstractParticleSystem createParticleSystem(
			Vector<Particle> particleSet) {
		return new SubStateFilter(particleSet);
		 
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
				ObstacleCanvas obstacleCanvas = new ObstacleCanvas(new ArrayList<MovsimWrap>(Arrays.asList(movSimParticleSystems)),"Obstacle Canvas, step "+ step+ " time " + step*stepLength);
				obstacleCanvas.addRealObstacle(realSys);
				new SmcSimulationCanvas(realSys,"Real System, step "+step+ " time " + step*stepLength + " simulatd time" + realSys.getSimulationTime());
				new SmcSimulationCanvas(simSys,"Simulated System, step " +step+ " time " + step*stepLength + " simulatd time" + simSys.getSimulationTime());
				new SmcSimulationCanvas(bestParticleSys, "Filtered System, step "+step+ " time " + step*stepLength + " simulatd time" + bestParticleSys.getSimulationTime());
				
			
				if(step==3){
					//Yuan 2/12/2015 for debugging
					Vector<Particle> curParticleSet = ((SubStatePF.AbstractSubStateParticleSystem)this.particleSystem).vecBeforeUpdateParticlesSet; // the particles 
					MovsimWrap[] curMovSimParticleSystems = new MovsimWrap[curParticleSet.size()]; // the systems on particles
					for (int i = 0; i < movSimParticleSystems.length; i++){
						curMovSimParticleSystems[i] = ((MovSimState) curParticleSet.elementAt(i).state).getMovSimWrap();
					}
					
					for(int i=0;i<curMovSimParticleSystems.length;i++){
						new SmcSimulationCanvas(curMovSimParticleSystems[i],"Filtered System, step "+step+ "Particle-"+i+" time " + step*stepLength + " simulatd time" + movSimParticleSystems[i].getSimulationTime());
					}
				}
			
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
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int particleN = 1;
		int stepLength = 15;
		int stepN = 3;
		
		SubStateMovSimTwinExperiment exp = new SubStateMovSimTwinExperiment(stepLength);
		try
		{
			exp.runDataAssimilationExperiement(stepN, particleN);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
