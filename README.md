# Sonogram Visible Speech 5.0
[![Alt text](images/Splash.png)](https://youtu.be/jDCRvBTwXuE)

Sonogram visible speech has been programmed at the German Research Center for Artifical Intelligence  (Deutsches Forschungszentrum für Kuenstliche Intelligenz DFKI, www.dfki.de), and is a 3D tool to analyze speech and sound signals with STFT and various other algorithms. The software originates from basic research and the attempt to build a speech recognizer. The new version **requires Java at least in version 16**. If you have any **questions or problems running or compiling the Sonogram, don't hesitate to contact us.** The program was written with [emacs](https://www.gnu.org/software/emacs/).

Contact: [Christoph Lauer](https://christoph-lauer.github.io), christophlauer@me.com

![](https://komarev.com/ghpvc/?username=Christoph-Lauer&color=blue&label=Page+Views)

## Download
**Note:** You have to be Java at least in version 16 installed on your Computer in order to use Sonogram. See [Oracle Java](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html) or [AdoptOpenJDK](https://adoptopenjdk.net). __Apple Silicon (ARM)__ users should use the native [Azul Zulu](https://www.azul.com/downloads/) JDK or JRE. 

<img align="left" width="109" height="82" padding="10" src="images/SonogramIcon.png">

* [Download](https://github.com/Christoph-Lauer/Sonogram-Visilbe-Speech/releases/download/v5.0/SonogramMacOS.zip) Sonogram for **macOS**
* [Download](https://github.com/Christoph-Lauer/Sonogram-Visilbe-Speech/releases/download/v5.0/SonogramWindows.zip) Sonogram for **Windows**
* [Download](https://github.com/Christoph-Lauer/Sonogram-Visilbe-Speech/releases/download/v5.0/SonogramUnix.zip) Sonogram for **Unix/linux**

[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=NJ7YC6GJT5QJA) (Sonogram is free, but please support the further Development of Sonogram Visible Speech)

## Screenshots
![alt text](images/1.png)
![alt text](images/2.png)
![alt text](images/6.png)
![alt text](images/3.png)
![alt text](images/4.png)
![alt text](images/5.png)
![alt text](images/7.png)
![alt text](images/8.png)
![alt text](images/9.gif)

## Support
If you have trouble with the Sonogram settings you can **start Sonogram with CAPS-LOCK enabled**.

![alt text](images/cl1.jpeg)                 

a reset screen is shown:

![alt text](images/cl2.png)                 


## Build
You can build Sonogram by you own. First you have to install a java SDK at least in version 16. Make sure Java is in your `PATH` variable.

`cd source`

`make all`

**Note:** If you determine memory problems (for example in the 3D generator) give the java VM more heap memory with the **xmx** flag in the corresponding startup scrips and the makefile.

## Pages who use Sonogram
* http://www.birdsongs.it
* http://www.naturesound.it
* https://www.web3.lu/spectrogram-speech-processing/
* https://www.youtube.com/watch?v=YQFOLmYhq50?t=460
* https://www.dfki.de/nite/anviltools/Sonogram.pdf
