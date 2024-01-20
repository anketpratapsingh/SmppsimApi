FROM tomcat:8.0-jre8

# Remove the default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your Spring Boot WAR file to the Tomcat webapps directory
COPY target/SmppsimApi.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]