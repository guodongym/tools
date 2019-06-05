package com.bitnei.tools.core.util;

import com.bitnei.tools.core.entity.TreeNode;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeUtil {

	/**
	 * 构造树结构的工具方法
	 * 
	 * @param list
	 * @param idField
	 * @param textField
	 * @return
	 */
	public static <T> List<TreeNode> buildTree(List<T> list, String idField, String textField) {
		List<TreeNode> roots = new ArrayList<TreeNode>();

		Map<Serializable, List<T>> itemMap = buildItemMap(list, idField);
		List<T> rootItems = itemMap.get(null);
		for (T rootItem : rootItems) {
			TreeNode root = new TreeNode();
			buildTree(rootItem, root, itemMap, idField, textField);
			roots.add(root);
		}

		return roots;
	}

	private static <T> void buildTree(T parent, TreeNode parentNode, Map<Serializable, List<T>> itemMap, String idField, String textField) {
		// 1. 复制结点
		copyItem(parent, parentNode, idField, textField);
		// 2. 递归
		Serializable parentCode = getFieldValue(parent, idField);
		List<T> children = itemMap.get(parentCode);
		if (children == null) {
			return;
		}

		List<TreeNode> childNodes = new ArrayList<TreeNode>(children.size());
		for (T child : children) {
			TreeNode childNode = new TreeNode();
			buildTree(child, childNode, itemMap, idField, textField);
			childNodes.add(childNode);
		}
		parentNode.setChildren(childNodes);
	}

	private static <T> void copyItem(T item, TreeNode node, String idField, String textField) {
		node.setKey((Serializable) getFieldValue(item, idField));
		node.setTitle((String) getFieldValue(item, textField));
	}

	private static <T> Map<Serializable, List<T>> buildItemMap(List<T> items, String idField) {
		Map<Serializable, List<T>> itemMap = new HashMap<Serializable, List<T>>();
		for (T item : items) {
			Serializable parentCode = getParentCode(item, idField);
			List<T> children = getChildren(itemMap, parentCode);
			children.add(item);
		}
		return itemMap;
	}

	private static <T> List<T> getChildren(Map<Serializable, List<T>> itemMap, Serializable parentCode) {
		List<T> children = itemMap.get(parentCode);
		if (children == null) {
			children = new ArrayList<T>();
			itemMap.put(parentCode, children);
		}
		return children;
	}

	private static <T> Serializable getParentCode(T item, String idField) {
		Serializable parentCode = null;
		T parent = getFieldValue(item, "parent");
		if (parent != null) {
			parentCode = getFieldValue(parent, idField);
		}
		return parentCode;
	}

	@SuppressWarnings("unchecked")
	private static <T, V> V getFieldValue(T item, String fieldName) {
		Class<?> clazz = item.getClass();
		try {
			Method method = clazz.getMethod("get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
			return (V) method.invoke(item);
		} catch (Exception e) {
		}
		return null;
	}
}
