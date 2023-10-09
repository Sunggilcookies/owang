# 상품
# 상품명, 상품대상(개인인지 고객인지), 상품코드, 상품유효기간, 상품가격
CREATE TABLE product (
    product_id	INT AUTO_INCREMENT PRIMARY KEY,
    product_name	VARCHAR(100),
    product_client	VARCHAR(100),
    product_code	VARCHAR(100),
    product_valid	INT,
    product_price	INT
);

# 삽입 테스트
insert into product (product_name, product_client, product_code, product_valid, product_price) values
("[오왕]리뷰열람권 7일권", "개인", "s01", 7, 15000),
("[오왕]채용공고권 7일권", "기업", "c01", 7, 15000);

select * from product;

select * from product order by product_name;