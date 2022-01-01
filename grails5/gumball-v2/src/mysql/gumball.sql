-- noinspection SqlDialectInspectionForFile
-- noinspection SqlNoDataSourceInspectionForFile

/* Create the New Database */
create database gumball;

/* Create DB User */
create user 'admin'@'%' identified by 'password';

/* Gives all privileges to the DB user on the newly created database */
grant all on gumball.* to 'admin'@'%';

/* Create Gumball Domain Table */
CREATE TABLE gumball (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  version bigint(20) NOT NULL,
  count_gumballs int(11) NOT NULL,
  model_number varchar(255) NOT NULL,
  serial_number varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY serial_number (serial_number)
) ;

/* Add Inventory to Gumball Machine */
insert into gumball ( id, version, count_gumballs, model_number, serial_number )
values ( 1, 0, 10, 'M102988', '1234998871109' ) ;

/* Query from Gumball Domain Table */
select * from gumball ;

/* Jumpbox */

make jumpbox-run

apt-get update
apt-get install mysql-client
mysql -u <user> -p -h <db host> <db name>