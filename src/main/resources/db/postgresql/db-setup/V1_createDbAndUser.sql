create role myuser with password 'mypassword';
ALTER ROLE myuser with LOGIN;
create database documentsdb with owner myuser;