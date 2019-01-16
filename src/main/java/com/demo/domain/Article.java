package com.demo.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "picks", indexes = @Index(columnList = "title, body"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
	/** ピックID */
	@Id
	@GeneratedValue
	private Integer id;
	
	/** ピックした記事のURL */
	@Column(nullable = false, unique = true)
	private String url;
	
	/** ピックした記事の画像のURL */
	@Column(nullable = false)
	private String imageUrl;
	
	/** ピックした記事のタイトル */
	@Column(nullable = false)
	private String title;
	
	/** ピックした記事の本文 */
	@Column(nullable = false)
	private String body;
	
	/** ピックした記事を扱っているニュースサイト名 */
	@Column(nullable = false)
	private String source;
}
