# 테이블 마다 반드시 컬럼들의 설명을 해주셔야합니다. 코드도 최대한 주석 달아주세요
# 테이블 이름위에 컬럼명의 설명 부탁드립니다.
# resource/static/sql 스프링에 폴더를 만들겠습니다.
# 하위폴더로 만든 쿼리문을 올려주세요!(아니면 카톡으로 공유할지 의논)
# 임시로 만든 컬러명은 반드시 기재 부탁드립니다. 교체될 내용도 같이 메모! (!!! 바뀔 컬럼명 !!!)ex-> recruit_name 채용 게시판 작성자 이름 !!! 기업 이름과 교체 !!!
# 아자아자 화이팅 할수있다!

# 답글을 안써도 될꺼같아 seq lev gid를 제거했습니다.
# 임시로 만듬 채용 테이블 recruit
# recruit_id 무결성을 위한 id
# recruit_title 채용 게시판 이름 
# recruit_name 체용 게시판에 작성한 기업 이름 !!! 기업 이름과 교체 !!!
# recruit_upfile 체용 게시판에 업로드한 이미지 파일
# recruit_content 체용 게시판에 작성한 내용
# reg_date 체용 게시판 작성일
#  recruit_magam 채용 게시판 마감일 선택한 수,
# cnt 체용 게시판 조회수
CREATE TABLE recruit (
    recruit_id INT AUTO_INCREMENT PRIMARY KEY,
    cid VARCHAR(100),
    recruit_title VARCHAR(100),
    recruit_upfile VARCHAR(100),
    recruit_content VARCHAR(9999),
    reg_date TIMESTAMP,
    recruit_magam INT,
    cnt INT,
    recruit_money Int
    
);

# 삽입 테스트
insert into recruit(
	recruit_title,
    recruit_name, 
    recruit_content,
    reg_date,
    cnt
    )
values
('육글이다','첫승우','첫글내용','2023-05-05',1),
('칠글이다','두승우','두글내용','2023-05-07',2),
('팔글이다','삼승우','세글내용','2023-05-10',3),
('구글이다','넷승우','네글내용','2023-05-12',4),
('십글이다','오승우','오글내용','2023-05-19',5);

select * from recruit order by recruit_id desc limit 0, 5;
select * from recruit;
select max(recruit_id) from recruit; 
select count(*) from recruit;
drop table recruit;