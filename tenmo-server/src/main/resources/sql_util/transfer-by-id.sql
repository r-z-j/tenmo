SELECT transfer_id, amount, 
user_from.username AS username_from, user_to.username AS username_to, 
account_from, account_to,
transfer_status_description, transfer_type_description,
transfer.transfer_status_id, transfer.transfer_type_id
FROM transfer
INNER JOIN account AS acc_from ON account_from = acc_from.account_id
INNER JOIN account AS acc_to ON account_to = acc_to.account_id
INNER JOIN tenmo_user AS user_from ON acc_from.user_id = user_from.user_id
INNER JOIN tenmo_user AS user_to ON acc_to.user_id = user_to.user_id
INNER JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id
INNER JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id
WHERE transfer_id = 5001;