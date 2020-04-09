package com.leewan.util;

import java.nio.charset.Charset;

import org.springframework.http.converter.StringHttpMessageConverter;

public class LeewanStringHttpMessageConverter extends StringHttpMessageConverter {

	public LeewanStringHttpMessageConverter() {
		super(Charset.forName("UTF-8"));
	}
}
