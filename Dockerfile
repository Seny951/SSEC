FROM openjdk:19
COPY ./target/JamesSenSquareEnixSSEC-1.0-SNAPSHOT.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java","-jar", "./JamesSenSquareEnixSSEC-1.0-SNAPSHOT.jar"]
EXPOSE 8001/tcp