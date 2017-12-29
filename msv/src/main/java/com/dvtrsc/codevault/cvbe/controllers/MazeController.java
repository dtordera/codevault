package com.dvtrsc.codevault.cvbe.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dvtrsc.codevault.cvbe.maze.Maze;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/maze")
public class MazeController 
{
	@ApiOperation(value="Gets a bit-encoded array maze")
	@ApiImplicitParams({
		@ApiImplicitParam(name="w", value="Maze width", required=true, dataType="int", paramType="query"),
		@ApiImplicitParam(name="h", value="Maze height", required=true, dataType="int", paramType="query")
	})
	
	@RequestMapping(value="", method=RequestMethod.GET, produces={"application/json", "application/encrypt"})
	public Maze getNew(	@RequestParam(value="w", required=true) int w , @RequestParam(value="h", required=true) int h) {				
		return new Maze(w,h);
    }
}
