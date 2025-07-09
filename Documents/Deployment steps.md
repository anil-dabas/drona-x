## Steps to create and deploy on AWS

1. Create the instance on AWS
2. Save the Pem file
3. On your terminal
   - Go to Pem file path
   - chmod 400 demo-solver.pem
   - ssh -i demo-solver.pem ubunto@13.60.186.152
   - ssh -i demo-solver.pem ec2-user@13.60.186.152
4. Install Docker 
5. Install docker compose 
6. Copy the docker compose on the server ```scp -i demo-solver.pem docker-compose.dependencies.yml ec2-user@13.60.186.152:/home/ec2-user/```
7. Run ```docker-compose -f docker-compose.dependencies.yml up -d --remove-orphans ``` 
8. Now on the server provide access to git and clone the application 
   1. ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
   2. cat ~/.ssh/id_rsa.pub
   3. ssh -T git@github.com
   4. 
9. cd drona-x
10. Run the application one by one 
    1. ```./gradlew bootRun```
    2. ```nohup ./gradlew bootRun > app.log 2>&1 &```