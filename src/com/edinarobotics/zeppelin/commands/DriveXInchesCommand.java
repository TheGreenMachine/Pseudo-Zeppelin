package com.edinarobotics.zeppelin.commands;

import com.edinarobotics.zeppelin.Components;
import com.edinarobotics.zeppelin.subsystems.Drivetrain;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Command;

public class DriveXInchesCommand extends Command {

	private Drivetrain drivetrain;
	
	private double velocity;
	private double initialPosition, target, ticks, initialAngle;
	private final double rampDownStart = 24;
	private final double rampDownValue;
	private double inchTarget;
	private double startTime;
	
	public DriveXInchesCommand(double inches, double velocity) {
		super("drivexinchescommand");
		this.velocity = velocity;
		this.inchTarget = inches;
		drivetrain = Components.getInstance().getDrivetrain();
		//You should math.round instead of cast you could be off by like .5%.
//		ticks = (int)(((inches * 41.2*1.018) * 10) / 13);	//32//COMPETITION CARPET CONSTANT				//OG Zeppelin constants	
		ticks = (int)(((inches * 38*1.018) * 10) / 13);	//29				//OG Zeppelin constants	
		rampDownValue = ((rampDownStart * 42) * 10) / 13;
		requires(drivetrain);
	}
	
	@Override
	protected void initialize() {
		System.out.println("INIT DRIVE X INCHES");
		initialPosition = drivetrain.getFrontLeftEncPosition();
		target = ticks + initialPosition;
		initialAngle = drivetrain.getGyroAngle();
		
		drivetrain.raiseCenterWheel();
		
		drivetrain.enableBrakes(true);
		
		startTime = System.currentTimeMillis();
	}

	@Override 
	protected void execute() {
		
		double deltaDegree = drivetrain.getGyroAngle() - initialAngle;
		
		double left, right;
		
		if (ticks>0)
			if ((target - drivetrain.getFrontLeftEncPosition()) < rampDownValue) {				//poor
				right = (velocity*.95) * (((deltaDegree) / 50) + 1) / 1.4;								//man's
				left = velocity * (((-deltaDegree) / 50) + 1) /1.4;										//PID
			} else {																					//
				right = (velocity*.95) * (((deltaDegree) / 50) + 1);									//
				left = velocity * (((-deltaDegree) / 50) + 1);											//
			}																							//
		else
			if ((target - drivetrain.getFrontLeftEncPosition()) > -rampDownValue) {				//poor
				right = (-velocity*.95) * (((deltaDegree) / 50) + 1) / 1.4;								//man's
				left = -velocity * (((-deltaDegree) / 50) + 1) / 1.4;										//PID
			} else {																					//
				right = (-velocity*.95) * (((deltaDegree) / 50) + 1);									//
				left = -velocity * (((-deltaDegree) / 50) + 1);											//
			}
		
//		System.out.println("Error: " + deltaDegree);
		
//		double right = (velocity*.95);
//		double left = velocity;

		drivetrain.setDrivetrainSides(0.6*left, 0.6*right);
	}

	@Override
	protected boolean isFinished() {
		if(Math.abs(startTime - System.currentTimeMillis()) > 3000)
			return true;
//		System.out.println("Off by: " + (drivetrain.getFrontLeftTalon().getEncPosition() - target));
		if(ticks>0)
			return drivetrain.getFrontLeftEncPosition() > target;
		else
			return drivetrain.getFrontLeftEncPosition() < target;	
	}

	@Override
	protected void end() {
		drivetrain.setValues(0.0, 0.0, 0.0);
		
		
//		drivetrain.getBackLeftTalon().enableBrakeMode(false);
//		drivetrain.getBackRightTalon().enableBrakeMode(false);
//		drivetrain.getFrontLeftTalon().enableBrakeMode(false);
//		drivetrain.getFrontRightTalon().enableBrakeMode(false);
	}

	@Override
	protected void interrupted() {
		end();
	}

}
