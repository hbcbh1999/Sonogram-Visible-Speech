# @file         Makefile
# @version      1.0
# @date         2021
# @author       Christoph Lauer
# @client       none
# @contributors none
# @biref        the makefile for build and run 
# @shell        make
# @requires     GNU make utility
# @usage        make all...
# @arguments    
# @see          https://www.gnu.org/software/make/
# @notes        none
# @todo         replace commands with variables
# @copyright    Christoph Lauer
# @license      GPL 3.0


compile:
	javac -d bin src/de/dfki/sonogram/*.java src/de/dfki/maths/*.java -cp bin
	

run:
	cd bin; java -classpath . -splash:de/dfki/sonogram/Splash.png -showversion -Xms128m -Xmx2048m -Xfuture --add-opens=java.desktop/sun.awt=ALL-UNNAMED de.dfki.sonogram.Sonogram
	

compileAndRun: compile run


jar: compile
	cd bin; jar cfm ../installers/Sonogram.jar META-INF/MANIFEST.MF *
	

runJar:
	cd installers; java --add-opens=java.desktop/sun.awt=ALL-UNNAMED -jar Sonogram.jar


releaseMac:
	cp -f installers/Sonogram.jar installers/MACOS/Sonogram.app/Contents/Resources/Java
	rm -f installers/MACOS/Sonogram.app/Contents/Resources/Java/SonogramConfig.xml
	cd installers/MACOS; zip -r SonogramMacOS.zip Sonogram.app/*
	cp -f installers/MACOS/SonogramMacOS.zip ../release
	

releaseWindows:
	cp -f installers/Sonogram.jar installers/WINDOWS/Sonogram\ Windows/
	rm -f installers/WINDOWS/Sonogram\ Windows/SonogramConfig.xml
	cd installers/WINDOWS; zip -r SonogramWindows.zip Sonogram\ Windows/*
	cp -f installers/Windows/SonogramWindows.zip ../release


releaseUnix:
	cp -f installers/Sonogram.jar installers/UNIX/SonogramUnix/
	rm -f installers/UNIX/SonogramUnix/SonogramConfig.xml
	cd installers/UNIX; zip -r SonogramUnix.zip SonogramUnix/*
	cp -f installers/UNIX/SonogramUnix.zip ../release


all: jar releaseMac releaseWindows releaseUnix installMacos


clean:
	find bin/de/dfki -name "*.class" -exec rm -rfv {} \;


installMacos: jar
	cp -fv installers/Sonogram.jar installers/MACOS/Sonogram.app/Contents/Resources/Java
	rm -fv installers/MACOS/Sonogram.app/Contents/Resources/Java/SonogramConfig.xml
	rm -rfv /Applications/Sonogram.app
	cp -rv installers/MACOS/Sonogram.app /Applications


runMacos:
	open -a Sonogram
