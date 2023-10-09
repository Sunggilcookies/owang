package aaa.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import aaa.model.ProductDTO;

@Mapper
public interface ProductMapper {

	// 상품목록보기
	List<ProductDTO> list();
	
	// 상품상세보기
	ProductDTO detail(String productId);

	// 상품 유효기간
	int period(String productId);
	
	// 상품생성
	int insert(ProductDTO dto);
	
	// 상품삭제
	int delete(String productId);

//	// 상품수정
//	int modify(ProductDTO dto);
}
