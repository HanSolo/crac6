## CRaC 6 demo

### Running the demo in a docker container (on a Linux x64 machine)
#### 1. Create docker image
1. Open a shell window
2. Change to the crac6 folder
3. Run ``` docker build -t crac6 . ``` to build the docker image

</br>

#### 2. Start the application in a docker container
1. Open a shell window
2. Run ``` docker run -it --privileged --rm --name crac6 crac6 ```
3. In the docker container run</br>
   ``` 
   cd /opt/app 
   java -XX:CRaCCheckpointTo=/opt/crac-files -jar crac6-17.0.0.jar
   ```
4. Note the PID of the program

</br>

#### 3. Start a 2nd shell window and create the checkpoint
1. Open a second shell window
2. Run ``` docker exec -it -u root crac6 /bin/bash ```
3. Wait until the program in the first window reaches for example the 17th iteration
4. Take the PID from shell 1 and run ``` jcmd PID JDK.checkpoint```
5. In the first shell window the application should have created the checkpoint
6. In second shell window run ``` exit ``` to get back to your machine

</br>

#### 4. Commit the current state of the docker container
1. Now get the CONTAINER_ID from shell window 1 by execute ``` docker ps -a ``` in shell window 2
2. Run ``` docker commit CONTAINER_ID crac6:checkpoint ``` in shell window 2
3. Go back to shell window 1 and execute ```exit``` to stop the container

</br>

#### 5. Run the docker container from the saved state incl. the checkpoint
Now you can start the docker container from the checkpoint by executing
``` docker run -it --privileged --rm --name crac6 crac6:checkpoint java -XX:CRaCRestoreFrom=/opt/crac-files ```

</br>

#### 6. Create a shell script to restore multiple times
1. Open a shell window
2. Create a text file named ```restore_docker.sh```
3. Add
```
#!/bin/bash

echo "docker run -it --privileged --rm --name $1 crac6:checkpoint java -XX:CRaCRestoreFrom=/opt/crac-files"

docker run -it --privileged --rm --name $1 crac6:checkpoint java -XX:CRaCRestoreFrom=/opt/crac-files
```
4. Make the script executable by executing ```chmod +x restore_docker.sh```
5. Now you can start the docker container multiple times executing ```restore_docker.sh NAME_OF_CONTAINER```

If you would like to start the original container without the checkpoint you can still
do that by executing the following command
```
docker run -it --privileged --rm --name crac6 crac6 java -jar /opt/app/crac6-17.0.0.jar
```