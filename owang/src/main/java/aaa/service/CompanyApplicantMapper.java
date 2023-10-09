package aaa.service;



import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import aaa.model.ApplicantDTO;
import aaa.model.MCompanyDTO;
import aaa.model.RecruitDTO;



@Mapper
public interface CompanyApplicantMapper {

		// 재훈 추가
		// 지원자 리스트 불러오기
		List<ApplicantDTO> applist(ApplicantDTO adto);
		
		List<ApplicantDTO> apppasslist(ApplicantDTO adto);
		
		List<ApplicantDTO> appnonpasslist(ApplicantDTO adto);
		
		// 지원서 디테일
		ApplicantDTO appdetail(int ano, String cid);
		
		// 지원자 열람여부
		int appread(int ano);
		
		// 지원자 합격
		int apppass(int ano);
		
		// 지원자 불합격
		int appnonpass(int ano);
		
		List<ApplicantDTO> appfind(String cid);
}

