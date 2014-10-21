/* should be run by postgres super user*/

CREATE ROLE supportsmallshop LOGIN PASSWORD 'password' NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;

CREATE DATABASE supportsmallshop WITH OWNER = supportsmallshop ENCODING = 'UTF8' TABLESPACE = pg_default LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8' CONNECTION LIMIT = -1 template=template0;

ALTER DATABASE supportsmallshop SET timezone TO 'Asia/Hong_Kong';

