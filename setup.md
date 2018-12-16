# SETUP WAR File in JBOSS Server with External Configuration

 ## SQL Server Setup
 Follow the guide on https://gitlab.com/thor-early-alert-tool/architecture/blob/master/docker-setup/README.md to build the database docker image.
 
 ## Building the WAR package
 Clone the frontend from https://gitlab.com/thor-early-alert-tool/early-alert-tool
 
 ```bash
 git clone https://gitlab.com/thor-early-alert-tool/early-alert-tool.git frontend
 ```
 
 Make sure the cloned directory named as "frontend".
 Go to frontend directory, run the following command:
 
 ```bash
 npm install
 ```
 Clone the backend from https://gitlab.com/thor-early-alert-tool/backend
 
 ```bash
 git clone https://gitlab.com/thor-early-alert-tool/backend.git
 ```
 
 Make sure that backend and frontend are on the same base directory.
 Build the WAR package
 
 ```bash
 mvn clean package -Dmaven.test.skip=true
 ```
 It will create `eat.war` in target directory.
 The `application.properties` configuration is now no longer inside created WAR. It is externalized to JBoss modules directory.
 
 ## Link the external configuration
 JBOSS_HOME = /opt/jboss/cnseat/etn-apps
 Grab configuration from here: https://gitlab.com/thor-early-alert-tool/backend/tree/dev/jboss_files/configuration
 copy jboss_files/configuration to $JBOSS_HOME/standalone/configuration
 Grab modules from here: https://gitlab.com/thor-early-alert-tool/backend/tree/dev/jboss_files/modules
 copy jboss_files/modules $JBOSS_HOME/modules
 COPY target/eat.war $JBOSS_HOME/standalone/deployments/
 This will boot JBoss EAP in the standalone mode and bind to all interface
 /opt/jboss/cnseat/etn-apps/bin/standalone.sh
 
 ## Environment parameters
 Right now current solution using database on docker, edit this parameters based on your configuration
 Datasource parameters:
https://gitlab.com/thor-early-alert-tool/backend/blob/dev/jboss_files/modules/eat/main/content/application.properties#L10
https://gitlab.com/thor-early-alert-tool/backend/blob/dev/jboss_files/configuration/standalone.xml#L143