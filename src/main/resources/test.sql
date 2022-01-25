CREATE TABLE PG_HINT_CAR (
                             VHCL_CODE VARCHAR(5) PRIMARY KEY ,
                             VHCL_RGSNO VARCHAR(10),
                             VHCL_MAKER VARCHAR(20),
                             VHCL_NAME VARCHAR(50),
                             VHCL_CLR VARCHAR(20),
                             MING_FG VARCHAR(4),
                             CRN_DTM DATE,
                             UDT_DTM DATE
);
insert into PG_HINT_CAR values('021','12가1234','현대자동차','아이오닉','그레이',null,now(),null);

