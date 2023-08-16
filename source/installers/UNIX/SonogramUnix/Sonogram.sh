#/bin/sh

java -showversion -Xms128m -Xmx2048m --add-opens=java.desktop/sun.awt=ALL-UNNAMED -jar Sonogram.jar
