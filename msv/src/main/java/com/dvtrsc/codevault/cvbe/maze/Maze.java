package com.dvtrsc.codevault.cvbe.maze;

import java.util.Random;

import com.dvtrsc.codevault.cvbe.entities.K;
import com.dvtrsc.codevault.cvbe.entities.Utility;

public class Maze 
{
	int width,height;
	byte[] map;
	Random rnd = new Random();
	long tickns;
	
	public Maze()	{}
	
	public byte[] getMap()
	{
		return map;
	}
	
	public long getTickns()
	{
		return tickns;
	}
	
	public Maze(int w, int h)
	{
		this.width = w;
		this.height = h;
		
		map = new byte[w*h];
					
		Dig();
	}	
	public byte getCell(int x, int y)
	{
		return map[x + y*width];
	}
	
	public void setCell(int x, int y, byte b)
	{
		map[x + y*width] = b;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	@Override
	public String toString()
	{
		int i;
		String s = "";
		
		for (i=0;i<map.length;i++)
			s+=map[i] + ",";
		
		return s.substring(0,s.length()-1);		
	}		
		
	private boolean ToDig(int x, int y)
	{
		return (x >= 0 && x < width && y >= 0 && y < height && getCell(x,y) == 0);
	}
	
	private void Open(int x, int y, byte direction)
	{
		setCell(x,y, (byte)(getCell(x,y) | 1 << direction));
	}
	
	private void DigHere(int x, int y)
	{		
		byte[] A = new byte[]{0,1,2,3}; 		
		Utility.Shuffle(A, rnd);
		
		byte i;
		int xf,yf;
				
		for (i=0;i<A.length;i++)
		{
			xf = x + K.COS[A[i]];
			yf = y + K.SIN[A[i]];
			
			if (ToDig(xf,yf)) 
			{
				Open(x, y, A[i]);
				Open(xf,yf,(byte)((A[i]+2)%4));
				DigHere(xf,yf);
			}
		}
	}
		
	private void Dig()
	{
		tickns = System.nanoTime();
		DigHere(rnd.nextInt(width),rnd.nextInt(height));	
		tickns = System.nanoTime()- tickns;
	}
	
	public String toBinString()
	{
		int x,y;
		String s = "";
		
		for (y=0;y<height;y++)
		{
			for (x=0;x<width;x++)
				s += Integer.toBinaryString(0x20 + getCell(x,y)).substring(2) + "\t";
			
			s += "\n";
		}
		
		return s.substring(0,s.length()-1);
	}
	
	public String toMapString()
	{
		return null;
	}
}