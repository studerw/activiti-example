create table IF NOT EXISTS DOCUMENT(
    ID varchar(255),
    AUTHOR varchar(255),
    TITLE varchar(255),
    SUMMARY varchar(10000),
    CONTENT  varchar(10000),
    GROUP_ID varchar(255),
    CREATED_DATE timestamp,
    STATE_ varchar(255),
    primary key (ID)
);

create table IF NOT EXISTS ALERT(
    ID varchar(255),
    CREATED_BY varchar(255),
    MESSAGE varchar(10000),
    PRIORITY  int,
    USER_ID varchar(255),
    CREATED_DATE timestamp,
    ACKNOWLEDGED boolean,
    primary key (ID)
);

