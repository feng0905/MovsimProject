package movSimSMC.test;

import java.math.BigDecimal;

import movSimSMC.SenSimMovSimIdenticalTwinExperiment;
import smc.weightUpdatingStrategies.KernelEstimationProposalWeight;
import smc.weightUpdatingStrategies.KernelEstimationProposalWeight.KernelFunction;

public class SenSimExpTest
{
	public static void main(String[] args)
	{
		
		int kernelN = 10;
		int stepLength = 180;
		int stepN = 15;
		
		if (args.length == 1) {
			int particleN = Integer.parseInt(args[0]);
			KernelFunction kernel =  new KernelEstimationProposalWeight.GaussianKernel();	
			double bandwidth=0.15;
			BigDecimal bandWidth = BigDecimal.valueOf(bandwidth);
			
			SenSimMovSimIdenticalTwinExperiment exp = new SenSimMovSimIdenticalTwinExperiment(stepLength, kernelN, kernel, bandWidth);
			try
			{
				exp.runDataAssimilationExperiement(stepN, particleN);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (args.length == 3){
			
			
			for (int i = Integer.parseInt(args[0]); i <= Integer.parseInt(args[2]); i+=Integer.parseInt(args[1])) {
				int particleN = i;
				KernelFunction kernel =  new KernelEstimationProposalWeight.GaussianKernel();
			
				double bandwidth=0.15;
				BigDecimal bandWidth = BigDecimal.valueOf(bandwidth);
			
				SenSimMovSimIdenticalTwinExperiment exp = new SenSimMovSimIdenticalTwinExperiment(stepLength, kernelN, kernel, bandWidth);
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
