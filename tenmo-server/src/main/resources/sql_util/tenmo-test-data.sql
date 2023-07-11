
BEGIN TRANSACTION;

INSERT INTO transfer_status (transfer_status_description) VALUES ('Pending');
INSERT INTO transfer_status (transfer_status_description) VALUES ('Approved');
INSERT INTO transfer_status (transfer_status_description) VALUES ('Rejected');

INSERT INTO transfer_type (transfer_type_description) VALUES ('Request');
INSERT INTO transfer_type (transfer_type_description) VALUES ('Send');

INSERT INTO tenmo_user (username,password_hash,role) VALUES ('tuser1','user1','ROLE_USER'); -- 1001
INSERT INTO tenmo_user (username,password_hash,role) VALUES ('tuser2','user2','ROLE_USER'); -- 1002
INSERT INTO tenmo_user (username,password_hash,role) VALUES ('tuser3','user3','ROLE_USER');

INSERT INTO transfer(
	amount, transfer_status_id, transfer_type_id, account_from, account_to)
	VALUES (25.00, 4002, 3002, 2001, 2002);

COMMIT TRANSACTION;