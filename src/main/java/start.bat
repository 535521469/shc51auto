@echo off 
echo "start 51auto spider"
title 51auto≈¿≥Ê
java -Xms512m -Djava.ext.dirs=../auto51_lib -Dlogback.configurationFile=logback.xml pp.corleone.auto51.service.Auto51Service
pause