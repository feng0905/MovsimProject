package movSimSMC.test;

import movSimSMC.BootstrapMovSimIdenticalTwinExperiment;

public class BootstrapExpTest
{

	public static void main(String[] args)
	{
		int particleN = 10;
		int stepLength = 300;
		int stepN = 15;
		
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

