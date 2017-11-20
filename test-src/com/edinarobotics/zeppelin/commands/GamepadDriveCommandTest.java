package com.edinarobotics.zeppelin.commands;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.math.Vector2;
import com.edinarobotics.zeppelin.TestSetup;

public class GamepadDriveCommandTest extends TestSetup {
	Gamepad mockGamepad;
	Vector2 leftJoy;
	Vector2 rightJoy;
	
	GamepadDriveCommand command;
	
	@Before
	public void setup() {
		mockGamepad = Mockito.mock(Gamepad.class);
		leftJoy = Mockito.mock(Vector2.class);
		rightJoy = Mockito.mock(Vector2.class);
		when(mockGamepad.getLeftJoystick()).thenReturn(leftJoy);
		when(mockGamepad.getRightJoystick()).thenReturn(rightJoy);
		command = new GamepadDriveCommand(mockGamepad);
	}
	
	@Test
	public void execute_test() {
		when(mockGamepad.getRightX()).thenReturn(12.0);
		
		when(leftJoy.getX()).thenReturn(10.0);
		when(leftJoy.getY()).thenReturn(11.0);
		when(rightJoy.getX()).thenReturn(12.0);

		command.execute();
		
		verify(mockDrivetrain).setValues(10.0, 11.0, 12.0);
	}
}
