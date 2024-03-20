# SynthaX

## Installation instructions
1. Install SynthaX as a zip folder.
2. Extract the zip folder and open the directory up in IntelliJ.
3. Go to File > Project Structure and make sure that the SDK is set to 21.0.0 or above and that language level is set to SDK default.

There are multiple ways to execute SynthaX. Below are two ways one can execute it.
Directly through main application or through Maven:
1. Navigate to MainApplication.java by going SynthaX-master > Program > src > main > java > com.synthax > and then execute MainApplication.java.
If errors would occur while executing the java file, do these following steps:
1. To the right side there is a vertical bar. Click on "m" that is maven.
2. Press plugins and execute the install:install jar file that is within install and then compiler:compile that is within compiler.
3. You can execute SynthaX by doing it two ways. Either to execute the MainApplication.java file or execute the javafx.run file that is in the maven bar.

If errors still persist, navigate to pom.xml that is within SynthaX's directory. Right-click on it, go to Maven and then click on "Unlink Maven Projects". The directory will look slightly different. Right-click on pom.xml again and click on Maven then "Add as Maven project". Repeat the install:install and compiler:compile process again and then try to run MainApplication.java or javafx:run located to the right side of the screen. 
