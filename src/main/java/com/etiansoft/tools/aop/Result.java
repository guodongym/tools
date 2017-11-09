/**
 * @项目名称: estate-web
 * @文件名称: Result.java
 * @author tianlihu
 * @Date: 2015-5-8
 * @Copyright: 2015 www.etiansoft.com Inc. All rights reserved.
 * 注意：本内容仅限于北京逸天科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.etiansoft.tools.aop;

import java.io.Serializable;

public class Result implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Result SUCCESS = new Result(false, "执行成功");

	private boolean error;
	private String message;

	public static final Result error(String message) {
		return new Result(true, message);
	}

	public Result(boolean error, String message) {
		this.error = error;
		this.message = message;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
