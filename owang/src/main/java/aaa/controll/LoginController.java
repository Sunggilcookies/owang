package aaa.controll;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import aaa.model.MCompanyDTO;
import aaa.model.PageData;
import aaa.model.SoloDTO;
import aaa.service.MCompanyMapper;
import aaa.service.MailService;
import aaa.service.SoloMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
	@Autowired
	MailService mailService;
	@Resource
	SoloMapper ssmapper;
	@Resource
	MCompanyMapper ccmapper;

	@GetMapping("main")
	String loginmain() {

		return "login/login_main";
	}

	// 기업회원 구분하기!
	@PostMapping("main")
	String loginReg(HttpServletRequest request, HttpSession session, PageData pd, MCompanyDTO mcDTO, SoloDTO soloDTO) {
		String loginType = request.getParameter("loginType");
		String userid = request.getParameter("userid");
		String userpw = request.getParameter("userpw");
		// 현재날짜가져오기
		Date today = new Date();
		// 승우 추가 2시간 설정 단위 밀리세컨드(2시간 = 2 * 60분 * 60초)
		session.setMaxInactiveInterval(2 * 60 * 60);

		// 기업 로그인
		if ("company".equals(loginType)) {

			int result = ccmapper.loginCompany(userid, userpw);

			pd.setGoUrl("/login/main");
			pd.setMsg("아이디나 비밀번호가 일치하지 않습니다.");
			if (result == 1) {
				MCompanyDTO comsession = ccmapper.deatilaaaCompany(userid);
				Date ccdate = comsession.getCdate();
				int cctype = comsession.getCtype();
				System.out.println(cctype + "hheifhaihf");
				pd.setMsg("기업회원 로그인");
				// 타입결제확인
				if (cctype == 2 && ccdate != null && ccdate.before(today)) { //

					comsession.setCtype(1);
					ccmapper.logincmember(comsession);
					System.out.println("타입수정완료!");
					pd.setMsg("기업회원 로그인완료. (채용공고권이 만료되었습니다.)");

				}

				// 객체세션발급
				MCompanyDTO companysession = ccmapper.deatilaaaCompany(userid);
				session.setAttribute("cid", userid);
				session.setAttribute("companysession", companysession);
				pd.setGoUrl("/");
			}

		} else if ("individual".equals(loginType)) {
			int result = ssmapper.loginSolo(userid, userpw);

			pd.setGoUrl("/login/main");
			pd.setMsg("아이디나 비밀번호가 일치하지 않습니다.");
			if (result == 1) {
				pd.setMsg("개인회원 로그인");
				// 객체정보받기
				SoloDTO sosession = ssmapper.resumeSolo(userid);

				Date ssdate = sosession.getSdate();
				int sstype = sosession.getStype();

				// 결제관련 타입처리
				if (sstype == 2 && ssdate != null && ssdate.before(today)) { //
					sosession.setStype(1);
					ssmapper.loginsmember(sosession);
					System.out.println("타입수정완료!");
					pd.setMsg("개인회원 로그인 (리뷰열람권이 만료되었습니다.)");
				}

				// 객체세션발급
				SoloDTO solosession = ssmapper.resumeSolo(userid);
				session.setAttribute("sid", userid);
				session.setAttribute("solosession", solosession);
				pd.setGoUrl("/");
			}

		}

		return "/join/join_alert";
	}

	@RequestMapping("logout")
	String logout(HttpSession session, PageData pd) {
		pd.setMsg("로그아웃 됨?");
		session.invalidate();

		return "redirect:/";
	}

	// 아이디 비번찾기
	@RequestMapping("find")
	String find() {

		return "login/login_find";
	}

	// 아이디결과창
	@RequestMapping("findreg/{finduid}")
	String findreg(@PathVariable String finduid, Model mm) {

		mm.addAttribute("uid", finduid);
		return "login/find_idreg";
	}

	// 아이디찾기
	@GetMapping("findid")
	String findid() {

		return "login/find_id";
	}

	@PostMapping("findid")
	String findnid(HttpServletRequest request, HttpSession session, PageData pd, MCompanyDTO mcDTO, SoloDTO soloDTO) {
		String loginType = request.getParameter("idloginType");
		String fname = request.getParameter("idname");
		String femail = request.getParameter("idemail");
		System.out.println("이름" + fname + "이메일" + femail + "타입" + loginType);
		// 기업아이디찾기
		if ("company".equals(loginType)) {
			System.out.println("기업");
			int cresult = ccmapper.findloCompany(fname, femail); // 개인아이디찾기 }else if

			pd.setGoUrl("/login/findid");
			pd.setMsg("일치하는 아이디가 없습니다.");
			if (cresult == 1) {
				MCompanyDTO comsession = ccmapper.findid(fname, femail);
				String cid = comsession.getCid();
				pd.setGoUrl("/login/findreg/" + cid); // URL에 sid 값을 문자열로 추가
				pd.setMsg("일치하는 아이디 검색 1건");
				System.out.println("기업아이디 성공");
				// 타입결제확인
			}
		} else if ("individual".equals(loginType)) {

			int sresult = ssmapper.findloSolo(fname, femail);

			pd.setGoUrl("/login/findid");
			pd.setMsg("일치하는 아이디가 없습니다.");
			if (sresult == 1) {
				SoloDTO sosession = ssmapper.findid(fname, femail);
				String sid = sosession.getSid();

				pd.setMsg("일치하는 아이디 검색 1건");
				pd.setGoUrl("/login/findreg/" + sid); // URL에 sid 값을 문자열로 추가

			}
		}
		return "join/join_alert";
	}

	// 비번찾기
	@GetMapping("findpw")
	String findpw() {
		return "login/find_pw";
	}

	@PostMapping("findpw")
	String findpw(HttpServletRequest request, PageData pd) {
		String loginType = request.getParameter("pwloginType");
		String fid = request.getParameter("pwid");
		String femail = request.getParameter("pwemail");
		String fbirth = request.getParameter("pwsbirth");
		String fceo = request.getParameter("pwcceo");

		
		System.out.println("아이디" + fid + ", 이메일" + femail + ", 생년월일" + fbirth + ", 대표이름" + fceo + ", 타입" + loginType);
		// 디폴트값 설정
		pd.setGoUrl("/login/findpw");
		pd.setMsg("일치하는 정보가 없습니다.");
		// 기업회원인 경우
		if ("company".equals(loginType)) {
			int cresult = ccmapper.findpwComp(fid, femail); // 일치하는 결과가 있는지 확인
			if (cresult == 1) {
				pd.setMsg("기업 이메일인증창입니다.");
				pd.setGoUrl("/login/findemail/c" + fid);
			}
		} else if ("individual".equals(loginType)) {
			int sresult = ssmapper.findpwSolo(fid, femail);
			if (sresult == 1) {
				pd.setMsg("개인 이메일인증창입니다.");
				pd.setGoUrl("/login/findemail/s" + fid); // URL에 sid 값을 문자열로 추가
			}
		}
		return "join/join_alert";
	}
	// 비밀번호찾기 이메일 인증번호

		@GetMapping("findemail/{csuserid}")
		String findemail(@PathVariable String csuserid, Model mm) {
			String userid = csuserid.substring(1);
			char type = csuserid.charAt(0);
			if (type == 'c') {
				// 기업회원일때 이메일가져오기
				String email = ccmapper.findemail(userid);

				mm.addAttribute("email", email);

			} else {// 개인 회원일때 이메일가져오기
				String email = ssmapper.findemail(userid);

				mm.addAttribute("email", email);

			}
			mm.addAttribute("csuserid", csuserid);
			return "login/findemail";
		}

		@PostMapping("findemail/{csuserid}")
		String findemail(PageData pd, @PathVariable String csuserid, String pw, String pw2) {
			String userid = csuserid.substring(1);
			char type = csuserid.charAt(0);
			
				if (type == 'c') {
					// 수정 성공시 결과가 1이 나옴
					int cnt = ccmapper.modifycpw(userid, pw);
					if (cnt > 0) {
						System.out.println("기업수정성공");
						pd.setMsg("수정되었습니다. 다시 로그인해주세요");
						pd.setGoUrl("/login/findemail/c" + userid);
					}
				} else {
					int cnt = ssmapper.modifyspw(userid, pw);
					if (cnt > 0) {
						System.out.println("개인수정성공");
						pd.setMsg("수정되었습니다. 다시 로그인해주세요");
						pd.setGoUrl("/login/findemail/s" + userid); // URL에 sid 값을 문자열로 추가
					}
				
			}
		
			return "join/join_alert";
		}

		// 비밀번호 재변경
		@GetMapping("findpwreg/{csuserid}")
		String findpwreg(@PathVariable String csuserid) {
			return "login/find_pwreg";
		}

		@PostMapping("findpwreg/{csuserid}")
		String modifyReg(PageData pd, @PathVariable String csuserid, String pw, String pw2) {
			
			String userid = csuserid.substring(1);
			char type = csuserid.charAt(0);
			pd.setMsg("오류가 발생했습니다. 다시 시도해주세요");
			pd.setGoUrl("/login/findpw");
			if (pw == null || pw.isEmpty()) {
				pd.setMsg("비밀번호를 입력하세요");
				pd.setGoUrl("/login/findpwreg/" + csuserid);
				return "join/join_alert";
			}
			if (!pw.isEmpty() && !pw.equals(pw2)) {
				// 비밀번호 틀림
				pd.setMsg("비밀번호가 일치하지 않습니다");
				pd.setGoUrl("/login/findpwreg/" + csuserid);
				return "join/join_alert";
			}
			if (pw.equals(pw2)) {
				if (type == 'c') {
					// 수정 성공시 결과가 1이 나옴
					int cnt = ccmapper.modifycpw(userid, pw);
					if (cnt > 0) {
						System.out.println("기업수정성공");
						pd.setMsg("수정되었습니다. 다시 로그인해주세요");
						pd.setGoUrl("/login/main");
					}
				} else {
					int cnt = ssmapper.modifyspw(userid, pw);
					if (cnt > 0) {
						System.out.println("개인수정성공");
						pd.setMsg("수정되었습니다. 다시 로그인해주세요");
						pd.setGoUrl("/login/main");
					}
				}
			}
			return "join/join_alert";
		}

		// 이메일 인증번호보내기
			@ResponseBody
			@PostMapping("/mail")
			String MailSend(String mail) {
				System.out.println("어서오시게나");
				System.out.println(mail);
				 //인증번호 생성
				int number=(int)(Math.random() * (90000)) + 100000;
				 String num = "" + number;
			   
				 String toEmail = mail; // 받는 사람 이메일 주소를 동적으로 설정
		        String subject = "[오왕] 비밀번호변경 인증메일입니다."; // 이메일 제목을 동적으로 설정
		        String text = "비밀번호 변경 인증번호입니다.: "  + "\n\n "+num+" \n\n 홈페이지로 돌아가 인증번호를 입력해주세요" + "\n만약 본인이 요청한 것이 아니라면 고객지원게시판으로 문의바랍니다."; // 이메일 내용을 동적으로 설정
		        mailService.sendEmail("ajh1070337@naver.com", toEmail, subject, text);
		        System.out.println("이메일이 전송되었슙니다.");
				return num;
			}

}
