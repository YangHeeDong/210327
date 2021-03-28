package com.sbs.untact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Article {
	private int id;
	private String title;
	private String body;
	private String regDate;
	private String updateDate;
}
