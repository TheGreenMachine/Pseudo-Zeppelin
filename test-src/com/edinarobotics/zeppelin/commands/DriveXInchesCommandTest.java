package com.edinarobotics.zeppelin.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.edinarobotics.zeppelin.TestSetup;

public class DriveXInchesCommandTest extends TestSetup {
	DriveXInchesCommand command;
	final static double DISTANCE = 10.0;
	final static double VELOCITY = 20.0;
	
	@Before
	public void setup() {
		command = new DriveXInchesCommand(DISTANCE, VELOCITY);
	}
	
	@Test
	public void execute_testZeros() {
		ArgumentCaptor<Double> leftArg = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Double> rightArg = ArgumentCaptor.forClass(Double.class);
		when(mockDrivetrain.getFrontLeftEncPosition()).thenReturn(200);
		when(mockDrivetrain.getGyroAngle()).thenReturn(0.0);
		
		command.initialize();
		command.execute();
		
		verify(mockDrivetrain, times(1) ).setDrivetrainSides(leftArg.capture(), rightArg.capture());
		
		double speedLeft = VELOCITY  * 0.6 / 1.4;
		double speedRight = speedLeft * 0.95;
		assertEquals(speedRight, rightArg.getValue().doubleValue(), 0.001);
		assertEquals(speedLeft, leftArg.getValue().doubleValue(), 0.001);
	}
	
	@Test
	public void execute_isFinished_zeros() throws Exception {
		when(mockDrivetrain.getFrontLeftEncPosition()).thenReturn(0);
		when(mockDrivetrain.getGyroAngle()).thenReturn(0.0);
		
		command.initialize();
		boolean isFinished = command.isFinished();
		
		assertFalse(isFinished);
	}
	
	@Test
	public void execute_isFinished_timeout() throws Exception {
		when(mockDrivetrain.getFrontLeftEncPosition()).thenReturn(0);
		when(mockDrivetrain.getGyroAngle()).thenReturn(0.0);
		
		command.initialize();
		
		setPrivateLong(command, "startTime", 5000);
		boolean isFinished = command.isFinished();
		
		assertTrue(isFinished);
	}
	
	public void  setPrivateLong(Object obj, String name, long value) throws Exception {
		Field fld = obj.getClass().getDeclaredField(name);
		fld.setAccessible(true);
		fld.setLong(obj, value);
	}
}
