@echo off
echo Compiling Legends of Valor...
if not exist out mkdir out
javac -d out src/*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed! Please check for errors.
    pause
    exit /b %ERRORLEVEL%
)

echo Compilation successful! Running the game...
java -cp out Main

pause 