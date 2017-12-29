
package com.dvtrsc.codevault.cvbe.tests;

import com.dvtrsc.codevault.cvbe.maze.Maze;

public class MazeTest
{
	public static void main(String args[])
	{
		Maze M = new Maze(20,20);	
		System.out.println(M.toBinString());
	}
}
