#ifndef BLUETOOTHMODULE_H
#define BLUETOOTHMODULE_H
#include <Arduino.h>
using namespace std;
class BluetoothModule {
public:
    void initialize();
    char* listen();
};

#endif // BLUETOOTHMODULE_H_INCLUDED

