BEGIN TRANSACTION;

DROP TABLE IF EXISTS transfer_type CASCADE;
DROP SEQUENCE IF EXISTS seq_transfer_type_id CASCADE;

DROP TABLE IF EXISTS transfer_status CASCADE;
DROP SEQUENCE IF EXISTS seq_transfer_status_id CASCADE;

DROP TABLE IF EXISTS transfer CASCADE;
DROP SEQUENCE IF EXISTS seq_transfer_id CASCADE;

DROP TABLE IF EXISTS account CASCADE;
DROP SEQUENCE IF EXISTS seq_account_id CASCADE;

DROP TABLE IF EXISTS tenmo_user CASCADE;
DROP SEQUENCE IF EXISTS seq_user_id CASCADE;


CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) UNIQUE NOT NULL,
	password_hash varchar(200) NOT NULL,
	role varchar(20),
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username));

CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance numeric(6,2),
	CONSTRAINT PK_account_id PRIMARY KEY (account_id),
	CONSTRAINT FK_user_id FOREIGN KEY(user_id) REFERENCES tenmo_user(user_id));


CREATE SEQUENCE seq_transfer_type_id
	INCREMENT BY 1
	START WITH 3001
	NO MAXVALUE;

CREATE TABLE transfer_type (
	transfer_type_id int NOT NULL DEFAULT nextval('seq_transfer_type_id'), 
	transfer_type_description varchar(200) NOT NULL,
	CONSTRAINT PK_tranfer_type_id PRIMARY KEY (transfer_type_id));
	
CREATE SEQUENCE seq_transfer_status_id
	INCREMENT BY 1
	START WITH 4001
	NO MAXVALUE;

CREATE TABLE transfer_status (
	transfer_status_id int NOT NULL DEFAULT nextval('seq_transfer_status_id'), 
	transfer_status_description varchar(200) NOT NULL,
	CONSTRAINT PK_tranfer_status_id PRIMARY KEY (transfer_status_id));	
	
CREATE SEQUENCE seq_transfer_id
	INCREMENT BY 1
	START WITH 5001
	NO MAXVALUE;

CREATE TABLE transfer (
	transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
	amount numeric(6,2) NOT NULL,
	transfer_status_id int NOT NULL,
	transfer_type_id int NOT NULL,
	account_from int NOT NULL, 
	account_to int NOT NULL,
	CONSTRAINT PK_tranfer_id PRIMARY KEY (transfer_id),
	CONSTRAINT FK_account_from FOREIGN KEY(account_from) REFERENCES account(account_id),
	CONSTRAINT FK_account_to FOREIGN KEY(account_to) REFERENCES account(account_id),
	CONSTRAINT FK_transfer_status_id FOREIGN KEY(transfer_status_id) REFERENCES transfer_status(transfer_status_id),
	CONSTRAINT FK_transfer_type_id FOREIGN KEY(transfer_type_id) REFERENCES transfer_type(transfer_type_id));

INSERT INTO transfer_status (transfer_status_description) VALUES ('Pending') RETURNING transfer_status_id; -- 4001
INSERT INTO transfer_status (transfer_status_description) VALUES ('Approved') RETURNING transfer_status_id; -- 4002
INSERT INTO transfer_status (transfer_status_description) VALUES ('Rejected') RETURNING transfer_status_id; -- 4003

INSERT INTO transfer_type (transfer_type_description) VALUES ('Request') RETURNING transfer_type_id; -- 3001
INSERT INTO transfer_type (transfer_type_description) VALUES ('Send') RETURNING transfer_type_id; -- 3002


COMMIT;
