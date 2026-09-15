## Deploy app to EC2 using Docker Compose

### On EC2
- Install Docker Compose:
```bash
sudo curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose version
```
- Create a Docker Compose YAML file; see [commit](https://github.com/JonathanBaqDev/TWN-Jenkins/commit/b8b9bed377698ce10f28283d664f15adbd44fb95).

### Jenkinsfile

- In the deploy stage, copy the **docker-compose.yaml** file to the EC2 instance and run `docker-compose up`; see [commit](https://github.com/JonathanBaqDev/TWN-Jenkins/commit/60cfe825c552d7365283715a70c3113ac5cae3f5).
- Optimise the deploy step by creating a separate shell script; see [commit](https://github.com/JonathanBaqDev/TWN-Jenkins/commit/0d045fa59cb6ca5263f32cf8254d98154eb72469).