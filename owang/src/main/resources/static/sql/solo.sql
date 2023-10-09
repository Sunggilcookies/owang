#솔로키입니다.
-- user 계정 
show databases;  /* db 보기 */


use owang; /* db 사용 */
show tables;  /* 테이블 보기 */

-- db 생성
create database owang;

-- db 권한주기
grant all privileges on owang.* to root@localhost with grant option;

show databases;
CREATE TABLE solo (
	sno int  NOT NULL AUTO_INCREMENT primary key,
    sid varchar(20) ,
    spw VARCHAR(20),
    sname VARCHAR(30),
    sbirth VARCHAR(30),
    sage int,
    sgender varchar(10),
    sphone  VARCHAR(30),
    semail  VARCHAR(100),
    saddress  VARCHAR(100),
    scompanyName  VARCHAR(100),
    scompanyFile  VARCHAR(100),
    stype Int,
    sdate date,
    sbcnt int
    
);

use solo;

INSERT INTO solo (sid, spw, sname, sbirth, sage, sgender, sphone, semail, saddress, scompanyName, scompanyFile, stype, sdate, sbcnt)
VALUES
    ('user1', 'pass1', 'John Doe', '1990-01-01', 30, 'Male', '123-456-7890', 'john@example.com', '123 Main St', 'Company XYZ', 'file1.txt', 1, '2023-09-04', 5),
    ('user2', 'pass2', 'Jane Smith', '1985-03-15', 36, 'Female', '987-654-3210', 'jane@example.com', '456 Oak St', 'Company ABC', 'file2.txt', 1, '2023-09-04', 3),
    ('user3', 'pass3', 'Bob Johnson', '1982-11-30', 39, 'Male', '555-123-4567', 'bob@example.com', '789 Elm St', 'Company PQR', 'file3.txt', 2, '2023-09-04', 7);

select * from solo;

drop table solo;