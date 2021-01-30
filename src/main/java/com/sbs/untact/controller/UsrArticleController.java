package com.sbs.untact.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.util.Util;

@Controller
public class UsrArticleController {
	List<Article> articles;
	private int articlesLastId;
	
	public UsrArticleController() {
		articlesLastId = 0;
		articles = new ArrayList<>();
		
		articles.add(new Article(++articlesLastId, "2020/01/28", "2020/01/28", "제목", "내용"));
		
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
	public ResultData doAdd(String title, String body) {
		String regDate = Util.getNowDate();
		String updateDate = regDate;
		
		articles.add(new Article(++articlesLastId, regDate, updateDate, title, body));
		return new ResultData("S-1", "성공하였습니다.", "id", articlesLastId);
	}
	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public ResultData doDelete(int id) {
		boolean Select = deleteArticle(id);
		if(Select == false) {
			return new ResultData("F-1", "존재하지 않는 게시물입니다");
			
		}
		return new ResultData("S-1", "게시물을 삭제했습니다", "id", id);
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
	public ResultData doModify(int id, String title, String body) {
		Article selArticle = null;
		
		for (Article article : articles) {
			if(article.getId() == id) {
				selArticle = article;
				break;
			}
		}
		if(selArticle == null) {
			return new ResultData("F-1", String.format("%d번 게시물은 존재하지 않습니다", id));
		}
		selArticle.setUpdateDate(Util.getNowDate());
		selArticle.setTitle(title);
		selArticle.setBody(body);
		
		return new ResultData("S-1", String.format("%d번 게시물이 수정되었습니다", id), "id", id);
	}
}
