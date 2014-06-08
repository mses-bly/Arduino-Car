#include "PIDControl.h"
#include "CarControl.h"
/*
 * ArduinoCar.ino
 *
 * Created: 6/5/2014 10:45:44 PM
 * Author: Moises
 */ 

#include "Project.h"

Project myProject;

void setup()
{
	myProject.setup();
}

void loop()
{
	myProject.loop();
	
}
