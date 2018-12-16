#!/bin/sh

# wait for the SQL Server to come up
sleep 10s
until /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Topcoder123 -Q 'select 1'
do
  sleep 10s
done


DB_NAME="ThorEarlyAlert"

TEST_DB_NAME="ThorEarlyAlertTest"

# Run the setup DDL script to create the DB and the tables
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Topcoder123 -d master -i db.sql
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Topcoder123 -d $DB_NAME -i ddl.sql
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Topcoder123 -d $DB_NAME -i init.sql

/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Topcoder123 -d $TEST_DB_NAME -i ddl.sql

# on Mac
# sqlcmd -S localhost -U sa -P Topcoder123 -d master -i db.sql
# sqlcmd -S localhost -U sa -P Topcoder123 -d ThorEarlyAlert -i ddl.sql
# sqlcmd -S localhost -U sa -P Topcoder123 -d ThorEarlyAlert -i data.sql
# sqlcmd -S localhost -U sa -P Topcoder123 -d ThorEarlyAlertTest -i ddl.sql
