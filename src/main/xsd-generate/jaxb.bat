@echo off
set xjc=D:\bin\sapjvm_8\bin\xjc.exe
set target=..\java
rem %xjc% -d ..\java -p connector -catalog catalog.txt connector_1_5.xsd j2ee_1_4.xsd j2ee_web_services_client_1_1.xsd
%xjc% -d %target% -p connector15 -catalog catalog.txt connector_1_5.xsd
%xjc% -d %target% -p connectorj2eeengine -catalog catalog.txt connector-j2ee-engine.xsd -b connector-j2ee-engine.xjb

