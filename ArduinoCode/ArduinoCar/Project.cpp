/*

* MyClass.cpp
*
* Created: 6/5/2014 10:45:44 PM
* Author: Moises
*/
#include "Project.h"



CarControl carControl(3,4,7);

Command command;

void Project::setup()
{	
	bluetoothModule.initialize();
}

void Project::loop()
{
	
	
	//Serial.println("Going forward");
	//carControl.setSpeed(0);
	//carControl.forward();
	//carControl.setSpeed(100);
	//delay(20000);
	//Serial.println("Increasing Speed");
	//carControl.setSpeed(255);
	//delay(20000);
	//Serial.println("Stopping");
	//carControl.setSpeed(0);
	//delay(20000);
	//Serial.println("Change Direction");
	//carControl.reverse();
	//carControl.setSpeed(255);
	//delay(20000);
	//
	
	int value = bluetoothModule.listen();
	int response = command.addOrder(value);
	if(response == 1){
		carControl.executeCommand(command);
	}
}


