/*

* MyClass.cpp
*
* Created: 6/5/2014 10:45:44 PM
* Author: Moises
*/
#include "Project.h"

int led = 9;
double Kp = 0.4;
double Kd = 0.001;
double Ki = 0.0001;
double dt = 0.01;
PIDControl pidControl(Kp,Kd,Ki,0,255,dt);

void Project::setup()
{	
	bluetoothModule.initialize();
	pinMode(led, OUTPUT);
	pinMode(6, OUTPUT);
}

void Project::loop()
{

	analogWrite(6,255);
	int value = bluetoothModule.listen();
	Serial.print("Received value: ");
	Serial.print(value);
	Serial.println();
	pidControl.setReferenceValue(value);
	pidControl.driveToValue(led);
	
}


