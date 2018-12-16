# Start SQL server and run the data setup script
set -e
run_cmd="/usr/src/sql/data-setup.sh"
until $run_cmd & /opt/mssql/bin/sqlservr; do
>&2 echo ""
done
