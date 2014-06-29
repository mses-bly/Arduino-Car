/*
 * Command.h
 *
 * Created: 6/29/2014 3:57:54 PM
 *  Author: Moises Baly
 */ 
#ifndef _COMMAND_h
#define _COMMAND_h

#include "Includes.h"

#define FORWARD 0
#define REVERSE 1

class Command
{
	private:
	
	//actions[0] = direction
	//actions[1] = speed
	int actions[2];

	int index;
	
	public:
	
	Command();
	
	int addOrder(int order);
	
	int getDirection();
	
	int getSpeed();
};

#endif

