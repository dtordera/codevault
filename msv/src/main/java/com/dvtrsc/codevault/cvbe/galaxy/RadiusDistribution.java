package com.dvtrsc.codevault.cvbe.galaxy;

import com.dvtrsc.codevault.cvbe.entities.RawProbabilisticDistribution;

public class RadiusDistribution extends RawProbabilisticDistribution {

	@Override
	public float DistributionFunction(float x) {
		
		float R = 0; 
		
		if (x < -30 || x > 30) R = 2/Math.abs(x);
		else
			R = 0;
//		if (x >= -30 && x <= 30) R = (float)Math.sqrt(2*30*30 - x*x)/500;		
		
//		R = Math.abs((float)Math.sin(x/100));
//		R = Math.abs((float)Math.exp(x/100));		
		
		return R;
	}
}
