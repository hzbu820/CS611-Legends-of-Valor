#!/bin/bash
echo "Compiling Legends of Valor..."
mkdir -p out
javac -d out src/*.java

if [ $? -ne 0 ]; then
    echo "Compilation failed! Please check for errors."
    read -p "Press enter to continue..."
    exit 1
fi

echo "Compilation successful! Running the game..."
java -cp out Main

read -p "Press enter to continue..." 