CREATE TABLE ACCOUNT (
  ACCOUNT_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
  NAME VARCHAR(128) NOT NULL UNIQUE,
  PASSWORD VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS AUTH_ACCOUNT_GROUP (
  AUTH_ACCOUNT_GROUP_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
  NAME VARCHAR(128) NOT NULL,
  AUTH_GROUP VARCHAR(128) NOT NULL,
  CONSTRAINT ACCOUNT_AUTH_USER_GROUP_FK FOREIGN KEY(NAME) REFERENCES ACCOUNT(NAME),
  UNIQUE (NAME, AUTH_GROUP)
);

CREATE TABLE IF NOT EXISTS guestbook_post
(
   id bigint PRIMARY KEY NOT NULL,
   approved_flag bit,
   date_submit timestamp,
   date_approve timestamp,
   image_content longblob,
   post_image_label varchar(255),
   post_text varchar(255),
   size bigint,
   account_account_id bigint
);
ALTER TABLE IF NOT EXISTS guestbook_post
ADD CONSTRAINT FKqc3fr0s376w3fmyhpk900uemv
FOREIGN KEY (account_account_id)
REFERENCES account(ACCOUNT_ID);
CREATE INDEX FKqc3fr0s376w3fmyhpk900uemv ON guestbook_post(account_account_id);