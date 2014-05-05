#include "BluetoothModule.h"
int led = 7;
BluetoothModule bluetoothModule;

void setup() {
    bluetoothModule.initialize();
    pinMode(led, OUTPUT);
}

void loop() {
    char* data = bluetoothModule.listen();
    if(data[0] == 'H')
        digitalWrite(led,HIGH);
    else if(data[0] == 'L')
        digitalWrite(led,LOW);
}



