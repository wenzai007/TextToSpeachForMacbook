#!/bin/bash
if [ $2 -eq 1  ]; then 
	echo 'stop it '
	kill -STOP $1

elif [ $2 -eq  2 ]; then
	echo 'continue it'
	kill -CONT $1
fi

