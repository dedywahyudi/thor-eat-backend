# Thor Early Alert Tools REST API Code Challenge

This is the deployment guide for Thor Early Alert Tools REST API Code Challenge - Implement Java Backend API.

## Prerequisites

1. Java 8
2. Maven 3+
3. SQL Server 2017
4. Spring 5+


## SQL Server Setup

- Use the `docker-setup/README.md` provided in my submission to setup the db and run the import:
```
docker exec thor-sqlsrv sh -c "python import.py 'full example data.xlsx'"
```


## API Configuration

### application configuration
Edit file `src/main/resources/application.properties`:  

- **server.port**: the server port on which the api will listen on   
- **spring.datasource.url**: the jdbc url for accessing the database   
- **spring.datasource.username**: the database username  
- **spring.datasource.password**: the database user password  

- **spring.mail.host**: the SMTP server host for sending emails    
- **spring.mail.port**: the SMTP server port for sending emails   
- **spring.mail.username**: the SMTP server username  
- **spring.mail.password**: the SMTP server user password
- **spring.mail.properties.mail.smtp.auth**: required authenticate or not    
- **spring.mail.properties.mail.smtp.starttls.enable**: use tls or not
- **spring.mail.properties.mail.smtp.starttls.required**: is tls required or not
- **paging.default.pagesize** the default page size.

You can keep the rest of the configurations unchanged.

### Unit Tests configuration
Edit file `src/test/resources/test.properties`.
The test configuration is overriding the application configuration, so want you only need to modify is:
 - **spring.datasource.url**: the jdbc url for accessing the database

### Logging configuration

Edit file `src/main/resources/application.properties`:

- **logging.level..com.thor.eat.api**: the log level to be used

For advanced configuration of the logging, edit file: `src/main/resources/logback.xml`.

## Building and Running the app

Set the env variables or modify the config in `src/main/resources/application.properties`   

Take setting the env variables for example, in linux or mac:

```bash
export PORT=8080
export CONTEXT_PATH=/api/v1
export DB_URL=jdbc:sqlserver://192.168.1.101:1433\;databaseName=ThorEarlyAlert
export TEST_DB_URL=jdbc:sqlserver://192.168.1.101:1433\;databaseName=ThorEarlyAlertTest
export DB_USERNAME=sa
export DB_PASSWORD=Topcoder123
export DB_POOLSIZE=128
export MAIL_SMTP_HOST=<YOUR_SMTP_HOST>
export MAIL_SMTP_PORT=<YOUR_SMTP_PORT>
export MAIL_SMTP_USERNAME=
export MAIL_SMTP_PASSWORD=
export MAIL_SMTP_AUTH_REQUIRED=false
export MAIL_SMTP_STARTTLS_ENABLED=false
export MAIL_SMTP_STARTTLS_REQUIRED=false
export LOG_LEVEL=DEBUG
```


```bash
mvn spring-boot:run
```

The default entrypoint is:
```
http://localhost:8081/api/v1
```

## Deploy to JBoss
Go to frontend directory,run the following command:
```
npm install
```
then go back to this directory, package with the following command:
```
mvn clean package -Dmaven.test.skip=true
```
Then you can see that a WAR file is generated in `target/eat.war`

You can use this war to deploy to JBoss by copy this war file to `standalone/deployments` folder in JBoss root directory.
Or you can use the wildfly plugin to deploy to jboss if your jboss is enabled the management, run:

```
mvn wildfly:deploy
```

Then start JBoss by running:
```
bin/standalone.sh
```

Start with following url in browser:
 ```
 http://localhost:8080/
 ```

## SQL Auth (Optional)

To use SQL Auth to connect sql server is easy:
1). in `application.properties` change the connection string to:
```
jdbc:sqlserver://host:port;DatabaseName=dbName;integratedSecurity=true
```

2). make the following configurations empty:
```
spring.datasource.username=
spring.datasource.password=
```

3).
find sqljdbc_auth.dll file from microsoft jdbc driver installation folder,
one you installed previously from the jdbc driver link. The path would be something like: <installation directory>\sqljdbc_<version>\<language>\auth\
4).
copy sqljdbc_auth.dll file to your JAVA_HOME/bin folder.
5).
finally start your jboss using windows credentials that you wanted to used to connect SQL Sever. Now you can inject your datasource anyway you need it to connect the db. That should do it!!


## Verification
### Unit tests
to run the unit tests, run:
```
mvn test
```

### Swagger
Open **http://editor.swagger.io/** and copy  `docs/swagger.yaml` to verify.

### SMTP server
For verification purpose, you can use a fake SMTP server, like: [FakeSMTP](https://github.com/Nilhcem/FakeSMTP)

### Postman

Import Postman collection `docs/postman.json` with environment variables `docs/postman-env.json`.  


### Predefined Accounts


**Admin**
```
username: admin  
password: Password123
```

 **AdminApprover**
```
username: adminapprover
password: Password123
```

 **DataManager**
```
username: datamanager  
password: Password123
```

## Running in Docker
1. Navigate to root of backend folder
2. Make sure you do build latest frontend on frontend folder with `npm install`
3. Make sure you do build latest backend on backend folder with `mvn clean package -Dmaven.test.skip=true`
4. Run `docker-compose build` to build the app in docker
5. Run `docker-compose up` or `docker-compose up -d` to running in background
6. Run `sudo docker exec 8e6ec6215586 sh -c "python import.py 'full example data.xlsx'"` to create sample data from xlxs file
7. NOTE: replace 8e6ec6215586 with `tea-sqlsrv` container id
8. Access app on `http://localhost/eat/`

## Setup CI/CD (Deploy to AWS EC2)
1. Follow sections 1 to 3 in https://hackernoon.com/configuring-gitlab-ci-on-aws-ec2-using-docker-7c359d513a46
   - POINTS TO NOTE:
       - EC2 minimum mpecs: t2.medium
       - In section 2 (Configure Security Group), add the following ports:
           - [Type: Custom TCP Rule, Port Range: 8080, Source: 0.0.0.0/0] -> this is the frontend port
           - [Type: Custom TCP Rule, Port Range: 9090, Source: 0.0.0.0/0] -> this is the backend port
       - Install gitlab runner
           - `sudo apt-get update`
           - `curl -L https://packages.gitlab.com/install/repositories/runner/gitlab-runner/script.deb.sh | sudo bash`
           - `sudo apt-get install gitlab-runner`
       - In section 3 (`sudo gitlab-runner register`), please do this to both backend and frontend gitlab repo
       - Install docker
           - `sudo apt-get update`
           - `sudo apt-get install \
           apt-transport-https \
           ca-certificates \
           curl \
           software-properties-common`
           - `curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -`
           - `sudo add-apt-repository \
          "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
          $(lsb_release -cs) \
          stable"`
           - `sudo apt-get update`
           - `sudo apt-get install docker-ce`

2. Install docker-compose (make sure you are connected to EC2 instance via SSH)
   - `sudo apt install docker-compose`
   - `sudo curl -L https://github.com/docker/compose/releases/download/1.22.0/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose`
   - `sudo chmod +x /usr/local/bin/docker-compose`

3. Add user gitlab-runner to list of sudoers (make sure you are connected to EC2 instance via SSH)
   - `sudo vi /etc/sudoers` and add the following entries
       - `gitlab-runner   ALL=(ALL:ALL) NOPASSWD: ALL`

AWS EC2 Demo: Running on JBOSS EAP 7.0.0
ec2-18-206-71-217.compute-1.amazonaws.com
