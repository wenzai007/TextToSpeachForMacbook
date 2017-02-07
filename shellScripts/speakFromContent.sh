#!/bin/bash
FILENAME="/Users/Knight/Documents/workspace/TextToSpeech/shellScripts/content.txt"
FILENAMEOFPID="/Users/Knight/Documents/workspace/TextToSpeech/shellScripts/nowpid"
#echo "$$

echo "$$"  >  $FILENAMEOFPID
while read LINE
	do 
	echo $LINE 
	#echo -e "\n"
	say   $LINE

done < $FILENAME 

