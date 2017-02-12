# init
docker network rm mynetwork
docker stop pingserver
docker stop pingclient
docker stop data
docker rm pingserver
docker rm pingclient
docker rm data

# compile codes first
javac ./code/*/*.java ./code/*/*/*.java
# init the dockerfile
docker build -t ubuntu ./ubuntu
docker build -t code ./code

# create volume
docker create -v /data --name data code /bin/true

# create network
docker network create -d bridge mynetwork

# start server
docker run -d --net=mynetwork  --name pingserver --volumes-from data ubuntu java -cp ./data server.PingPongServer
# start client
docker run -d --net=mynetwork  --name pingclient --volumes-from data ubuntu java -cp ./data client.PingPongClient

echo !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
echo !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
echo use \"docker logs pingclient\" and \"docker logs pingserver\" to check the running status
echo !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
echo !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
