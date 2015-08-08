package movSimSMC.test;

import movSimSMC.BootstrapMovSimIdenticalTwinExperiment;
import movSimSMC.GlobalConstants;

public class BootstrapExpTest
{

	public static void main(String[] args)
	{
		int stepLength = 180;
		int stepN = 15;
		
		if (args.length == 1) {
			int particleN = Integer.parseInt(args[0]);			
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
		else if (args.length == 3) {
			for (int i = Integer.parseInt(args[0]); i <= Integer.parseInt(args[2]); i+=Integer.parseInt(args[1])) {
				int particleN = i;
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
		

	}

}
