FROM maven:3.8.6-ibm-semeru-11-focal as BUILD
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean install -DskipTests=true


FROM ibm-semeru-runtimes:open-11.0.15_10-jdk-focal
COPY --from=BUILD /usr/src/app/target/springjpa-0.0.1-SNAPSHOT.jar ./
CMD ["java", "-jar", "./springjpa-0.0.1-SNAPSHOT.jar"]




