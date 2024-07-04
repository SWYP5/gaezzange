#!/bin/bash

set -e

mysql -u root -p"${MYSQL_ROOT_PASSWORD}" <<-EOSQL
  -- Allow remote access to root user
  ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '${MYSQL_ROOT_PASSWORD}';
  GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

  -- Allow remote access to MYSQL_USER
  CREATE USER IF NOT EXISTS '${MYSQL_USER}'@'%' IDENTIFIED BY '${MYSQL_PASSWORD}';
  GRANT ALL PRIVILEGES ON *.* TO '${MYSQL_USER}'@'%';
  FLUSH PRIVILEGES;
EOSQL
