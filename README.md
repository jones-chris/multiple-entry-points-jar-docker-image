# multiple-entry-points-jar-docker-image

This repo is a Proof of Concept showing different ways a jar can have multiple entry points.

This repo contains a `Main` class, which is listed in the pom.xml file as the main class.  The `main` method 
contained in this class will be the default entry point when executing the jar.  

This repo also contains an `AnotherEntryPoint` class, which is **not** listed in the pom.xml file, so it is
not the default entry point when executing the jar.

**NOTE:  All entry point methods must be named `main` and be `public static void` or the execution will fail.**

There are 3 methods of implementing multiple jar entry points in this repo:

###Method #1. Specifying the entry point in the `java -jar` command

1. Open the pom.xml file and make sure the `mainClass` value is `Main`.
2. `cd` to this project's root directory.
3. Run `mvn clean package` to create a jar file in the project's `/target` directory.
4. Run `java -jar ./target/multiple-entry-points-jar-docker-image-1.0-SNAPSHOT.jar`.  You should see `In the main method` output in your terminal.
5. Run `java -cp ./target/multiple-entry-points-jar-docker-image-1.0-SNAPSHOT.jar AnotherEntryPoint`.  You should see `In the anotherEntryPoint method` output in your terminal.

###Method #2. Executing a method using a single `public static void main` method, which executes a method based on the command line `args`

1. Open the pom.xml file and change the `mainClass` value from `Main` to `MainArgsRouter`.
2. `cd` to the project's root directory.
3. Run `mvn clean pacakge` to create a jar file in the project's `/target` directory.
4. Run `java -jar ./target/multiple-entry-points-jar-docker-image-1.0-SNAPSHOT.jar`.  You should see `In the main method` output in your terminal.
5. Run `java -jar ./target/multiple-entry-points-jar-docker-image-1.0-SNAPSHOT.jar anotherEntryPoint`.  You should see `In the anotherEntryPoint method` output in your terminal.
6. Run `java -jar ./target/multiple-entry-points-jar-docker-image-1.0-SNAPSHOT.jar nonExistentMethod`.  An exception stacktrace should be output in your terminal.

###Method #3. Executing a method using a single `public static void main` method, which executes a method based on the command line named arguments

This option is similar to the previous option, except instead of relying on the method to execute being the 0th element in the method's
`args` array (which comes from the command line), we pass the method to execute into the command line as a named option when executing the jar.  This 
gives us the advantage of getting the method to execute by name instead of relying on remembering the order of command line arguments. 

1. Open the pom.xml file and chang the `mainClass` value from `MainArgsRouter` to `MainSystemArgsRouter`.
2. `cd` to the project's root directory.
3. Run `mvn clean package` to create a jar file in the project's `/target` directory.
4. Run `java -jar ./target/multiple-entry-points-jar-docker-image-1.0-SNAPSHOT.jar`.  You should see `In the main method` in your terminal.
5. Run `java -DentryPointMethod=anotherEntryPoint -jar ./target/multiple-entry-points-jar-docker-image-1.0-SNAPSHOT.jar`.  You should see `In the anotherEntryPoint method` in your terminal.
6. Run `java -DentryPointMethod=nonExistentMethod -jar ./target/multiple-entry-points-jar-docker-image-1.0-SNAPSHOT.jar`.  An exception stacktrace should be output in your terminal.

##Multiple jar entry points in a Docker image

Additionally, we can execute multiple jar entry points when running a jar in a Docker container. 

**NOTE:  The steps below assume you just completed Method #3 above.**

1. `cd` to the project's root directory.
2. Run `docker image build -t joneschris/multiple-jar-entry-points:1 .` to build the image.
3. Run `docker run joneschris/multiple-jar-entry-points:1` to run the image in a container.  You should see `In the main method` output in the terminal.
4. Run `docker run --entrypoint '/bin/sh' joneschris/multiple-jar-entry-points:1 -c 'java -DentryPointMethod=anotherEntryPoint -jar /multiple-entry-points-jar-docker-image/multiple-entry-points-jar-docker-image-1.0-SNAPSHOT.jar'`.  
You should see `In the anotherEntryPoint method` output in the terminal.
5. Run `docker run --entrypoint '/bin/sh' joneschris/multiple-jar-entry-points:1 -c 'java -DentryPointMethod=nonExistentMethod -jar /multiple-entry-points-jar-docker-image/multiple-entry-points-jar-docker-image-1.0-SNAPSHOT.jar'`.
An exception stacktrace should be output in your terminal.

##Conclusion
Hopefully this has been helpful in showing how you can have multiple entry points in a single jar file.  Some use cases for this are:

1. Running scheduled tasks in Kubernetes or AWS ECS where the scheduled task makes use of logic that is also contained within the 
jar.  For example, if we have a scheduled task that needs to call a service class in the jar, we could write the scheduled task
class and wrap it's logic in a `public static void main` method.  After building our Docker image, both the scheduled task and
service logic are contained in the same jar.  This gives us the option of 1) running a container beside other containers in the cluster 
and 2) creating a cron job or scheduled task to execute the scheduled task class' `public static void main` method as it's entry point if
we were to override the Docker image's `CMD`.  By doing this, we avoid having to expose the service class as an API endpoint, 
write the scheduled task as a client that calls the service over HTTP, and code auth logic on both the client and server side.
   
