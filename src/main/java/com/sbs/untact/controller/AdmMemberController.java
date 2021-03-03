package com.sbs.untact.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Member;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.MemberService;
import com.sbs.untact.util.Util;

@Controller
public class AdmMemberController {
	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/adm/member/login")
	public String login() {
		return "adm/member/login";
	}
	
	@RequestMapping("/adm/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, HttpSession session) {
		
		if (loginId == null) {
			return Util.msgAndBack("ID를 입력해주세요.");
		}
		
		Member existingMember = memberService.getMemberByLoginId(loginId);

		if (existingMember == null) {
			return Util.msgAndBack("존재하지 않는 아이디");
		}
		if (loginPw == null) {
			return Util.msgAndBack("PW를 입력해주세요");
		}
		if (existingMember.getLoginPw().equals(loginPw) == false) {
			return Util.msgAndBack("비밀번호가 일치하지 않습니다");
		}
		if(memberService.isAdmin(existingMember) == false) {
			return Util.msgAndBack("관리자만 접근 할 수 있습니다");
		}
		session.setAttribute("loginedMemberId", existingMember.getId());
		String msg = String.format("%s님 환영합니다", existingMember.getNickname());
		return Util.msgAndReplace(msg, "../home/main");
	}
	
	@RequestMapping("/adm/member/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		
		if (param.isEmpty()) {
			return new ResultData("F-1", "수정할 정볼르 입력해주세요.");
		}
		int loginedMemberId = (int)req.getAttribute("loginedMemberId");
		param.put("id", loginedMemberId);
		
		return memberService.modifyMember(param);
	}
}