package com.dvtrsc.codevault.cvbe.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dvtrsc.codevault.cvbe.entities.Ping;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiImplicitParam;


/*
 * PingController. Check if API is alive. It returns server Unix timestamp. Can be used for synchronization.  
 * D.Tordera, 20171031
 */

@RestController
@RequestMapping("/ping")
public class PingController {
 		
	@ApiOperation(value="Check API working status (with echo & validation)")
	@ApiImplicitParams({
		@ApiImplicitParam(name="U", value="login user (USER/p4ssw0rd or any user and pass=user)", required=true, dataType= "string", paramType="header"),
		@ApiImplicitParam(name="ST", value="salt (Unix timestamp)", required=true, dataType="long", paramType="header"),
		@ApiImplicitParam(name="SH", value="salted hash (hexsha256 of hexsha256(password) concat with hexsha256(salt), all lowercase)", required=true, dataType="string", paramType="header")
	})

	@RequestMapping(value="/{echo}", method=RequestMethod.GET, produces={"application/json", "application/encrypt"})				
	public Ping doPing(HttpServletRequest request, @PathVariable("echo") String echo)
	{		
		return new Ping(echo);
	}
	
	@ApiOperation(value="Check API working status")	
	@RequestMapping(value="", method=RequestMethod.GET, produces={"application/json", "application/encrypt"})
	public Ping doPing(HttpServletRequest request)
	{		
		return new Ping("pong");
	}
}
