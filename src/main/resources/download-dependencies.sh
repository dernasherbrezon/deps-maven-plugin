#!/bin/bash

# input parameters:
#   1) Directory with repositories.txt and dependencies.txt files
#   2) Directory where to output downloaded dependencies

set -e
set -o pipefail

if [ ! -f $1/repositories.txt ]; then
	echo "$1/repositories.txt doens't exist"
	exit 1;
fi

if [ ! -f $1/dependencies.txt ]; then
	echo "$1/dependencies.txt doens't exist"
	exit 1;
fi

unset REPOSITORIES
while IFS= read -r; do
    REPOSITORIES+=("$REPLY")
done < $1/repositories.txt

[[ $REPLY ]] && REPOSITORIES+=("$REPLY")

unset DEPENDENCIES
while IFS= read -r; do
    DEPENDENCIES+=("$REPLY")
done < $1/dependencies.txt

[[ $REPLY ]] && DEPENDENCIES+=("$REPLY")

function downloadFromRepositories {
	for repository in "${REPOSITORIES[@]}"
	do
		wget -O $1 $repository$2
		if [ $? -eq 0 ]; then
			return 0
		else
			rm $1
			echo "trying next repo"
		fi 
	done
	return 1
}

unset expectedFilenames

for dependency in "${DEPENDENCIES[@]}"
do
	filename="$2/${dependency##*/}"
	if [ ! -f $filename ]; then
		downloadFromRepositories $filename $dependency
		if [ $? -ne 0 ]; then
			echo "unable to download"
			exit 1
		fi
	else
		echo "dependency already downloaded: $filename"
	fi
	expectedFilenames+=(filename)
done

//FIXME
while read -r line; do
    echo "delete unexpected: $2/$line"
    rm $2/$line
done <<< `comm -23  <(ls $2/*.jar | sort) <(printf '%s\n' "${expectedFilenames[@]}" | sort)`
