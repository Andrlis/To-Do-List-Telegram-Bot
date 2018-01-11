SET NAMES 'utf8';
SET SESSION collation_connection = 'utf8_general_ci';

CREATE DATABASE to_do_bot DEFAULT CHARACTER SET 'utf8';

USE to_do_bot;

CREATE TABLE users(
	user_id INTEGER AUTO_INCREMENT,
	telegram_id INTEGER NOT NULL,
	PRIMARY KEY(user_id)
);

CREATE TABLE to_do_lists(
	list_id INTEGER AUTO_INCREMENT,
	user_id INTEGER NOT NULL,
	list_name VARCHAR(100) NOT NULL,
	PRIMARY KEY(list_id),
	FOREIGN KEY(user_id) REFERENCES users(user_id)
);

CREATE TABLE lits_items(
	item_id INTEGER AUTO_INCREMENT,
	list_id INTEGER NOT NULL,
	item_description VARCHAR(140) NOT NULL,
	item_status BOOLEAN NOT NULL,
	PRIMARY KEY(item_id),
	FOREIGN KEY(list_id) REFERENCES to_do_lists(list_id)
);