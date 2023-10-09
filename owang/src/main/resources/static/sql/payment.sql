payment 테이블입니다

# 결제
# 결제아이디, 개인회원아이디, 기업회원아이디, 아임포트고유번호, 주문번호, 
# 상품아이디, 상품명, 주문자명, 주문자연락처, 
# 결제액, 결제시간
CREATE TABLE payment(
    pay_id   INT AUTO_INCREMENT PRIMARY KEY,
    sid      VARCHAR(100),
    cid      VARCHAR(100),
    imp_uid   VARCHAR(100),
    merchant_uid   VARCHAR(100),
    product_id   VARCHAR(100),
    pname   VARCHAR(100),
    buyer_name   VARCHAR(100),
    buyer_tel   VARCHAR(100),
    amount      INT,
    paid_at      Date
);

INSERT INTO payment (imp_uid, merchant_uid, product_id, pname, buyer_name, buyer_tel, amount, paid_at)
VALUES 
('imp_025790351562', '', 'S01', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('imp_566888188428', 'IMP20230906002441082', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-05'),
('imp_985741385064', 'IMP20230904182215976', '2', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-09-04'),
('imp_999619442857', 'IMP20230904155016690', '2', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-09-04'),
('imp_169382293483', 'IMP20230904142259072', '2', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-09-04'),
('imp_625432241613', 'IMP20230904140709774', '1', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-09-04'),
('imp_874746686122', 'IMP20230904133903779', '1', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-09-04'),
('imp_308629473164', 'IMP20230904130346776', '1', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-09-04'),
('imp_599502746699', 'IMP20230904130139965', '1', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-09-04'),
('imp_946694407848', 'IMP20230904110811870', '1', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-09-04'),
('imp_817319134367', 'IMP20230904110156990', '1', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-09-04'),
('imp_050157797661', 'IMP20230904105914552', '1', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-09-04'),
('imp_888435779362', 'IMP20230904104711422', '1', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-09-04'),
('imp_686190563825', 'IMP20230904031307436', '2', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-09-04'),
('imp_314094345695', 'IMP20230904031130573', '2', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-09-04'),
('imp_358517998075', 'IMP20230904030026103', '2', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-09-04'),
('imp_199079359823', 'IMP20230831205111724', '2', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-08-31'),
('imp_091839904737', 'IMP20230831172716717', '2', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-08-31'),
('imp_280029240857', 'IMP20230831162347718', '2', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-08-31'),
('imp_107421568601', 'IMP20230831160646321', '1', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-08-31'),
('imp_271406012531', 'IMP20230830201219168', '1', '[Wuzik]강남 다이아 1시간', '테스터', '01012345678', 1004, '2023-08-30'),
('imp_889339241805', 'IMP20230827042534720', '1', '[Wuzik]강남 다이아 1시간', '포트원', '0101234567', 1004, '2023-08-27'),
('imp_863297252022', 'IMP20230827041812669', '1', '[Wuzik]강남 다이아 1시간', '포트원', '0101234567', 1004, '2023-08-27'),
('imp_201625680630', 'IMP476', '1', '[Wuzik]강남 다이아 1시간', '포트원', '01012345678', 1004, '2023-08-26'),
('imp_013404314802', 'IMP292', '1', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_864800252118', 'IMP28', '2', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_859855972239', 'IMP27', '1', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_475609894852', 'IMP23', '1', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_796868324499', 'IMP19', '1', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_967542378544', 'IMP159', '1', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_981743179250', 'IMP130', '1', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_370355199872', 'IMP129', '1', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_118300037125', 'IMP128', '2', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_084070451778', 'IMP12', '2', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_416339369582', 'IMP11', '2', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_835688159220', 'IMP10', '2', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_591919237632', 'IMP9', '2', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_221978912485', 'IMP8', '2', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_122111890282', 'IMP7', '1', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_449621156160', 'IMP6', '1', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_170141682436', 'IMP5', '1', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_779058855491', 'IMP4', '1', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_821090531804', 'IMP3', '1', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_468823677345', 'IMP2', '1', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24'),
('imp_855430834058', 'IMP1', '1', '강남 다이아 1시간', '포트원 기술지원팀', '01012345678', 1004, '2023-08-24');

INSERT INTO payment (sid, imp_uid, merchant_uid, product_id, pname, buyer_name, buyer_tel, amount, paid_at)
VALUES 
('user1', 'imp_072398279289', '230907185311923S01', '1', '[오왕]리뷰열람권 7일권', '테스터', '01012345678', 15000, '2023-09-07'),
('user1', 'imp_973907384004', '230907095142338S01', '1', '[오왕]리뷰열람권 7일권', '테스터', '01012345678', 15000, '2023-09-07'),
('user1', 'imp_234779873776', '1694047776841', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-07'),
('user1', 'imp_538549349237', '1694047525077', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-07'),
('user2', 'imp_864097863856', '230907015130958s01', '1', '[오왕]리뷰열람권 7일권', '테스터', '01012345678', 15000, '2023-09-07'),
('user2', 'imp_612639395036', '230907014355018s01', '1', '[오왕]리뷰열람권 7일권', '테스터', '01012345678', 15000, '2023-09-07'),
('user1', 'imp_349185487732', '230907011942328s01', '1', '[오왕]리뷰열람권 7일권', '테스터', '01012345678', 15000, '2023-09-07'),
('user1', 'imp_517030275301', '230907011706620s01', '1', '[오왕]리뷰열람권 7일권', '테스터', '01012345678', 15000, '2023-09-07'),
('user3', 'imp_574265993389', '230907001422975s01', '1', '[오왕]리뷰열람권 7일권', '테스터', '01012345678', 15000, '2023-09-07'),
('user1', 'imp_602194255689', '230907001252222s01', '1', '[오왕]리뷰열람권 7일권', '테스터', '01012345678', 15000, '2023-09-07'),
('user1', 'imp_241238503767', '1693982211221', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user4', 'imp_678999715209', '1693980996573', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user1', 'imp_469596153287', '1693980592387', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user1', 'imp_398146769280', '1693980144625', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user1', 'imp_044912708565', '1693979907878', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user1', 'imp_557512696971', '1693979510647', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user2', 'imp_997032164882', '1693972030111', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user1', 'imp_508556634727', '1693971551171', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user3', 'imp_608465557530', '1693971462341', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user1', 'imp_759270975452', '1693971269188', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user4', 'imp_949066737666', '1693971064709', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user1', 'imp_737923544858', '1693970921261', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user1', 'imp_357751759467', '1693970749533', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user1', 'imp_577345397925', '1693970340771', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user5', 'imp_499444395612', '1693968442594', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user2', 'imp_102333479037', '1693968329978', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user4', 'imp_524078201746', '1693968075379', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user2', 'imp_534875988739', '1693967873242', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user1', 'imp_262727487065', '1693967724713', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user2', 'imp_048877613588', '1693966874879', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user3', 'imp_805478244039', '1693965460375', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user3', 'imp_225014855631', '1693930003589', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user3', 'imp_681410454304', '1693927408425', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user2', 'imp_255329447830', '1693927318394', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06'),
('user1', 'imp_659961984317', '1693926960461', '1', '오왕 리뷰열람권', '테스터', '01012345678', 1004, '2023-09-06');

INSERT INTO payment (cid, imp_uid, merchant_uid, product_id, pname, buyer_name, buyer_tel, amount, paid_at)
VALUES 
('company2', 'imp_280663677470', '230907200419737C01', '2', '[오왕]채용공고권 7일권', '테스터', '01012345678', 15000, '2023-09-07'),
('company1', 'imp_860985992566', '230907020623578c01', '2', '[오왕]채용공고권 7일권', '테스터', '01012345678', 15000, '2023-09-07'),
('company5', 'imp_268855136267', '230907011411597c01', '2', '[오왕]채용공고권 7일권', '테스터', '01012345678', 15000, '2023-09-07'),
('company4', 'imp_528647316508', '230907011025915c01', '2', '[오왕]채용공고권 7일권', '테스터', '01012345678', 15000, '2023-09-07'),
('company3', 'imp_090294380311', '230907010450706c01', '2', '[오왕]채용공고권 7일권', '테스터', '01012345678', 15000, '2023-09-07'),
('company2', 'imp_882364398679', '230907003141080c01', '2', '[오왕]채용공고권 7일권', '테스터', '01012345678', 15000, '2023-09-07'),
('company2', 'imp_969382024106', '230908131255833C01', '2', '[오왕]채용공고권 7일권', '테스터', '01012345678', '15000', '2023-09-08');

INSERT INTO payment (sid, cid, imp_uid, merchant_uid, product_id, pname, buyer_name, buyer_tel, amount, paid_at)
VALUES 
(NULL, 'company2', 'imp_526385956409', '230908145258370C01', '2', '[오왕]채용공고권 7일권', '', '', '15000', '2023-09-08'),
(NULL, 'company2', 'imp_015757273316', '230908145856800C01', '2', '[오왕]채용공고권 7일권', 'XYZ Corporation', '987-654-3210', '15000', '2023-09-08'),
('user2', NULL, 'imp_363905103134', '230908150141927S01', '1', '[오왕]리뷰열람권 7일권', 'Jane Smith', '987-654-3210', '15000', '2023-09-08'),
('user2', NULL, 'imp_062418525013', '230908151007349S01', '1', '[오왕]리뷰열람권 7일권', 'Jane Smith', '987-654-3210', '15000', '2023-09-08'),
('user3', NULL, 'imp_397818026463', '230908162334436S01', '1', '[오왕]리뷰열람권 7일권', 'Bob Johnson', '555-123-4567', '15000', '2023-09-08'),
(NULL, 'company1', 'imp_786405842581', '230908163323239C01', '2', '[오왕]채용공고권 7일권', 'ABC Company', '123-456-7890', '15000', '2023-09-08');

select * from payment;

drop table payment;