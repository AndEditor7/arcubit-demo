package com.andedit.arcubit.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;

public class Codes {
	public static final int axisLeftX = 0;
    public static final int axisLeftY = 1;
    public static final int axisRightX = 2;
    public static final int axisRightY = 3;

    public static final int buttonA = 4;
    public static final int buttonB = 5;
    public static final int buttonX = 6;
    public static final int buttonY = 7;
    public static final int buttonBack = 8;
    public static final int buttonStart = 9;

    public static final int buttonL1 = 10;
    public static final int buttonL2 = 11;
    public static final int buttonR1 = 12;
    public static final int buttonR2 = 13;

    public static final int buttonDpadUp = 14;
    public static final int buttonDpadDown = 15;
    public static final int buttonDpadLeft = 16;
    public static final int buttonDpadRight = 17;

    public static final int buttonLeftStick = 18;
    public static final int buttonRightStick = 19;
    
    public static int toCode(Controller control, int utilCode) {
    	return toCode(control.getMapping(), utilCode);
    }
    
    public static int toCode(ControllerMapping map, int utilCode) {
    	switch (utilCode) {
		case axisLeftX: return map.axisLeftX;
		case axisLeftY: return map.axisLeftY;
		case axisRightX: return map.axisRightX;
		case axisRightY: return map.axisRightY;
		
		case buttonA: return map.buttonA;
		case buttonB: return map.buttonB;
		case buttonX: return map.buttonX;
		case buttonY: return map.buttonY;
		case buttonBack: return map.buttonBack;
		case buttonStart: return map.buttonStart;
		
		case buttonL1: return map.buttonL1;
		case buttonL2: return map.buttonL2;
		case buttonR1: return map.buttonR1;
		case buttonR2: return map.buttonR2;
		
		case buttonDpadUp: return map.buttonDpadUp;
		case buttonDpadDown: return map.buttonDpadDown;
		case buttonDpadLeft: return map.buttonDpadLeft;
		case buttonDpadRight: return map.buttonDpadRight;
		
		case buttonLeftStick: return map.buttonLeftStick;
		case buttonRightStick: return map.buttonRightStick;
		
		default: return -1;
		}
    }
}
