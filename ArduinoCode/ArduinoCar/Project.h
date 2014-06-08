/*
 * MyClass.h
 *
 * Created: 6/5/2014 10:45:44 PM
 * Author: Moises
 */ 

#ifndef _PROJECT_h
#define _PROJECT_h

#include "Includes.h"
#include "BluetoothModule.h"
#include "PIDControl.h"

class Project
{
 private:
	

 public:
 
	BluetoothModule bluetoothModule;
	
	void setup();
	void loop();
};

#endif

