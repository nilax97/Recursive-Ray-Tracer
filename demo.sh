cd src
javac -cp json-20171018.jar *.java
java -cp .:json-20171018.jar Main ../examples/config1.json ../examples/scene1.png 3
java -cp .:json-20171018.jar Main ../examples/config2.json ../examples/scene2.png 3
java -cp .:json-20171018.jar Main ../examples/config3.json ../examples/scene3.png 3
rm *.class
cd ..