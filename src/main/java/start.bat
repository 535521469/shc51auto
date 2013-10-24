@echo off 
echo "start 51auto spider"
java -Xms512m -Xmx1024m -Djava.ext.dirs=../auto51_lib -Dlogback.configurationFile=logback.xml pp.corleone.auto51.service.Auto51Service
pause