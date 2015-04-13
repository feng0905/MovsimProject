package movSimSMC.test;

import java.math.BigDecimal;

import movSimSMC.SenSimMovSimIdenticalTwinExperiment;
import smc.weightUpdatingStrategies.KernelEstimationProposalWeight;
import smc.weightUpdatingStrategies.KernelEstimationProposalWeight.KernelFunction;

public class SenSimExpTest
{
	public static void main(String[] args)
	{
		int particleN = Integer.parseInt(args[0]);
		int kernelN = 10;
		int stepLength = 25;
		int stepN = 20;
		
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
