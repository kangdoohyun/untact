package com.sbs.untact.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Article;

@Controller
public class UsrArticleController {
	List<Article> articles;
	private int articlesLastId;
	
	public UsrArticleController() {
		articlesLastId = 0;
		articles = new ArrayList<>();
		
		articles.add(new Article(++articlesLastId, "2020/01/28", "제목", "내용"));
		
	}
	@RequestMapping("/usr/article/list")
	@ResponseBody
	public List<Article> showList(){
		return articles;
	}
	@RequestMapping("/usr/article/detail")
	@ResponseBody
	public Article showDetail(int id) {
		return articles.get(id - 1);
	}
	@RequestMapping("/usr/article/doAdd")
	@ResponseBody
	public Map<String, Object> doAdd(String regDate, String title, String body) {
		articles.add(new Article(++articlesLastId, regDate, title, body));
		Map<String, Object> rs = new HashMap<>();
		rs.put("resultCode", "S-1");
		rs.put("msg", "성공하였습니다.");
		rs.put("id", articlesLastId);
		return rs;
	}
	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public Map<String, Object> doDelete(int id) {
		boolean Select = deleteArticle(id);
		Map<String, Object> rs = new HashMap<>();
		if(Select) {
			rs.put("Code", "S-1");
			rs.put("msg", "게시물을 삭제했습니다");
		}
		else {
			rs.put("Code", "F-1");
			rs.put("msg", "존재하지 않는 게시물입니다");
		}
		rs.put("id", id);
		return rs;
	}
	private boolean deleteArticle(int id) {
		for(Article article : articles) {
			if(article.getId() == id) {
				articles.remove(article);
				return true;
			}
		}
		return false;
	}
	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public Map<String, Object> doModify(int id, String title, String body) {
		Article selArticle = null;
		Map<String, Object> rs = new HashMap<>();
		
		for (Article article : articles) {
			if(article.getId() == id) {
				selArticle = article;
				break;
			}
		}
		if(selArticle == null) {
			rs.put("Code", "F-1");
			rs.put("msg", String.format("%d번 게시물은 존재하지 않습니다", id));
		}
		selArticle.setTitle(title);
		selArticle.setBody(body);
		
		rs.put("Code", "S-1");
		rs.put("msg", String.format("%d번 게시물이 수정되었습니다", id));
		rs.put("id", id);
		
		return rs;
	}
}
