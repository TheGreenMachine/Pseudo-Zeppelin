package com.edinarobotics.zeppelin.commands;

import com.edinarobotics.zeppelin.Components;
import com.edinarobotics.zeppelin.subsystems.Collector;

import edu.wpi.first.wpilibj.command.Command;

public class UnpunchThenCloseGearCommand extends Command{

//	private final int DELAY_IN_MILLIS = 100;
//	private double startTime;

	public Collector collector;
	
	
	public UnpunchThenCloseGearCommand() {
		super("unpunchthenclosegearcommand");
		this.collector = Components.getInstance().getCollector();
		requires(collector);
	}
	
	protected void initialize(){
//		startTime = System.currentTimeMillis();
		collector.closeGearDoors();
//		while(Math.abs(startTime - System.currentTimeMillis()) < DELAY_IN_MILLIS);
		collector.unpunchGearCollector();
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
