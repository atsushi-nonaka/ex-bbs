package com.example.controller;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Article;
import com.example.form.ArticleForm;
import com.example.repository.ArticleRepository;

/**
 * 記事を表示するクラス
 * 
 * @author nonaa
 *
 */
@Controller
@RequestMapping("")
public class ArticleController {

	@Autowired
	private ServletContext application;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@ModelAttribute
	public ArticleForm setUpForm() {
		return new ArticleForm();
	}
	
	@RequestMapping("")
	public String index() {
		List<Article> articleList = articleRepository.findAll();
		application.setAttribute("articleList", articleList);
		return "bbs/bbs";
	}
	
	@RequestMapping("insert")
	public String insertArticle(ArticleForm articleForm ,Model model) {
		Article article = new Article();
	    BeanUtils.copyProperties(articleForm, article);
	    articleRepository.insert(article);
	    return index();
	}
}
