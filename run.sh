#! /usr/bin/env sh

java -classpath junit-r4.11/bin/:junit-r4.11/lib/hamcrest-core-1.3.jar -javaagent:junit-r4.11/lib/ossrewriter-1.0.jar kuleuven.group6.Daemon kuleuven.group6.tests.testsubject.tests.AllTests junit-r4.11/testsubjectbin/source/ junit-r4.11/testsubjectbin/tests/
