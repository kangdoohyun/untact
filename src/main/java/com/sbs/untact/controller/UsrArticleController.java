package com.sbs.untact.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Board;
import com.sbs.untact.dto.Reply;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;
import com.sbs.untact.util.Util;

@Controller
public class UsrArticleController {
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping("/usr/article/list")
	@ResponseBody
	public ResultData showList(String searchKeywordType, String searchKeyword, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "1") int boardId){
		Board board = articleService.getBoard(boardId);
		if(board == null) {
			return new ResultData("F-1", "존재하지 않는 게시판입니다");
		}
		if(searchKeywordType != null) {
			searchKeywordType = searchKeywordType.trim();
		}
		if(searchKeywordType == null || searchKeywordType.length() == 0) {
			searchKeywordType = "titleAndBody";
		}
		if(searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = null;
		}
		if(searchKeyword != null) {
			searchKeyword = searchKeyword.trim();
		}
		int itemsInAPage = 20;
		List<Article> articles = articleService.getForPrintArticles(searchKeywordType, searchKeyword, page,  itemsInAPage, boardId); 
		return new ResultData("S-1", "성공", "arrticles", articles);
	}
	@RequestMapping("/usr/article/detail")
	@ResponseBody
	public ResultData showDetail(Integer id) {
		if(id == null) {
			return new ResultData("F-1", "ID를 입력해주세요");
		}
		Article article = articleService.getForPrintArticle(id);
		if(article == null) {
			return new ResultData("F-2", "존재하지 않는 게시물번호입니다");
		}
		return new ResultData("S-1", "성공했습니다", "article", article);
	}
	@RequestMapping("/usr/article/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param, HttpSession session) {
		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);
		
		if (param.get("title") == null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}

		if (param.get("body") == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}
		param.put("memberId", loginedMemberId);
		return articleService.addArticle(param);
	}
	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id, HttpSession session) {
		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);
		
		if(id == null) {
			return new ResultData("F-2", "게시물 번호를 입력해주세요");
		}
		Article article = articleService.getArticle(id);
		if(article == null) {
			return new ResultData("F-1", "존재하지 않는 게시물입니다");
		}
		ResultData actorCanDeleteRd = articleService.getActorCanDeleteRd(article, loginedMemberId);
		if(actorCanDeleteRd.isFail()) {
			return actorCanDeleteRd;
		}
		return articleService.deleteArticle(id);
	}
	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public ResultData doModify(Integer id, String title, String body, HttpSession session) {
		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);
		
		if(id == null) {
			return new ResultData("F-1", "id를 입력해주세요");
		}
		if(title == null) {
			return new ResultData("F-1", "title을 입력해주세요");
		}
		if(body == null) {
			return new ResultData("F-1", "body를 입력해주세요");
		}
		Article article = articleService.getArticle(id);
		if(article == null) {
			return new ResultData("F-1", "존재하지 않는 게시물 입니다");
		}
		ResultData actorCanModifyRd = articleService.getActorCanModifyRd(article, loginedMemberId);
		if(actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}
		return articleService.modifyArticle(id, title, body);
	}
	@RequestMapping("/usr/article/doAddReply")
	@ResponseBody
	public ResultData doAddReply(@RequestParam Map<String, Object> param, HttpSession session) {
		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);
		if (param.get("articleId") == null) {
			return new ResultData("F-1", "articleId를 입력해주세요.");
		}
		if (param.get("body") == null) {
			return new ResultData("F-1", "body을 입력해주세요.");
		}
		
		param.put("memberId", loginedMemberId);
		return articleService.addReply(param);
	}
	@RequestMapping("/usr/article/doDeleteReply")
	@ResponseBody
	public ResultData doDeleteReply(Integer id, HttpSession session) {
		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);
		
		if(id == null) {
			return new ResultData("F-2", "삭제할 댓글 번호를 입력해주세요");
		}
		Reply reply = articleService.getReply(id);
		if(reply == null) {
			return new ResultData("F-1", "존재하지 않는 댓글 입니다");
		}
		ResultData actorCanDeleteRd = articleService.getActorCanDeleteRdReply(reply, loginedMemberId);
		if(actorCanDeleteRd.isFail()) {
			return actorCanDeleteRd;
		}
		return articleService.deleteReply(id);
	}
	@RequestMapping("/usr/article/doModifyReply")
	@ResponseBody
	public ResultData doModifyReply(Integer id, String body, HttpSession session) {
		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);
		
		if(id == null) {
			return new ResultData("F-1", "id를 입력해주세요");
		}
		if(body == null) {
			return new ResultData("F-1", "body를 입력해주세요");
		}
		Reply reply = articleService.getReply(id);
		if(reply == null) {
			return new ResultData("F-1", "존재하지 않는 댓글 입니다");
		}
		ResultData actorCanModifyRd = articleService.getActorCanModifyRdReply(reply, loginedMemberId);
		if(actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}
		return articleService.modifyReply(id, body);
	}
	@RequestMapping("/usr/article/replies")
	@ResponseBody
	public ResultData replies(Integer articleId, @RequestParam(defaultValue = "1") int page) {
		if(articleId == null) {
			return new ResultData("F-1", "댓글을 확인할 게시물 번호 를 입력해주세요");
		}
		int itemsInAPage = 3;
		List<Reply> replies = articleService.getForPrintReplies(page, itemsInAPage); 
		return new ResultData("S-1", "성공", "replies", replies);
	}
}
