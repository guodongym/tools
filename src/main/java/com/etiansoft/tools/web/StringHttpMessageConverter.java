package com.etiansoft.tools.web;

import java.nio.charset.Charset;

public class StringHttpMessageConverter extends org.springframework.http.converter.StringHttpMessageConverter {

	public StringHttpMessageConverter() {
		super(Charset.forName("UTF-8"));
	}
}
