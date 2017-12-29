package com.dvtrsc.codevault.cvbe.entities;

import java.util.Random;

public class Utility 
{	
	public static void Shuffle(byte[] A, Random rnd)
	{
		int	n = A.length,
			i,
			r;		
		byte e;
		
		for (i=0;i<n;i++)
		{
			r = i + rnd.nextInt(n-i);
			e = A[r];
			A[r] = A[i];
			A[i] = e; 
		}		
	}
}