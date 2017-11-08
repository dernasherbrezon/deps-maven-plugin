#!/bin/bash

workingdir="./bash_unit"
scriptLocation=../../../src/main/resources/download-dependencies.sh

_wget() {
	IFS=' ' read -ra ARGS <<< "$FAKE_PARAMS"
	touch ${ARGS[1]}
	printf "downloaded ${ARGS[1]}\n"
}

_failed_wget() {
	return 1
}

test_success() {
	testWorkingDir="$workingdir/${FUNCNAME[0]}"
	mkdir -p "$testWorkingDir"
	
	touch "$testWorkingDir/test2.jar"
	touch "$testWorkingDir/test3.jar"
	printf "/com/example/1.0/test.jar\n/com/example/1.0/test2.jar" > $testWorkingDir/dependencies.txt
	printf "https://repo1.maven.org/maven2" > $testWorkingDir/repositories.txt
	
	export -f _wget
	fake wget _wget 
	$scriptLocation $testWorkingDir $testWorkingDir
	
	assert "test -e $testWorkingDir/test.jar"
	assert "[ ! -f $testWorkingDir/test3.jar ]"
}

test_unable_to_download() {
	testWorkingDir="$workingdir/${FUNCNAME[0]}"
	mkdir -p "$testWorkingDir"
	
	printf "/com/example/1.0/test.jar" > $testWorkingDir/dependencies.txt
	printf "https://repo1.maven.org/maven2" > $testWorkingDir/repositories.txt
	
	export -f _failed_wget
	fake wget _failed_wget 
	assert_status_code 1 $scriptLocation $testWorkingDir $testWorkingDir
	assert "[ ! -f $testWorkingDir/test.jar ]"
}

teardown_suite() {
	rm -rf $workingdir
}

