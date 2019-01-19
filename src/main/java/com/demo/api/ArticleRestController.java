package com.demo.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.demo.domain.Article;
import com.demo.service.ArticleService;

/**
 * リクエストに対応する処理のエンドポイントとなるコントローラ
 * @author yosukenn
 *
 */
@RestController
@RequestMapping("api/articles")
public class ArticleRestController {
	@Autowired
	ArticleService articleService;
	
	/**
	 * 記事を取得するリクエストへのマッピング
	 * @param url 取得したいニュースのURL
	 * @return 取得した記事情報（Asrticle型）
	 * @throws IOException
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	Article postArticle(@RequestBody String url) throws IOException {
		return articleService.createArticle(url);
	}
	
	/**
	 * IOException の例外ハンドラー
	 * @param e エラーオブジェクト
	 * @return エラー時のJSONメッセージ
	 */
	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> handleIOException(IOException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
}
