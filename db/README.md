### Requirements
* Docker
* Linux distribution (to run `docker build`)

### Important Notes
The Excel files are expected to be in the format provided as shown in the sample. Thus, the following assumptions are made for the import script:
* There are 2 sheets in the Excel file. The first and second sheets correspond to "Standard" and "CnSIssue" data respectively.
* The first row of each sheet will always contain the confidential information text.
* The second row of each sheet will always contain the column names.

### Configuration and File Changes
* Extract the submission and change the working directory to the `<submission>/build` folder
* Edit `build/data-setup.sh` and update the values for `ADMIN_NAME`, `ADMIN_EMAIL` and `ADMIN_PASSWORD` as may be required.
* Update all occurrences `Topcoder123` with a password of your choice. Note that the password needs to contain at least alphabets and numbers, or the SQL Server docker image will not start. This password should also be updated in the `docker run` command.
* Edit `build/import.py` and update all occurrences of `Topcoder123` if you performed the update in the previous step.

### Build the Docker Image
* Extract the submission and change the working directory to the `<submission>/build` folder
* Run `sudo docker build -t tea-sqlsrv .` in the folder. This will create a docker image that you can run, and also publish to Docker hub. The image will be tagged with the name `tea-sqlsrv`. You can change this tag to any value, but remember to change all references to the tag when using `docker run`.

### Run the Docker Image
* Run the Docker image using the command below. Replace `<submission>/excel` with the actual path to the folder where the Excel files for import will be stored. If this folder does not exist, the import script will not work. Remember to update `Topcoder123` if you changed the password in the configuration. Also, remember to update the `tea-sqlsrv` tag if you used a different tag name when building the image.
```
sudo docker run -e "ACCEPT_EULA=Y" \
    -e "MSSQL_SA_PASSWORD=Topcoder123" \
    -p 1433:1433 \
    --name thor-sqlsrv \
    -v <submission>/excel:/usr/src/sql/excel \
    -d tea-sqlsrv:latest
```

### Run the Import Script
* Run `sudo docker ps -a` to view the list of running containers. This will provide output similar to the following:
```
CONTAINER ID        IMAGE                  COMMAND                  CREATED             STATUS                     PORTS                    NAMES
18164193bc61        tea-sqlsrv:latest      "/bin/sh -c '/bin/ba…"   2 minutes ago       Up 2 minutes               0.0.0.0:1433->1433/tcp   thor-sqlsrv
```
* Wait up to 60 seconds for the SQL Server image to be initialised and the initial DDL script and admin user creation script to run.
* Copy the "full example data.xlsx" file (or any other file that matches the sample data format) to the path that you specified for storing import Excel files.
* Run the command `sudo docker exec 8e6ec6215586 sh -c "python import.py 'full example data.xlsx'"` to import the data from the sample Excel sheet. Note that `8e6ec6215586` is the "CONTAINER ID" value from the `docker ps` output. Make sure to replace this with the value from your command output.


### Verification
* On Linux, you can use SQL Operations Studio to connect to the local SQL Server instance, verify that the database was created and the data was imported successfully.
* On Windows, you can make use of SQL Server Management Studio to connect to the SQL Server instance.
