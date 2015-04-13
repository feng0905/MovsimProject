package movSimSMC.test;

import movSimSMC.BootstrapMovSimIdenticalTwinExperiment;

public class BootstrapExpTest
{

	public static void main(String[] args)
	{
		int particleN = Integer.parseInt(args[0]);
		int stepLength = 25;
		int stepN = 20;
		
		BootstrapMovSimIdenticalTwinExperiment exp = new BootstrapMovSimIdenticalTwinExperiment(stepLength);
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
