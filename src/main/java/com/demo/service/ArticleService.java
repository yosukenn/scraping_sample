package com.demo.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.Article;
import com.demo.repository.ArticleRepository;

@Service
@Transactional
public class ArticleService {
	@Autowired
	ArticleRepository articleRepository;
	
	public Article createArticle(String url) throws IOException {
		// 渡されたURLのHTML情報を取得
		Document document = Jsoup.connect(url).get();
		// Article の各フィールドに代入する変数の宣言
		String imageUrl = "";
		String title = "";
		String body = "";
		String source = "";
		
		// 画像ソースの取得
		Elements imgElements = document.select("img");
		if (imgElements != null) {
			imageUrl = imgElements.first().attr("src");
		}
		
		// タイトルの取得
		Elements titleElements = document.select("h1");
		if (titleElements != null) {
			title = titleElements.first().text();
		}
		
		// 本文の取得
		Elements bodyElements = document.select("p");
		if (bodyElements != null) {
			body = bodyElements.first().text();
		}
		
		// ソース
		source = "俺";
		
		Article article = new Article();
		article.setUrl(url);
		article.setImageUrl(imageUrl);
		article.setTitle(title);
		article.setBody(body);
		article.setSource(source);
		
		return articleRepository.save(article);
	}
}
