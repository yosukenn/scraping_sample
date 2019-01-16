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
		// Article の各フィールドに代入する変数を宣言して初期化
		String imageUrl = "";
		String title = "";
		String body = "";
		String source = "";
		
		// 画像ソースの取得
		Elements imgElements = document.getElementsByAttributeValue("property", "og:image");
		if (imgElements != null) {
			imageUrl = imgElements.first().attr("content");
		} else {
			imageUrl = "https://lh3.googleusercontent.com/VVhNlcQA_r1FP-T09tiSPdASiiBAYsQ7jw0StynJmoIzqy1BxteCOJtlh_fXzl-_JCUNj0inwj-MM7-EYgeR3ObcihckA-FjK_CUrmGzIsEGYJfiyBhOH4JDftzEfPEFxFm-3ycY4lQ=w853-h570-no";
		}
		
		// タイトルの取得
		if ( document.getElementsByAttributeValue("property", "og:title") != null) {
			Elements titleElements = document.getElementsByAttributeValue("property", "og:title");
			title = titleElements.attr("content");
		} else if ( document.select("h1") != null ) {
			Elements titleElements = document.select("h1");
			title = titleElements.first().text();
		} else if ( document.select("h2") != null ) {
			Elements titleElements = document.select("h2");
			title = titleElements.first().text();
		} else {
			title = "記事タイトルを取得できませんでした。";
		}
		
		// 本文の取得
		Elements bodyElements = document.select("p");
		if (bodyElements != null) {
			body = bodyElements.first().text();
		} else {
			body = "本文の取得に失敗しました。";
		}
		
		// ソース
		Elements sourceElements = document.getElementsByAttributeValue("property", "og:site_name");
		if (sourceElements != null) {
			source = imgElements.first().attr("content");
		} else {
			source = url;
		}
		
		
		Article article = new Article();
		article.setUrl(url);
		article.setImageUrl(imageUrl);
		article.setTitle(title);
		article.setBody(body);
		article.setSource(source);
		
		return articleRepository.save(article);
	}
}
