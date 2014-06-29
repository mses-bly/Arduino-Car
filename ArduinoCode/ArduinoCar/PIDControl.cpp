//
//
//

#include "PIDControl.h"

void PIDControl::computeStep(){
	error = referenceValue-currentValue;
	double derivative = (error-old_error)/dt;
	integral += (error*dt);
	double u = Kp*error + Kd*derivative + Ki*integral;
	old_error = error;
	currentValue += u;
}

void PIDControl::driveToValue(int pwmPin, double objectiveSpeed){
	referenceValue = objectiveSpeed;
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

double PIDControl::getCurrentValue(){
	return currentValue;
}