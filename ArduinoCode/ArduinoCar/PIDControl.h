// PIDControl.h

#ifndef _PIDCONTROL_h
#define _PIDCONTROL_h

#include "Includes.h"

class PIDControl
{
	
	private:
	
	double error;
	double old_error;
	double integral;
	double currentValue;
	double referenceValue;
		
	static const double Kp = 0.4;
	static const double Kd = 0.001;
	static const double Ki = 0.0001;
	static const double dt = 0.01;


	public:
	
	void computeStep();
	
	void driveToValue(int pwmPin, double objectiveSpeed);
	
	double getCurrentValue();
};

#endif

