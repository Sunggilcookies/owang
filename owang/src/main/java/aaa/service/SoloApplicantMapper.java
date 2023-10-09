package aaa.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import aaa.model.ApplicantDTO;
import aaa.model.SoloResumeDTO;

@Mapper
public interface SoloApplicantMapper {
	
	// 지원서 접수
	void appinsert(ApplicantDTO adto);
	
	// 지원서 리스트 출력	
	List<ApplicantDTO> applist(ApplicantDTO adto);

	// 지원서 디테일
	ApplicantDTO appdetail(int ano, String sid);
	
	// 지원서 삭제
	int appdelete(int ano);
	
	// 지원서 총 개수
	int apptotal();
	
	// 해당공고의 지원자 수
	int appnum(ApplicantDTO adto);
	
	// 지원서 상태 변경
	int appstate(int ano);

	void recnt(int id);
}
