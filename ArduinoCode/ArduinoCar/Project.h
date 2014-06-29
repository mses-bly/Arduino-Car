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
#include "CarControl.h"

class Project
{
 private:
	BluetoothModule bluetoothModule;
	

 public:
	void setup();
	void loop();
};

extern CarControl carControl;
extern Command command;
#endif

