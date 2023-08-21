sudo docker-compose down
sudo docker rmi front
sudo docker rmi back
sudo docker rmi socket
cd Back-End/fashionity
sudo chmod u+x gradlew
./gradlew bootjar
sudo docker build -t back .
cd ../socketserver
sudo chmod u+x gradlew
./gradlew bootjar
sudo docker build -t socket .
cd ../../Front-End/fashionity
sudo docker build -t front .
cd ../..
sudo docker-compose up -d