package movSimSMC.test;

import movSimSMC.BootstrapMovSimIdenticalTwinExperiment;

public class BootstrapExpTest
{

	public static void main(String[] args)
	{
		int particleN = 5;
		int stepLength = 300;
		int stepN = 5;
		
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
