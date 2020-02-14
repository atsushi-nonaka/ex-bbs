package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Article;
import com.example.domain.Comment;

/**
 * 記事を操作するレポジトリ.
 * 
 * @author nonaa
 *
 */
@Repository
public class ArticleRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	/** テーブル名 */
	private String TABLE_NAME = "articles";
	
	/**
	 * 記事を登録するローマッパー.
	 */
	private final static RowMapper<Article> Article_ROW_MAPPER=(rs, i)->{
		Article article = new Article();
		article.setId(rs.getInt("art_id"));
		article.setName(rs.getString("art_name"));
		article.setContent(rs.getString("art_content"));
		return article;
	};
	
	private final static ResultSetExtractor<List<Article>> Article_RESULT_SET_EXTRACTOR=(rs)->{
		//下は中級
		List<Article> articleList = new ArrayList<>();
		List<Comment> commentList = null;
		int beforeArticleId = 0;
		while(rs.next()) {
			int nowArticleId = rs.getInt("art_id");
			if(beforeArticleId != nowArticleId) {
				Article article = new Article();				
				article.setId(nowArticleId);
				article.setName(rs.getString("art_name"));
				article.setContent(rs.getString("art_content"));				
				commentList = new ArrayList<>();
				article.setCommentList(commentList);
				articleList.add(article);
			}
			
			if(rs.getInt("com_id") != 0) {
				Comment comment = new Comment();
				comment.setId(rs.getInt("com_id"));
				comment.setName(rs.getString("com_name"));
				comment.setContent(rs.getString("com_content"));				
				comment.setArticleId(rs.getInt("com_article_id"));
				commentList.add(comment);																	
			}
				
			
			beforeArticleId = nowArticleId;
		}
		return articleList;
	};
	
	/**
	 * 記事リスト一覧表示を取得する.
	 * 
	 * @return 記事一覧リスト
	 */
	public List<Article> findAll(){
		String sql = "SELECT id, name, content FROM " + TABLE_NAME + " ORDER BY id desc";
		List<Article> articleList = template.query(sql, Article_ROW_MAPPER);
		return articleList;
	}
	
	//中級課題
	public List<Article> findAllJoin(){
		String sql = "SELECT art.id art_id, art.name art_name, art.content art_content, com.id com_id, com.name com_name, com.content com_content, com.article_id com_article_id "
					 + "FROM articles art LEFT OUTER JOIN comments com ON art.id = com.article_id ORDER BY art.id DESC, com.id DESC";
		List<Article> articleList = template.query(sql, Article_RESULT_SET_EXTRACTOR);
		return articleList;
	}
	
	/**
	 * 記事の投稿をする.
	 * 
	 * @param article 記事
	 */
	public void insert(Article article) {
		String sql = "INSERT INTO " + TABLE_NAME + "(name, content) VALUES(:name, :content)";
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(article);
		template.update(sql, param);
	}
	
	/**
	 * 記事を1件削除する.
	 * 
	 * @param id ID
	 */
	public void deletedById(int id) {
		String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}
	
}
