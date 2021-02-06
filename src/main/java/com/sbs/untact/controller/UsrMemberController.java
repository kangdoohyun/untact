package com.sbs.untact.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.MemberService;

@Controller
public class UsrMemberController {
	@Autowired
	private MemberService memberService;

	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public ResultData showMain(@RequestParam Map<String, Object> param) {
		if (param.get("loginId") == null) {
			return new ResultData("F-1", "ID를 입력해주세요.");
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
}