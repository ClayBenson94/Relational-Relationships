#!/bin/bash
cd ..
rm -rf "out"
mkdir "out"
javac -Xlint:unchecked -d "out" -cp "lib/forms_rt.jar:lib/h2-1.4.191.jar" src/helpers/*.java src/objects/*.java src/tables/*.java src/ui/*.java

read -p "Generate Fresh Database[Y/N]? " -r
echo    # (optional) move to a new line
if [[ ! $REPLY =~ ^[Yy]$ ]]
then
    java -classpath "out:lib/h2-1.4.191.jar:lib/forms_rt.jar" objects.RelationshipController -n -g 1000 500
else
	java -classpath "out:lib/h2-1.4.191.jar:lib/forms_rt.jar" objects.RelationshipController
fi