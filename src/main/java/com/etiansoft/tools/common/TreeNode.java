package com.etiansoft.tools.common;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class TreeNode implements Serializable {

	private static final long serialVersionUID = 4083579947137126329L;

	private String title;
	private boolean isForder;
	private Serializable key;
	private boolean expand = true;
	private boolean select;

	private List<TreeNode> children;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isIsForder() {
		return isForder;
	}

	public void setIsForder(boolean isForder) {
		this.isForder = isForder;
	}

	public Serializable getKey() {
		return key;
	}

	public void setKey(Serializable key) {
		this.key = key;
	}

	public boolean isExpand() {
		return expand;
	}

	public void setExpand(boolean expand) {
		this.expand = expand;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public List<TreeNode> getChildren() {
		if (children == null) {
			children = Collections.emptyList();
		}
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
		if (children != null && !children.isEmpty()) {
			isForder = true;
		}
	}
}
