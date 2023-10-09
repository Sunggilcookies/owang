package aaa.controll;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import aaa.model.MCompanyDTO;
import aaa.model.PageData;
import aaa.model.SoloDTO;
import aaa.service.AdminCompanyMapper;
import aaa.service.EndCompanyMapper;
import aaa.service.EndSoloMapper;
import aaa.service.MCompanyMapper;
import aaa.service.MailService;
import aaa.service.SoloMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/join")
public class JoinController {
	@Autowired
	MailService mailService;
	@Resource
	SoloMapper smapper;
	@Resource
	MCompanyMapper cmapper;
	@Resource
	AdminCompanyMapper acmapper;
	@Resource
	EndSoloMapper endSoloMapper;
	@Resource
	EndCompanyMapper endCompanyMapper;

	Date currentDate = new Date();

	// 이용약관
	@GetMapping("cterms.html")
	public String showTermsPage() {

		return "join/cterms"; // "terms.html" 파일과 매핑
	}

	// 이용약관
	@GetMapping("sterms.html")
	public String sshowTermsPage() {

		return "join/sterms"; // "terms.html" 파일과 매핑
	}

	// 개인아이디중복확인
	@PostMapping("checksId")
	@ResponseBody
	public String checksId(@RequestParam("sid") String sid) {
		System.out.println("아이디체크");
		int Count = smapper.idChk(sid);
		int byeCount = endSoloMapper.idChk(sid);
		int total = Count + byeCount;
		System.out.println(Count);
		return total > 0 ? "중복" : "고유";
	}

	// 기업아이디중복확인
	@PostMapping("checkcId")
	@ResponseBody
	public String checkcId(@RequestParam("cid") String cid) {
		int Count = cmapper.idChk(cid);
		int byeCount = endCompanyMapper.idChk(cid);
		int total = Count + byeCount;
		return total > 0 ? "중복" : "고유";
	}

	// 개인회원가입
	@GetMapping("solo")
	String joinsoloForm(SoloDTO solo, MCompanyDTO company, Model mm) {
		List<MCompanyDTO> data = acmapper.join(company);
		mm.addAttribute("mainData", data);
		return "join/join_solo";
	}

	@PostMapping("solo")

	String joinsoloReg(SoloDTO solo, PageData pd, HttpServletRequest request) {
		fileSavesolo(solo, request);
		solo.setSjoindate(currentDate);
		pd.setMsg("개인회원가입이 완료되었습니다.");
		pd.setGoUrl("/");
		smapper.insertSolo(solo); // sql에 개인정보입력
		return "join/join_alert";
	}

	// 기업고객
	@GetMapping("company")
	String joincompanyForm(MCompanyDTO company, Model mm) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date);
		mm.addAttribute("today", today);

		return "join/join_company";
	}

	@PostMapping("company")
	String joincompanyReg(MCompanyDTO company, PageData pd, HttpServletRequest request) {
		fileSavecompany(company, request);
		fileloSavecompany(company, request);
		pd.setMsg("기업회원가입이 완료되었습니다.");
		pd.setGoUrl("/");
		cmapper.insertCompany(company); // sql에 개인정보입력
		return "join/join_alert";
	}

	@RequestMapping("/join_ok")
	String joinmain() {

		return "join/join_joinok";
	}

	void fileSavesolo(SoloDTO dto, HttpServletRequest request) {

		// 파일 업로드 유무 확인
		if (dto.getMmff().isEmpty()) {

			return;
		}

		String path = request.getServletContext().getRealPath("soloup");
		// path = "C:\\Final_Team\\owang\\src\\main\\webapp\\member\\soloup";

		int dot = dto.getMmff().getOriginalFilename().lastIndexOf(".");
		String fDomain = dto.getMmff().getOriginalFilename().substring(0, dot);
		String ext = dto.getMmff().getOriginalFilename().substring(dot);

		dto.setScompanyFile(fDomain + ext);
		File ff = new File(path + "\\" + dto.getScompanyFile());
		int cnt = 1;
		while (ff.exists()) {

			dto.setScompanyFile(fDomain + "_" + cnt + ext);
			ff = new File(path + "\\" + dto.getScompanyFile());
			cnt++;
		}

		try {
			FileOutputStream fos = new FileOutputStream(ff);

			fos.write(dto.getMmff().getBytes());

			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 사업자등록증파일저장
	void fileSavecompany(MCompanyDTO cto, HttpServletRequest request) {

		// 파일 업로드 유무 확인
		if (cto.getMmff().isEmpty()) {

			return;
		}

		String path = request.getServletContext().getRealPath("companyup");
		// path = "C:\\Final_Team\\owang\\src\\main\\webapp\\member\\companyup";

		int dot = cto.getMmff().getOriginalFilename().lastIndexOf(".");
		String fDomain = cto.getMmff().getOriginalFilename().substring(0, dot);
		String ext = cto.getMmff().getOriginalFilename().substring(dot);

		cto.setCcompanyFile(fDomain + ext);
		File ff = new File(path + "\\" + cto.getCcompanyFile());
		int cnt = 1;
		while (ff.exists()) {

			cto.setCcompanyFile(fDomain + "_" + cnt + ext);
			ff = new File(path + "\\" + cto.getCcompanyFile());
			cnt++;
		}

		try {
			FileOutputStream fos = new FileOutputStream(ff);

			fos.write(cto.getMmff().getBytes());

			fos.close();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	// 로고파일저장
	void fileloSavecompany(MCompanyDTO cto, HttpServletRequest request) {

		// 파일 업로드 유무 확인
		if (cto.getLommff() == null || cto.getLommff().isEmpty()) {
			return;
		}

		String path = request.getServletContext().getRealPath("companylogoup");
		// path = "C:\\Final_Team\\owang\\src\\main\\webapp\\member\\companyup";

		int dot = cto.getLommff().getOriginalFilename().lastIndexOf(".");
		String fDomain = cto.getLommff().getOriginalFilename().substring(0, dot);
		String ext = cto.getLommff().getOriginalFilename().substring(dot);

		cto.setClogo(fDomain + ext);
		File ff = new File(path + "\\" + cto.getClogo());
		int cnt = 1;
		while (ff.exists()) {

			cto.setClogo(fDomain + "_" + cnt + ext);
			ff = new File(path + "\\" + cto.getClogo());
			cnt++;
		}

		try {
			FileOutputStream fos = new FileOutputStream(ff);

			fos.write(cto.getLommff().getBytes());

			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 개인이메일 인증번호보내기
	@ResponseBody
	@PostMapping("/smail")
	String sMailSend(String smail) {
		System.out.println("어서오시게나");
		System.out.println(smail);
		// 인증번호 생성
		int number = (int) (Math.random() * (90000)) + 100000;
		String num = "" + number;

		String toEmail = smail; // 받는 사람 이메일 주소를 동적으로 설정
		String subject = "[오왕] 개인회원가입 인증메일입니다."; // 이메일 제목을 동적으로 설정
		String text = "개인회원가입 인증번호입니다.: " + "\n\n " + num + " \n\n 홈페이지로 돌아가 인증번호를 입력해주세요"; // 이메일 내용을 동적으로 설정
		mailService.sendEmail("ajh1070337@naver.com", toEmail, subject, text);
		System.out.println("이메일이 전송되었슙니다.");
		return num;
	}

	// 기업이메일 인증번호보내기
	@ResponseBody
	@PostMapping("/cmail")
	String cMailSend(String cmail) {
		System.out.println("어서오시게나");
		System.out.println(cmail);
		// 인증번호 생성
		int number = (int) (Math.random() * (90000)) + 100000;
		String num = "" + number;

		String toEmail = cmail; // 받는 사람 이메일 주소를 동적으로 설정
		String subject = "[오왕] 기업회원가입 인증메일입니다."; // 이메일 제목을 동적으로 설정
		String text = "기업회원가입 인증번호입니다.: " + "\n\n " + num + " \n\n 홈페이지로 돌아가 인증번호를 입력해주세요"; // 이메일 내용을 동적으로 설정
		mailService.sendEmail("ajh1070337@naver.com", toEmail, subject, text);
		System.out.println("이메일이 전송되었슙니다.");
		return num;
	}
}
