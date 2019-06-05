/**
 * @项目名称: tools
 * @文件名称: FileEntry.java
 * @author tianlihu
 * @Date: 2015-5-27
 * @Copyright: 2015 www.etiansoft.com Inc. All rights reserved.
 * 注意：本内容仅限于北京逸天科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.bitnei.tools.web.entity;

import java.io.Serializable;

public class FileEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fileName;
	private String filePath;
	private Long length;

	public FileEntry(String fileName, String filePath, Long length) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.length = length;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

}