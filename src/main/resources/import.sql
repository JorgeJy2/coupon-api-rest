INSERT INTO users (id,username, password, enabled) VALUES(1,'jorge', '$2a$10$JZpk3e7ixJFdC00x6aKfG.sIZ4Zg5sMgxxE05Ki/vX0YFL0JQU9kC', 1);
INSERT INTO users (id, username, password, enabled) VALUES(2,'admin', '$2a$10$s9Io8N2A1QIsDXc6Scg4oOt.XBmZKEU355DLWfumy0Xfs5OSTTcI.', 1);


INSERT INTO authorities (id,user_id, authority) VALUES(1,1, 'ROLE_USER');
INSERT INTO authorities (id,user_id, authority) VALUES(2,2, 'ROLE_USER');
INSERT INTO authorities (id,user_id, authority) VALUES(3,2, 'ROLE_ADMIN');

