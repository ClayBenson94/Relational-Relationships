# Relational Relationships

## Synopsis

**Relational Relationships** is a desktop dating application, which allows you to view and match with other users of the system.

## Compilation + Execution:
* Ensure you have Java 8 in your PATH (so that you can run the “java” and “javac” commands)
* In the utilities directory of Relational Relationships, run the “COMPILE_AND_RUN.bat” file (on Windows) or the “COMPILE_AND_RUN.sh” file on *nix based systems
  * You will be prompted if you wish to generate a fresh database. This database is generated on the fly and will create 500 users and 1000 visits
* If you wish to generate more or less content, use the command line flags listed in the command line flags section

## Command Line Flags:
**EXAMPLE:**  java -classpath "out:lib/h2-1.4.191.jar:lib/forms_rt.jar" objects.RelationshipController

* -n generates a fresh database (will overwrite any existing data)
* -l username password will automatically login to the system with the given username and password
* -g numUsers numVisits will generate the specified number of random users and random visits in the database. USE WITH -n FLAG
  * Generating large numbers of users can take some time


## Organization:
* All source code is located in the src folder
* All utilities used for viewing the DB and compiling the application are in the utilities folder
* All resources (images, CSVs) are located in the resources folder
* All libraries (required to be in classpath upon compilation) are in the lib folder

## Usage:
* Login by entering your username, or click the register button to create a new user. (one of the included users username/password is ClayBenson94/password)
  * After logging in you will be presented with a search screen, which by default searches for fall users in your zip code. You can specify a different zip code by typing it in the field and hitting “Search”
* Change your preferences on the Preferences page, accessible from the search screen
  * You can also add and remove photos on this page
  * You may add, remove, and create interests on this page
* Click a user to view detailed information on them
  * You may then “Like” a user, or simply read their information and hit “Back” to bring you to the search page again.
  * Additionally, you may head back to a previously liked user and “Unlike” them, if you decide to change your mind.
* You can view your visits, likes (and matches) by clicking the appropriate buttons at the top of the search page


## Motivation

This project was created for the _Principles of Data Management_ course.

## Collaborators

[Clay Benson](https://github.com/ClayBenson94)

[Nick Coriale](https://github.com/njc3006)

[Jarryd Lee](https://github.com/jarrydlee)

[Joshua Horning](https://github.com/jrh827)

[Robert Adams](https://github.com/robthethird)