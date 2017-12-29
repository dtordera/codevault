package com.dvtrsc.codevault.cvbe.galaxy;

import java.util.Random;

import com.dvtrsc.codevault.cvbe.entities.Star;

public class Galaxy {
	
	int radius;
	int height;
	Star[] stars; 
	long tickns;
			
	public long getTickns()
	{
		return tickns;
	}
	
	public int getHeight()
	{
		return height;
	}
		
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public int getRadius()
	{
		return radius;
	}
	
	public Star[] getStars()
	{
		return stars;
	}	
	
	public void setRadius(int r)
	{
		this.radius = r;
	}
	
	public int getStarsnumber()
	{
		return stars.length;
	}
	
	public Galaxy(int r, int starsnumber, float deviation, float anglevariation)
	{
		this.radius = r;
		stars = new Star[starsnumber];	
		
		for (int i=0;i<starsnumber;i++)
			stars[i] = new Star();
		
		tickns = System.nanoTime();
		
		RadiusDistribution D = new RadiusDistribution();
		Random rnd = new Random();
		
		float[] R = D.getDistributedPoints(-r, +r, starsnumber);
		
		height = 0;
		
		int i=0;
		for(Star s:stars)
		{
			s.setR(R[i]);			
			
			float p = Math.abs(R[i]);
			float d = rnd.nextFloat()*(2*r - Math.abs(R[i]))*(rnd.nextInt(2) == 0? -1 : 1)/deviation;
			
			s.setP((p + d)/anglevariation);			
			float z = rnd.nextFloat()*(D.DistributionFunction(s.getR()))*(200 + d*2);
			
			height = (int)(z > height? z : height); 
			
			s.setZ(z*(rnd.nextInt(2)==0? -1 : 1));						
			s.setM((float)(rnd.nextInt(100))/100);
			i++;
		}
		
		tickns = System.nanoTime() - tickns;
	}
}
