 #!/bin/bash
PIDFILE="/Users/Knight/Documents/workspace/TextToSpeech/shellScripts/nowpid"
	cat $PIDFILE | xargs kill -9
