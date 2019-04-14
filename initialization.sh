sed 's/#.*//' apt_requirements | xargs sudo apt-get install
pip3 install -r pip3_requirements

echo "Make sure you have all proxy settings off"
sudo systemctl start docker
sudo systemctl enable docker
docker run -p 6379:6379 -d redis
