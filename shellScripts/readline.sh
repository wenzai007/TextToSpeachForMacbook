#!/bin/bash

FILENAME="/Users/Knight/Documents/workspace/TextToSpeech/shellScripts/lalala.txt"

while read LINE
	do
	echo $LINE
  echo -e "\n"
	
done < $FILENAME
