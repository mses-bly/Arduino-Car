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
	double Kp;
	double Kd;
	double Ki;
	double currentValue;
	double referenceValue;
	double dt;
	
	protected:


	public:
	
	PIDControl(double _Kp, double _Kd, double _Ki, int _currentValue, int _referenceValue, double _dt);
		
	void setCoefficients(double _Kp, double _Kd, double _Ki);
	
	void computeStep();
	
	void driveToValue(int pwmPin);
	
	void setReferenceValue(double _referenceValue);
	
	double getCurrentValue();
};

#endif

