#!/bin/bash
echo "Trying to replace the placeholders with secret values..."
filename=env.values
sed -ne '/^#/d;s!^\([^=]*\)=\(.*\)$!s|\!\!\!\1\!\!\!|\2|g!p' $filename > script.sed
file=application.properties
sed -i -f script.sed $file ;
echo "Replaced the placeholders successfully!"

echo "Starting the server"
java -jar *.jar