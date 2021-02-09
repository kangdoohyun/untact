package com.sbs.untact.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Member;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.MemberService;

@Controller
public class UsrMemberController {
	@Autowired
	private MemberService memberService;

	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public ResultData doJoin(@RequestParam Map<String, Object> param) {
		if (param.get("loginId") == null) {
			return new ResultData("F-1", "ID를 입력해주세요.");
		}
		
		Member existingMember = memberService.getMemberByLoginId((String) param.get("loginId"));

		if (existingMember != null) {
			return new ResultData("F-2", String.format("%s (은)는 이미 사용중인 로그인아이디 입니다.", param.get("loginId")));
		}
		if (param.get("loginPw") == null) {
			return new ResultData("F-1", "PW를 입력해주세요.");
		}
		
		if (param.get("name") == null) {
			return new ResultData("F-1", "name을 입력해주세요.");
		}
		
		if (param.get("nickname") == null) {
			return new ResultData("F-1", "nickname을 입력해주세요.");
		}
		
		if (param.get("cellphoneNo") == null) {
			return new ResultData("F-1", "cellphoneNo를 입력해주세요.");
		}
		
		if (param.get("email") == null) {
			return new ResultData("F-1", "email를 입력해주세요.");
		}
		
		return memberService.join(param);
	}
	@RequestMapping("/usr/member/doLogin")
	@ResponseBody
	public ResultData doLogin(String loginId, String loginPw, HttpSession session) {
		
		if (loginId == null) {
			return new ResultData("F-1", "ID를 입력해주세요.");
		}
		
		Member existingMember = memberService.getMemberByLoginId(loginId);

		if (existingMember == null) {
			return new ResultData("F-2", "존재하지 않는 아이디.");
		}
		if (loginPw == null) {
			return new ResultData("F-1", "PW를 입력해주세요.", "loginId", loginId);
		}
		if (existingMember.getLoginPw().equals(loginPw) == false) {
			return new ResultData("F-3", "비밀번호가 일치하지 않습니다.");
		}
		session.setAttribute("loginedMemberId", existingMember.getId());
		return new ResultData("S-1", String.format("%s님 환영합니다.", existingMember.getNickname()));
	}
	@RequestMapping("/usr/member/doLogout")
	@ResponseBody
	public ResultData doLogout(HttpSession session) {
		
		session.removeAttribute("loginedMemberId");
		
		return new ResultData("S-1", "로그아웃 되었습니다.");
	}
	@RequestMapping("/usr/member/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param, HttpSession session) {
		
		if (param.isEmpty()) {
			return new ResultData("F-1", "수정할 정볼르 입력해주세요.");
		}
		int loginedMemberId = (int)session.getAttribute("loginedMemberId");
		param.put("id", loginedMemberId);
		
		return memberService.modifyMember(param);
	}
}