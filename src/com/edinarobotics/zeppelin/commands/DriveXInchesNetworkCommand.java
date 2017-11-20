package com.edinarobotics.zeppelin.commands;
import com.edinarobotics.zeppelin.Components;
import com.edinarobotics.zeppelin.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class DriveXInchesNetworkCommand extends Command{

	private Drivetrain drivetrain;
	private NetworkTable table;
	
	private double initialPosition, target, ticks;
	private final double rampDownStart = 24;											//inches from the target at which the drivetrain slows down speed
	private double rampDownValue = 0;
	private double startTime = 0;
	
	private boolean tooClose = false;
	private boolean stuckVision = false;
	private int wiggleCounter = 0;
	
	private final int CAMERA_X_DIMENSION = 640;
	private final int SLOW_RANGE_STRAFE = 30;
	private final int ENDING_TOLERANCE_STRAFE = 3;
	
	private final double DISTANCE_THRESHOLD = 500;
	
	double x,y = 0;
	
	AnalogInput ai;
	double bits;
	
	public DriveXInchesNetworkCommand(double inches) {
		super("drivexinchesnetworkcommand");
		drivetrain = Components.getInstance().getDrivetrain();
		ticks = -Math.round(((inches * 38*1.018) * 10) / 13);										//conversion from inches to ticks
		rampDownValue = rampDownStart*31.6923;
		requires(drivetrain);
	} 
	
	@Override
	protected void initialize() {
		System.out.println("INIT DRIVE X INCHES VISION VIA NETWORK TABLES");
		
		table = NetworkTable.getTable("SmartDashboard");
		initialPosition = drivetrain.getFrontLeftTalon().getEncPosition();
		target = initialPosition - ticks; //switch when switching encoder direction
			
		drivetrain.getBackLeftTalon().enableBrakeMode(true);
		drivetrain.getBackRightTalon().enableBrakeMode(true);
		drivetrain.getFrontLeftTalon().enableBrakeMode(true);
		drivetrain.getFrontRightTalon().enableBrakeMode(true);
				
		tooClose = false;
		stuckVision = false;
		wiggleCounter = 0;
		startTime = System.currentTimeMillis();
		
		ai = new AnalogInput(3);
		
		ai.setOversampleBits(4);
		ai.setAverageBits(2);
		bits = ai.getAverageVoltage();
		AnalogInput.setGlobalSampleRate(62500);
	}

	@Override
	protected void execute() {
		x = table.getNumber("visionX", -1);
		y = table.getNumber("visionY", -1);
		
		double deltaVision = 320-x;
		double velocityLeft = 0.33;
		double velocityRight = 0.33;
		
		//deleted portion of loop code that checks distance ticks, relies solely on distance sensor //
		//as i am unsure of how well it works or if tick values are accurate. change if necessary   //
		//they should probably be re-implemented after checking values                              //
		//also am unsure about speeds and other values brought in from prev. code                   //
		
		if (Math.abs(deltaVision)<20.0) {
			if (/*Math.abs(target - drivetrain.getFrontLeftTalon().getEncPosition()) < rampDownValue || Math.abs(drivetrain.getFrontLeftTalon().getEncPosition() - initialPosition) < 5*31.6923||*/ai.getAverageVoltage()>DISTANCE_THRESHOLD) {
				velocityLeft = velocityLeft/2;
				velocityRight = velocityRight/2;
			}
			System.out.println("Pixels Off Target: "+deltaVision);
		} else {
			if(deltaVision>0) {
				velocityLeft += 0.5; //unsure of value
			} else {
				velocityRight +=0.5;
			}
		}
		
		drivetrain.setDrivetrainSides(velocityLeft, velocityRight);
		System.out.println("L Velocity: " + velocityLeft +"  R Velocity: " + velocityRight);
	}

	@Override
	protected boolean isFinished() {
		if (Math.abs(startTime - System.currentTimeMillis()) > 200) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void end() {
		drivetrain.setValues(0.0, 0.0, 0.0);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		drivetrain.getBackLeftTalon().enableBrakeMode(false);
		drivetrain.getBackRightTalon().enableBrakeMode(false);
		drivetrain.getFrontLeftTalon().enableBrakeMode(false);
		drivetrain.getFrontRightTalon().enableBrakeMode(false);
	}

	@Override
	protected void interrupted() {
		end();
	}
	

}
