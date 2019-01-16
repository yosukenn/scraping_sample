package com.demo.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.demo.domain.Article;
import com.demo.service.ArticleService;

@RestController
@RequestMapping("api/articles")
public class ArticleRestController {
	@Autowired
	ArticleService articleService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	Article postArticle(@RequestBody String url) throws IOException {
		return articleService.createArticle(url);
	}
}
