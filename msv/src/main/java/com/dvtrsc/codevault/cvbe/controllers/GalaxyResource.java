
package com.dvtrsc.codevault.cvbe.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dvtrsc.codevault.cvbe.galaxy.Galaxy;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/galaxy")
public class GalaxyResource 
{
	@ApiOperation(value="Gets a Galaxy")
	@ApiImplicitParams({
		@ApiImplicitParam(name="r", value="Radius (i.e, 500)", required=true, dataType="int", paramType="query"),
		@ApiImplicitParam(name="t", value="Total number of stars (i.e, 10000)", required=true, dataType="int", paramType="query"),
		@ApiImplicitParam(name="d", value="Deviation (inverse, i.e, 10)", required=true, dataType="float", paramType="query"),
		@ApiImplicitParam(name="a", value="Angle variation (inverse, i.e., 30)", required=true, dataType="float", paramType="query")
	})
	
	@RequestMapping(value="", method=RequestMethod.GET, produces={"application/json", "application/encrypt"})
    public Galaxy getNew(
    		@RequestParam(value="r", required=true) int r,
    		@RequestParam(value="t", required=true) int t,
    		@RequestParam(value="d", required=true) float d,    		
    		@RequestParam(value="a", required=true) float a){   
		
		return new Galaxy(r,t,d,a);			
    }
}