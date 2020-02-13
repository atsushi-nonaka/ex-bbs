package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Article;

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
	private final static RowMapper<Article> Article_ROW_MAPPER=(rs,i)->{
		Article article = new Article();
		article.setId(rs.getInt("id"));
		article.setName(rs.getString("name"));
		article.setContent(rs.getString("content"));
		return article;
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
	
}
