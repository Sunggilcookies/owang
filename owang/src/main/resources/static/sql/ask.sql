[ 은별 ] -- Qna이름을 ask로 바꿨습니다~ 참고해주세용~!

create table notice(
id int auto_increment primary key,
title varchar(100),
pname varchar(100),
pw varchar(100),
upfile varchar(100),
content varchar(9999),
reg_date timestamp,
cnt int,
seq int,
lev int,
gid int
);


insert into notice (id, title, pname, pw, content, reg_date, cnt, seq, lev, gid)
values
(1,'첫글이다','현빈','1111','첫글 내용','2023-05-05',0,0,0,1),
(2,'두글이다','원빈','1111','첫글 내용','2023-05-07',0,0,0,2),
(3,'세글이다','투빈','1111','첫글 내용','2023-05-10',0,0,0,3),
(4,'네글이다','골빈','1111','첫글 내용','2023-05-12',0,0,0,4),
(5,'오글이다','장희빈','1111','첫글 내용','2023-05-19',0,0,0,5);



create table ask(
id int auto_increment primary key,
title varchar(100),
pname varchar(100),
pw varchar(100),
upfile varchar(100),
content varchar(9999),
reg_date timestamp,
cnt int,
seq int,
lev int,
gid int
);


insert into ask (id, title, pname, pw, content, reg_date, cnt, seq, lev, gid)
values
(1,'첫글이다','현빈','1111','첫글 내용','2023-05-05',0,0,0,1),
(2,'두글이다','원빈','1111','첫글 내용','2023-05-07',0,0,0,2),
(3,'세글이다','투빈','1111','첫글 내용','2023-05-10',0,0,0,3),
(4,'네글이다','골빈','1111','첫글 내용','2023-05-12',0,0,0,4),
(5,'오글이다','장희빈','1111','첫글 내용','2023-05-19',0,0,0,5);