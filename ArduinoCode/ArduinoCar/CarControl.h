// CarControl.h

#ifndef _CARCONTROL_h
#define _CARCONTROL_h

#include "Includes.h"
#include "PIDControl.h"
#include "Command.h"
#define FORWARD 0
#define REVERSE 1
class CarControl
{
 private:
 


 int speedPin;
 int forwardPin;
 int reversePin;

 int direction;

 PIDControl pidControl;
	
 public:
 
 CarControl(int _speedPin, int _forwardPin, int _reversePin);

 void forward();
 
 void reverse();
 
 void stop();
 
 void setSpeed(int speed);
 
 void executeCommand(Command command);
 
};

#endif

