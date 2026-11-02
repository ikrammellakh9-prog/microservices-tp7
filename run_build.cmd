@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "C:\Users\hp\Desktop\labs\ikram\TP7-JAXRS-Jersey"
call mvn clean test
