#include "CarControl.h"



CarControl::CarControl(int _speedPin, int _forwardPin, int _reversePin):
speedPin(_speedPin),forwardPin(_forwardPin),reversePin(_reversePin){
	
	pinMode(speedPin, OUTPUT);
	pinMode(forwardPin, OUTPUT);
	pinMode(reversePin, OUTPUT);	
	
	digitalWrite(reversePin,LOW);
	digitalWrite(forwardPin,LOW);
	digitalWrite(speedPin,LOW);
	
	direction = NULL;
		
}

void CarControl::forward(){
	digitalWrite(reversePin,LOW);
	digitalWrite(forwardPin,HIGH);
	direction = FORWARD;
}

void CarControl::reverse(){
	digitalWrite(forwardPin,LOW);
	digitalWrite(reversePin,HIGH);
	direction = REVERSE;
}


void CarControl::stop(){
	digitalWrite(forwardPin,LOW);
	digitalWrite(reversePin,LOW);
	direction = NULL;
}
void CarControl::setSpeed(int speed){
	pidControl.driveToValue(speedPin,(double)speed);
}

void CarControl::executeCommand(Command command){
	int newDirecion = command.getDirection();
	if(direction != newDirecion){
		stop();
		//avoid breaking the gears
		delay(1000);
		if(newDirecion == FORWARD){
			forward();
		}
		else{
			reverse();
		}
	}
	setSpeed(command.getSpeed());
}

