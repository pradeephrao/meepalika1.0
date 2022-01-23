FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
#COPY ./target/hms-apinextgen2.0-0.0.1-SNAPSHOT.jar hms.jar 
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container","-jar","/hms.jar"]]
CMD [""]
