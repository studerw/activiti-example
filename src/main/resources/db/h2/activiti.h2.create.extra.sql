create table If NOT EXISTS BOOK_REPORT(
    ID varchar(255),
    AUTHOR varchar(255),
    GROUP_ID varchar(255),
    CREATED_DATE timestamp,
    DOC_STATE varchar(255),
    DOC_TYPE varchar(255),
    TITLE varchar(255),
    BOOK_TITLE varchar(255),
    BOOK_AUTHOR varchar(255),
    SUMMARY varchar(10000),
    CONTENT  varchar(10000),
    primary key (ID)
);

create table If NOT EXISTS INVOICE(
    ID varchar(255),
    AUTHOR varchar(255),
    GROUP_ID varchar(255),
    TITLE varchar(255),
    CREATED_DATE timestamp,
    DOC_STATE varchar(255),
    DOC_TYPE varchar(255),
    PAYEE varchar(255),
    AMOUNT numeric(11,2),
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

