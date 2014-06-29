/*
* Command.cpp
*
* Created: 6/29/2014 3:58:13 PM
*  Author: Moises Baly
*/


#include "Command.h"

Command::Command(){
	index = -1;
}

int Command::getDirection(){
	return actions[0];
}

int Command::getSpeed(){
	return actions[1];
}


int Command::addOrder(int order){
	switch(index){
		case -1:
			if(order == 2){
				index++;
			}
		break;
		case 0:
			if(order == 1 || order == 0){
				actions[index] = order;
				index++;
			}
			else{
				//ignore and discard the command
				index = -1;
			}
		break;
		case 1:
			if(order >= 0 && order <= 255){
				actions[index] = order;
				index = -1;
				return 1;
			}
			else{
				//ignore and discard the command
				index = -1;
			}
		break;
		default:
		//ignore and discard the command
		index = -1;
		break;
	}
	
	return -1;
}