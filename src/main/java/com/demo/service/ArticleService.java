package com.demo.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.Article;
import com.demo.repository.ArticleRepository;

/**
 * ニュース記事に関するサービスクラス
 * @author yosukenn
 *
 */
@Service
@Transactional
public class ArticleService {
	@Autowired
	ArticleRepository articleRepository;
	
	/**
	 * ユーザが入力したニュース記事のURLから記事オブジェクトを生成する業務ロジック
	 * @param url ユーザが入力したニュース記事のURL
	 * @return 生成した記事オブジェクト
	 * @throws IOException
	 */
	public Article createArticle(String url) throws IOException {
		Article article = getNewsInfo(url);
		return articleRepository.save(article);
	}
	
	/**
	 * URLからニュース記事の情報をスクレイピングしてくるメソッド
	 * @param url ユーザが入力したURL
	 * @return スクレイピングした記事情報から生成した記事オブジェクト
	 * @throws IOException
	 */
	private Article getNewsInfo(String url) throws IOException {
		// 渡されたURLのHTML情報を取得
		Document document = Jsoup.connect(url).get();

		// タスク
		// （同じ記事がピックされた時の処理）
		
		// 画像ソースの取得
		String imageUrl;
		if (document.getElementsByAttributeValue("property", "og:image") != null) {
			Elements imgElements = document.getElementsByAttributeValue("property", "og:image");
			imageUrl = imgElements.first().attr("content");
		} else {
			imageUrl = "https://lh3.googleusercontent.com/VVhNlcQA_r1FP-T09tiSPdASiiBAYsQ7jw0StynJmoIzqy1BxteCOJtlh_fXzl-_JCUNj0inwj-MM7-EYgeR3ObcihckA-FjK_CUrmGzIsEGYJfiyBhOH4JDftzEfPEFxFm-3ycY4lQ=w853-h570-no";
		}
		
		// タイトルの取得
		String title;
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
		
		// 本文の取得[
		// 文字数制限の追加 - なぜかよきに計らってくれている。 Rails でのロジックは以下
//	    if page.search('p')
//	      texts = page.search('p')
//	      i = 0
//	      while texts[i] && results[:body].length <= 40
//	        results[:body] += texts[i]
//	        i += 1
//	      end
//	    end
		Elements bodyElements = document.select("p");
		String body;
		if (bodyElements != null) {
			body = bodyElements.first().text();
		} else {
			body = "本文の取得に失敗しました。";
		}
		
		// ソース
		Elements sourceElements = document.getElementsByAttributeValue("property", "og:site_name");
		String source;
		if (sourceElements != null) {
			source = sourceElements.first().attr("content");
		} else {
			source = url;
		}
		
		Article article = new Article();
		article.setUrl(url);
		article.setImageUrl(imageUrl);
		article.setTitle(title);
		article.setBody(body);
		article.setSource(source);
		
		return article;
	}
}
