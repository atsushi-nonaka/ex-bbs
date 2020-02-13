package com.example.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Article;
import com.example.domain.Comment;
import com.example.form.ArticleForm;
import com.example.form.CommentForm;
import com.example.repository.ArticleRepository;
import com.example.repository.CommentRepository;

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
	private ArticleRepository articleRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@ModelAttribute
	public ArticleForm setUpArticleForm() {
		return new ArticleForm();
	}
	
	@ModelAttribute
	public CommentForm setUpCommentForm() {
		return new CommentForm();
	}
	
	/**
	 * 記事,コメントを表示する.
	 * 
	 * @return 記事,コメント表示html
	 */
	@RequestMapping("")
	public String index(Model model) {
		List<Article> articleList = articleRepository.findAll();
		for(Article article : articleList) {
			List<Comment>commentList = commentRepository.findByArticleId(article.getId());
			article.setCommentList(commentList);
		}
		model.addAttribute("articleList", articleList);		
		return "bbs/bbs";
	}
	
	/**
	 * 記事を登録する.
	 * 
	 * @param articleForm 記事フォーム
	 * @return リダイレクト
	 */
	@RequestMapping("insert-article")
	public String insertArticle(ArticleForm articleForm) {
		Article article = new Article();
	    BeanUtils.copyProperties(articleForm, article);
	    articleRepository.insert(article);
	    return "redirect:/";
	}
	
	/**
	 * コメントを登録する.
	 * 
	 * @param commentForm コメントフォーム
	 * @return リダイレクト
	 */
	@RequestMapping("insert-comment")
	public String insertComment(CommentForm commentForm) {
		Comment comment = new Comment();
		BeanUtils.copyProperties(commentForm, comment);
		comment.setArticleId(commentForm.getIntArticleId());
		commentRepository.insert(comment);
		return "redirect:/";
	}
}
