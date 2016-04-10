@echo off
cd ..
rmdir /s /q	"out"
mkdir "out"
javac -Xlint:unchecked -d "out" -cp "lib\forms_rt.jar;lib\h2-1.4.191.jar" src\helpers\*.java src\objects\*.java src\tables\*.java src\ui\*.java

echo.
echo.
set /P c=Generate Fresh Database[Y/N]?
if /I "%c%" EQU "Y" java -classpath "out;lib\h2-1.4.191.jar;lib\forms_rt.jar" objects.RelationshipController -n
if /I "%c%" EQU "N" java -classpath "out;lib\h2-1.4.191.jar;lib\forms_rt.jar" objects.RelationshipController