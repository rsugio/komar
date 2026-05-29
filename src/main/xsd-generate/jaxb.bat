@echo off
set xjc=D:\bin\sapjvm_8\bin\xjc.exe
set target=..\java
rem %xjc% -d ..\java -p connector -catalog catalog.txt connector_1_5.xsd j2ee_1_4.xsd j2ee_web_services_client_1_1.xsd

rem %xjc% -d %target% -p connector15 -catalog catalog.txt connector_1_5.xsd
rem %xjc% -d %target% -p connectorj2eeengine -catalog catalog.txt connector-j2ee-engine.xsd -b connector-j2ee-engine.xjb
rem %xjc% -d %target% -p applicationj2eeengine application-j2ee-engine_customized.xsd
rem %xjc% -d %target% -p provider -dtd library.provider.dtd 
rem %xjc% -d %target% -p logConfiguration -dtd log-configuration_custom.dtd 
rem %xjc% -d %target% -p application13 -catalog catalog.txt -dtd application_1_3.dtd
%xjc% -d %target% -p adaptermetadata adapterMetadata.xsd
