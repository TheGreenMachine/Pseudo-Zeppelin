package com.edinarobotics.zeppelin;

import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.mockito.Mockito;

import com.edinarobotics.zeppelin.subsystems.Drivetrain;

public class TestSetup {
	
	public static Components mockComponents;
	public static Drivetrain mockDrivetrain;
	
	@BeforeClass
	public static void classSetup() {
		mockComponents = Mockito.mock(Components.class);
		Components.setInstance(mockComponents);
		mockDrivetrain = Mockito.mock(Drivetrain.class);
		when(mockComponents.getDrivetrain()).thenReturn(mockDrivetrain);
	}

}
