/**
 * @项目名称: tools-model
 * @文件名称: JsonException.java
 * @author tianlihu
 * @Date: 2015-7-7
 * @Copyright: 2015 www.etiansoft.com Inc. All rights reserved.
 * 注意：本内容仅限于北京逸天科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.etiansoft.tools.aop;

public class JsonException extends Exception {

	private static final long serialVersionUID = 1L;

	public JsonException(Throwable e) {
		super(e);
	}

	public JsonException(String message, Throwable e) {
		super(message, e);
	}
}
