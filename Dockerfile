FROM jboss/base-jdk:8

# Set the JBOSS_VERSION env variable
ENV JBOSS_VERSION 7.0.0
ENV JBOSS_HOME /opt/jboss/cnseat/etn-apps

# Copy the JBoss EAP archive to /opt/jboss
COPY jboss-eap-$JBOSS_VERSION.zip $HOME

# Unzip the archive and remove when it's done
RUN cd $HOME \
    && unzip jboss-eap-$JBOSS_VERSION.zip -d cnseat \
    && mv $HOME/cnseat/jboss-eap-7.0 $HOME/cnseat/etn-apps \
    && rm jboss-eap-$JBOSS_VERSION.zip

# Ensure signals are forwarded to the JVM process correctly for graceful shutdown
ENV LAUNCH_JBOSS_IN_BACKGROUND true

# Copy datasource configuration
copy jboss_files/configuration $JBOSS_HOME/standalone/configuration

# Copy module
copy jboss_files/modules $JBOSS_HOME/modules

# Copy war to deployments folder
COPY target/eat.war $JBOSS_HOME/standalone/deployments/

# User root to modify war owners
USER root

# Modify owners war
RUN chown jboss:jboss $JBOSS_HOME/standalone/deployments/eat.war

# Important, use jboss user to run image
USER jboss

# Expose the ports we're interested in
EXPOSE 8080 9990

# Set the default command to run on boot
# This will boot JBoss EAP in the standalone mode and bind to all interface
CMD ["/opt/jboss/cnseat/etn-apps/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
