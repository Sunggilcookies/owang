CREATE TABLE applicant (
    ano INT AUTO_INCREMENT PRIMARY KEY,
    rsid VARCHAR(100),
    sid VARCHAR(100),
    cid VARCHAR(100),
    sname VARCHAR(100),
    cname VARCHAR(100),
    recruit_id VARCHAR(100),
    recruit_title VARCHAR(100),
    apphoto VARCHAR(100),
    aptitle VARCHAR(100),
    apcareer VARCHAR(100),
    apcompany VARCHAR(100),
    apworkstart VARCHAR(100),
    apworkend VARCHAR(100),
    apwork VARCHAR(100),
    apacademic VARCHAR(100),
    apacademicstat VARCHAR(100),
    apintroduce VARCHAR(100),
    aplicense VARCHAR(100),
    aplicenseorg VARCHAR(100),
    aplicenseyear VARCHAR(100),
    aplanguage VARCHAR(100),
    aplanguagelevel VARCHAR(100),
    apsubmitdate TIMESTAMP,
    apread INT DEFAULT 0,
    appass INT DEFAULT 0
);

select * from applicant;
drop table applicant;