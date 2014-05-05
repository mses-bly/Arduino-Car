#include "BluetoothModule.h"

using namespace std;


void BluetoothModule::initialize(){
    Serial.begin(9600);
}

char* BluetoothModule::listen(){
    while(Serial.available() <= 0);
    char* data = new char[1];
    Serial.readBytes(data,1);
    return data;
}

