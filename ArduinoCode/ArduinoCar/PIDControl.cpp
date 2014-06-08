//
//
//

#include "PIDControl.h"

PIDControl::PIDControl(double _Kp = 0, double _Kd = 0, double _Ki = 0, int _currentValue = 0, int _referenceValue = 0, double _dt = 0):
Kp (_Kp) , Kd (_Kd), Ki (_Ki), currentValue (_currentValue) , referenceValue (_referenceValue), dt(_dt){
	error = 0;
	old_error = 0;
	integral = 0;
}


void PIDControl::setCoefficients(double _Kp, double _Kd, double _Ki){
	Kp = _Kp;
	Kd = _Kd;
	Ki = _Ki;
}

void PIDControl::computeStep(){
	error = referenceValue-currentValue;
	double derivative = (error-old_error)/dt;
	integral += (error*dt);
	double u = Kp*error + Kd*derivative + Ki*integral;
	old_error = error;
	currentValue += u;
}

void PIDControl::driveToValue(int pwmPin){
	double threshold = referenceValue;
	unsigned long tic = millis();
	double leftExtreme = 0.999*referenceValue;
	double rightExtreme = referenceValue + (0.01*referenceValue);
	while(!(currentValue >= leftExtreme && currentValue <= rightExtreme)){
		unsigned long toc = millis();
		if((double)((tic-toc)/1000) >= dt){
			computeStep();
			if (currentValue < 0){
				currentValue = 0;
			}
			analogWrite(pwmPin,(int)currentValue);
			tic = millis();
		}
	}
}

void PIDControl::setReferenceValue(double _referenceValue){
	referenceValue = _referenceValue;
}

double PIDControl::getCurrentValue(){
	return currentValue;
}