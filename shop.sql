CREATE TABLE ROLES(
	ID SERIAL PRIMARY KEY,
	NAME VARCHAR(6)
)

CREATE TABLE USERS(
	ID VARCHAR(21) PRIMARY KEY,
	FIRST_NAME VARCHAR(26),
	LAST_NAME VARCHAR(26),
	EMAIL VARCHAR(51),
	PHONE VARCHAR(13),
	GENDER VARCHAR(6),
	ADDRESS VARCHAR(51),
	CREATED_AT DATE,
	MODIFIED_AT DATE
)


CREATE TABLE ACCOUNTS(
	ID VARCHAR(21) PRIMARY KEY,
	USER_ID VARCHAR(21) REFERENCES USERS(ID),
	PASSWORD VARCHAR(151),
	ROLE INT REFERENCES ROLES(ID),
	MODIFIED_AT DATE
)

CREATE TABLE TYPE_PRODUCT(
	ID VARCHAR(21) PRIMARY KEY,
	NAME VARCHAR(51),
	CREATED_AT DATE,
	MODIFIED_AT DATE
)

ALTER TABLE CATEGORIES ADD COLUMN TYPE_PRODUCT VARCHAR(21) REFERENCES TYPE_PRODUCT(ID)

CREATE TABLE CATEGORIES(
	ID VARCHAR(21) PRIMARY KEY,
	NAME VARCHAR(51),
	CREATED_AT DATE,
	MODIFIED_AT DATE,
	TYPE_PRODUCT VARCHAR(21) REFERENCES TYPE_PRODUCT(ID)
)

select * from categories

select * from type_product

CREATE TABLE DISTRIBUTORS(
	ID VARCHAR(21) PRIMARY KEY,
	USER_ID VARCHAR(21) REFERENCES USERS(ID),
	DESCRIPTION TEXT,
	REGISTERED_AT DATE,
	MODIFIED_AT DATE
)


CREATE TABLE PRODUCTS(
	ID VARCHAR(21) PRIMARY KEY,
	NAME VARCHAR(51),
	DISTRIBUTOR_ID VARCHAR(21) REFERENCES DISTRIBUTORS(ID),
	DESCRIPTION TEXT,
	PRICE REAL,
	STATUS BOOLEAN,
	STAR REAL,
	CREATED_AT DATE,
	MODIFIED_AT DATE
)

CREATE TABLE DISCOUNT(
	ID SERIAL PRIMARY KEY,
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCTS(ID),
	VALUE REAL,
	MODIFIED_AT DATE
)

CREATE TABLE CATEGORIES_ITEM(
	ID SERIAL PRIMARY KEY,
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCTS(ID),
	CATEGORY_ID VARCHAR(21) REFERENCES CATEGORIES(ID)
)


CREATE TABLE WAREHOUSE(
	ID SERIAL PRIMARY KEY,
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCTS(ID),
	QUANTITY INT
)

CREATE TABLE HISTORY_WAREHOUSE(
	ID SERIAL PRIMARY KEY,
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCTS(ID),
	TYPE BOOLEAN,
	QUANTITY INT,
	CREATED_AT DATE
)

CREATE TABLE BANNERS(
	ID VARCHAR(21) PRIMARY KEY,
	LINK TEXT,
	STATUS BOOLEAN,
	CREATED_AT DATE
)

CREATE TABLE CARTS(
	ID VARCHAR(21) PRIMARY KEY,
	USER_ID VARCHAR(21) REFERENCES USERS(ID),
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCTS(ID),
	QUANTITY INT,
	STATUS BOOLEAN
)

CREATE TABLE ORDERS(
	ID SERIAL PRIMARY KEY,
	BILL_ID VARCHAR(21) REFERENCES BILLS(ID),
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCTS(ID),
	PRICE REAL,
	QUANTITY INT,
	REVIEWED BOOLEAN
)

UPDATE ORDERS SET REVIEWED = 'FALSE'


CREATE TABLE BILLS(
	ID VARCHAR(21) PRIMARY KEY,
	USER_ID VARCHAR(21) REFERENCES USERS(ID),
	ORDER_TIME TIMESTAMP,
	PRICE REAL
)

CREATE TABLE COMMENT_PRODUCT(
	ID VARCHAR(21) PRIMARY KEY,
	USER_ID VARCHAR(21) REFERENCES USERS(ID),
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCTS(ID),
	CREATED_AT TIMESTAMP,
	MODIFIED_AT TIMESTAMP
)

CREATE TABLE REVIEW_PRODUCT(
	ID VARCHAR(21) PRIMARY KEY,
	USER_ID VARCHAR(21) REFERENCES USERS(ID),
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCTS(ID),
	BILL_ID VARCHAR(21) REFERENCES BILLS(ID),
	CONTENT TEXT,
	STAR INT,
	CREATED_AT TIMESTAMP
)


INSERT INTO ROLES(NAME) VALUES ('ADMIN')
INSERT INTO ROLES(NAME) VALUES ('USER')

DROP TABLE REVIEW_PRODUCT

select * from bills

select * from orders
select * from bills

drop table orders

delete from bills
delete from orders

delete from users

select * from accounts

SELECT * FROM CATEGORIES
select * from users
select * from accounts
select * from distributors
select * from products
select * from discount
select * from warehouse
SELECT * FROM BILLS
select * from history_warehouse
SELECT * FROM ROLES
select * from categories_item
SELECT * FROM CARTS ORDER BY PRODUCT_ID
select * from banners
select * from orders
delete from carts where id='o6zpumgkt7c7yr8v13hg'
select * from type_product


SELECT PRODUCTS.ID, PRODUCTS.NAME, PRODUCTS.DESCRIPTION, PRICE,DISCOUNT.VALUE AS DISCOUNT  FROM PRODUCTS, DISCOUNT WHERE PRODUCTS.ID = DISCOUNT.product_id
SELECT *  FROM PRODUCTS, DISCOUNT WHERE PRODUCTS.ID = DISCOUNT.product_id
UPDATE CARTS SET STATUS = FALSE WHERE USER_ID = '025gfnx1n3gyvu8l78m3'
select * from banners limit NULL offset NULL
delete from users
delete from accounts
delete from distributors

DELETE FROM DISCOUNT
DELETE FROM WAREHOUSE
DELETE FROM PRODUCTS
DELETE FROM categories_item
delete from banners


DROP TABLE ACCOUNTS
DROP TABLE USERS
DROP TABLE CATEGORIES
DROP TABLE DISTRIBUTORS
DROP TABLE CARTS
DROP TABLE discount
DROP TABLE WAREHOUSE
DROP TABLE HISTORY_WAREHOUSE
DROP TABLE PRODUCTS

delete from users where id = 'ic4fuaqce40k1brs3xh0'
delete from accounts where id = 'j80nzor8wwqjnslaowsf'

DROP TABLE WAREHOUSE
DROP TABLE HISTORY_WAREHOUSE
DROP TABLE PRODUCTS
DROP TABLE CARTS

