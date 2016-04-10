#!/bin/bash
cd ..
rm -rf "out"
mkdir "out"
javac -Xlint:unchecked -d "out" -cp "lib/forms_rt.jar;lib/h2-1.4.191.jar" src/helpers/*.java src/objects/*.java src/tables/*.java src/ui/*.java
java -classpath "out;lib/h2-1.4.191.jar;lib/forms_rt.jar" objects.RelationshipController