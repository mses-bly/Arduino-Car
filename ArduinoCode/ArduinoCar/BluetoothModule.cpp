#include "BluetoothModule.h"

using namespace std;


void BluetoothModule::initialize(){
	Serial.begin(9600);
}

int BluetoothModule::listen(){
	while(Serial.available() <= 0);
	byte data = Serial.read();
	return data;
}
