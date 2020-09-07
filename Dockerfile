FROM openjdk:11.0

RUN mkdir /multiple-entry-points-jar-docker-image
WORKDIR /multiple-entry-points-jar-docker-image
COPY ./target/multiple-entry-points-jar-docker-image-1.0-SNAPSHOT.jar .

# This is set to execute the default entry point method per the jar's manifest, which is determined based on the value of
# the mainClass field in the pom.xml file.
CMD java -jar /multiple-entry-points-jar-docker-image/multiple-entry-points-jar-docker-image-1.0-SNAPSHOT.jar

