package com.dvtrsc.codevault.cvbe.entities;

/*
 * Ping
 * D.Tordera, 20171031
 */
final public class Ping extends BaseEntity {
	
	public String echo; 
	public long ts; 
	
	public Ping(String echo)
	{
		this.echo = echo; 
		this.ts = System.currentTimeMillis()/1000; // return unix timestamp, seconds
	}
}
