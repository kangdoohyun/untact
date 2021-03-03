package com.sbs.untact.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Reply;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;
import com.sbs.untact.service.ReplyService;

@Controller
public class AdmReplyController {
	@Autowired
	private ReplyService replyService;
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping("/adm/reply/list")
	@ResponseBody
	public ResultData showList(String relTypeCode, Integer relId, @RequestParam(defaultValue = "1") int page){
		if(relTypeCode == null) {
			return new ResultData("F-1", "relTypeCode를 입력해주세요");
		}
		
		if(relId == null) {
			return new ResultData("F-1", "relId를 입력해주세요");
		}
		
		if(relTypeCode.equals("article")) {
			Article article = articleService.getArticle(relId);
			if(article == null) {
				return new ResultData("F-1", "존재하지 않는 게시물입니다");
			}
		}
		int itemsInAPage = 20;
		List<Reply> replies = replyService.getForPrintReplies(relTypeCode, relId, page, itemsInAPage); 
		return new ResultData("S-1", "성공", "replies", replies);
	}
	@RequestMapping("/adm/reply/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id, HttpServletRequest req) {
		int loginedMemberId = (int)req.getAttribute("loginedMemberId");
		
		if(id == null) {
			return new ResultData("F-2", "게시물 번호를 입력해주세요");
		}
		Reply reply = replyService.getReply(id);
		if(reply == null) {
			return new ResultData("F-1", "존재하지 않는 댓글입니다");
		}
		ResultData actorCanDeleteRd = replyService.getActorCanDeleteRd(reply, loginedMemberId);
		if(actorCanDeleteRd.isFail()) {
			return actorCanDeleteRd;
		}
		return replyService.deleteReply(id);
	}
	@RequestMapping("/adm/reply/doModify")
	@ResponseBody
	public ResultData doModify(Integer id, String body, HttpServletRequest req) {
		int loginedMemberId = (int)req.getAttribute("loginedMemberId");
		
		if(id == null) {
			return new ResultData("F-2", "수정할 댓글 번호를 입력해주세요");
		}
		if(body == null) {
			return new ResultData("F-2", "수정할 댓글 내용을 입력해주세요");
		}
		Reply reply = replyService.getReply(id);
		if(reply == null) {
			return new ResultData("F-1", "존재하지 않는 댓글입니다");
		}
		ResultData actorCanDeleteRd = replyService.getActorCanDeleteRd(reply, loginedMemberId);
		if(actorCanDeleteRd.isFail()) {
			return actorCanDeleteRd;
		}
		return replyService.modifyReply(id, body);
	}
}
