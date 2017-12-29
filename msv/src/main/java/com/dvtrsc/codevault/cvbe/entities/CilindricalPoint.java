package com.dvtrsc.codevault.cvbe.entities;

public class CilindricalPoint {

	float r; // radius (distance against origin)
	float p; // phi (direction)
	float z; // elevation
	
	public void setR(float r) { this.r = r; }	
	public void setP(float p) { this.p = p; }	
	public void setZ(float z) { this.z = z; }	
	
	public float getR() { return r; }	
	public float getP() { return p; }	
	public float getZ() { return z; }
}
