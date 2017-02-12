# init
docker stop pingserver
docker stop pingclient
docker stop data
docker rm pingserver
docker rm pingclient
docker rm data
docker network rm mynetwork

# compile codes first
javac ./code/*/*.java ./code/*/*/*.java

# init the dockerfile
docker build -t ubuntu ./ubuntu
docker build -t code ./code

# create volume
docker create -v /data --name data code /bin/true

# create network
docker network create mynetwork --subnet 172.18.0.0/16

# start server
docker run -d --net=mynetwork --ip 172.18.0.2 --name pingserver --volumes-from data ubuntu java -cp ./data server.PingPongServer
# start client
docker run -d --net=mynetwork --ip 172.18.0.3 --name pingclient --volumes-from data ubuntu java -cp ./data client.PingPongClient


echo use \"docker logs pingclient\" and \"docker logs pingserver\" to check the running status