#!/usr/bin/env bash
set -e

./gradlew clean bootJar

rm -rf eb-deploy
mkdir eb-deploy
cp build/libs/*.jar eb-deploy/application.jar
echo "web: java -jar application.jar" > eb-deploy/Procfile

cd eb-deploy
zip ../backend-eb.zip application.jar Procfile
cd ..

echo "Created backend-eb.zip"
