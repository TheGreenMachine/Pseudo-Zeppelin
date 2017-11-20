package com.edinarobotics.zeppelin.commands;

import javax.swing.plaf.synth.SynthSeparatorUI;

import com.edinarobotics.zeppelin.Components;
import com.edinarobotics.zeppelin.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;

public class IncrementShooterSpeedCommand extends Command{

	private Shooter shooter;
	private double inc;
	
	public IncrementShooterSpeedCommand(double inc){
		super("incrementshooterspeedcommand");
		this.shooter = Components.getInstance().getShooter();
		this.inc = inc;
		requires(shooter);
	}
	
	protected void initialize(){
		shooter.addIncrementSpeed(inc);
		System.out.println(shooter.getIncrementedShooterSpeed());
		shooter.setShooterTalons(shooter.getIncrementedShooterSpeed(),shooter.getIncrementedShooterSpeed());
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}	
	
}
