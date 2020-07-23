INSERT INTO HERO(ID, HERO_NAME, CREATED_BY, CREATED_DATE) VALUES(NEXTVAL('HERO_SEQ'), 'Dr Nice', 'admin', to_date('01-07-2019', 'dd-MM-yyyy'));
INSERT INTO HERO(ID, HERO_NAME, CREATED_BY, CREATED_DATE) VALUES(NEXTVAL('HERO_SEQ'), 'Narco', 'admin', to_date('01-07-2019', 'dd-MM-yyyy'));
INSERT INTO HERO(ID, HERO_NAME, CREATED_BY, CREATED_DATE) VALUES(NEXTVAL('HERO_SEQ'), 'Bombasto', 'admin', to_date('01-07-2019', 'dd-MM-yyyy'));
INSERT INTO HERO(ID, HERO_NAME, CREATED_BY, CREATED_DATE) VALUES(NEXTVAL('HERO_SEQ'), 'Celeritas', 'admin', to_date('01-07-2019', 'dd-MM-yyyy'));
INSERT INTO HERO(ID, HERO_NAME, CREATED_BY, CREATED_DATE) VALUES(NEXTVAL('HERO_SEQ'), 'Magneta', 'admin', to_date('01-07-2019', 'dd-MM-yyyy'));
INSERT INTO HERO(ID, HERO_NAME, CREATED_BY, CREATED_DATE) VALUES(NEXTVAL('HERO_SEQ'), 'RubberMan', 'admin', to_date('01-07-2019', 'dd-MM-yyyy'));
INSERT INTO HERO(ID, HERO_NAME, CREATED_BY, CREATED_DATE) VALUES(NEXTVAL('HERO_SEQ'), 'Dynama', 'admin', to_date('01-07-2019', 'dd-MM-yyyy'));
INSERT INTO HERO(ID, HERO_NAME, CREATED_BY, CREATED_DATE) VALUES(NEXTVAL('HERO_SEQ'), 'Dr IQ', 'admin', to_date('01-07-2019', 'dd-MM-yyyy'));
INSERT INTO HERO(ID, HERO_NAME, CREATED_BY, CREATED_DATE) VALUES(NEXTVAL('HERO_SEQ'), 'Magma', 'admin', to_date('01-07-2019', 'dd-MM-yyyy'));
INSERT INTO HERO(ID, HERO_NAME, CREATED_BY, CREATED_DATE) VALUES(NEXTVAL('HERO_SEQ'), 'Tornado', 'admin', to_date('01-07-2019', 'dd-MM-yyyy'));

INSERT INTO USERS(ID, USERNAME, PASSWORD, EMAIL, ENABLED) VALUES (NEXTVAL('USER_SEQ'), 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin@itrunner.org', TRUE);
INSERT INTO USERS(ID, USERNAME, PASSWORD, EMAIL, ENABLED) VALUES (NEXTVAL('USER_SEQ'), 'jason', '$2a$10$6m2VoqZAxa.HJNErs2lZyOFde92PzjPqc88WL2QXYT3IXqZmYMk8i', 'jason@itrunner.org', TRUE);
INSERT INTO USERS(ID, USERNAME, PASSWORD, EMAIL, ENABLED) VALUES (NEXTVAL('USER_SEQ'), 'coco', '$2a$10$TBPPC.JbSjH1tuauM8yRauF2k09biw8mUDmYHMREbNSXPWzwY81Ju', 'coco@itrunner.org', FALSE);

INSERT INTO AUTHORITY (ID, AUTHORITY_NAME) VALUES (NEXTVAL('AUTHORITY_SEQ'), 'ROLE_USER');
INSERT INTO AUTHORITY (ID, AUTHORITY_NAME) VALUES (NEXTVAL('AUTHORITY_SEQ'), 'ROLE_ADMIN');

INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 2);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (2, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (3, 1);