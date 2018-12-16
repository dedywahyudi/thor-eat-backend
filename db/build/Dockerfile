FROM microsoft/mssql-server-linux:latest

# Install required packages
RUN echo 'debconf debconf/frontend select Noninteractive' | debconf-set-selections
RUN apt-get -y update && \
    ACCEPT_EULA=Y apt-get install -y python python-pip python-bcrypt msodbcsql17 unixodbc-dev

RUN pip install openpyxl pyodbc

# Create base directory in the container
RUN mkdir -p /usr/src/sql
RUN mkdir -p /usr/src/sql/excel
WORKDIR /usr/src/sql

# Copy working files
COPY . /usr/src/sql

# Setup SQL script
# Make it xecutable
RUN chmod +x /usr/src/sql/data-setup.sh

CMD /bin/bash ./start.sh