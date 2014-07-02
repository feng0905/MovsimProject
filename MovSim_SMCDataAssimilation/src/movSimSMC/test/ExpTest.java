package movSimSMC.test;

import java.math.BigDecimal;

import movSimSMC.SenSimMovSimIdenticalTwinExperiment;
import smc.weightUpdatingStrategies.KernelEstimationProposalWeight;
import smc.weightUpdatingStrategies.KernelEstimationProposalWeight.KernelFunction;

public class ExpTest
{
	public static void main(String[] args)
	{
		int particleN = 10;
		int kernelN = 10;
		int stepLength = 120;
		KernelFunction kernel =  new KernelEstimationProposalWeight.GaussianKernel();
		
		double bandwidth=2000;
		BigDecimal bandWidth = BigDecimal.valueOf(bandwidth);
		
		SenSimMovSimIdenticalTwinExperiment exp = new SenSimMovSimIdenticalTwinExperiment(stepLength, kernelN, kernel, bandWidth);
		try
		{
			exp.runDataAssimilationExperiement(5, particleN);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
