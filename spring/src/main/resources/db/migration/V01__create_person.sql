CREATE SEQUENCE PERSON_SEQ
    START WITH 100
    INCREMENT BY 1;

CREATE TABLE PERSON (
    id bigint,
    name varchar(255),
    age int,
    PRIMARY KEY(id)
);

COMMIT;
