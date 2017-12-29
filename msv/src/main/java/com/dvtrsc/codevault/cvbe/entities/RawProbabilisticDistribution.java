package com.dvtrsc.codevault.cvbe.entities;

import java.util.Random;

public abstract class RawProbabilisticDistribution {

	public abstract float DistributionFunction(float x); // returns probability of success for x
	
	public float[] getDistributedPoints(int min, int max, int totalpoints)
	{	
		int count=0;
		Random rnd = new Random();
		float x, y;
		float[] R = new float[totalpoints];
		
		int delta = max - min;
		
		while (count < totalpoints)
		{
			x = min + rnd.nextInt(delta);
			y = rnd.nextFloat();
			
			if (y < DistributionFunction(x)) 
				R[count++] = x;
		}		
		
		return R;
	}
}
