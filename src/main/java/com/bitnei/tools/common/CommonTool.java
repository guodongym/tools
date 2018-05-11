package com.bitnei.tools.common;

import com.bitnei.tools.entity.TreeNode;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

public class CommonTool {

	/**
	 * 判断两个对象是否相等
	 */
	public static boolean equal(Object o1, Object o2) {
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	public static void convert(Object source, Object target) {
		if (source == null || target == null) {
			return;
		}
		Method[] sourceMethods = source.getClass().getMethods();
		Map<String, Method> sourceMethodMap = new HashMap<String, Method>();
		for (Method sourceMethod : sourceMethods) {
			String sourceMethodName = sourceMethod.getName();
			if (sourceMethodName.startsWith("get") && sourceMethodName.length() > 3 && sourceMethod.getParameterTypes().length == 0) {
				String sourceFieldName = sourceMethodName.substring(3);
				sourceMethodMap.put(sourceFieldName, sourceMethod);
			}
		}

		Method[] targetMethods = target.getClass().getMethods();
		Map<String, Method> targetMethodMap = new HashMap<String, Method>();
		for (Method targetMethod : targetMethods) {
			String targetMethodName = targetMethod.getName();
			if (targetMethodName.startsWith("set") && targetMethodName.length() > 3 && targetMethod.getParameterTypes().length == 1) {
				String targetFieldName = targetMethodName.substring(3);
				targetMethodMap.put(targetFieldName, targetMethod);
			}
		}

		Set<Entry<String, Method>> sourceMethodEntries = sourceMethodMap.entrySet();
		for (Entry<String, Method> sourceMethodEntry : sourceMethodEntries) {
			String fieldName = sourceMethodEntry.getKey();
			Method sourceMethod = sourceMethodEntry.getValue();
			Method targetMethod = targetMethodMap.get(fieldName);
			if (targetMethod != null) {
				copyProperty(source, sourceMethod, target, targetMethod);
			}
		}
	}

	private static void copyProperty(Object source, Method sourceMethod, Object target, Method targetMethod) {
		Class<?> sourceType = sourceMethod.getReturnType();
		Class<?> targetType = targetMethod.getParameterTypes()[0];
		if (!sourceType.equals(targetType)) {
			return;
		}

		try {
			Object value = sourceMethod.invoke(source);
			targetMethod.invoke(target, value);
		} catch (Exception e) {
		}
	}

	public static <T> T convert(Object source, Class<T> targetClass) {
		if (source == null) {
			return null;
		}
		try {
			T target = targetClass.newInstance();
			convert(source, target);
			return target;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 用来简化PropertyUtils的copyProperties用法, 两个列表的对象个数要相等
	 */
	public static void convert(List<?> sources, List<?> targets) {
		if (sources == null && !targets.isEmpty()) {
			targets.clear();
		} else if (sources != null && targets != null && sources.size() == targets.size()) {
			for (int i = 0; i < sources.size(); i++) {
				convert(sources.get(i), targets.get(i));
			}
		}
	}

	/**
	 * 用来简化PropertyUtils的copyProperties用法, 两个列表的对象个数要相等
	 */
	public static <T> List<T> convert(List<?> sources, Class<T> targetClass) {
		List<?> list = sources;
		if (list == null) {
			list = Collections.emptyList();
		}
		List<T> targets = new ArrayList<T>(list.size());
		convert(list, targets, targetClass);
		return targets;
	}

	/**
	 * 用来简化PropertyUtils的copyProperties用法
	 */
	public static <T> void convert(List<?> sources, List<T> targets, Class<T> targetClass) {
		if (targets == null) {
			return;
		}
		targets.clear();
		if (sources == null) {
			return;
		}
		for (int i = 0; i < sources.size(); i++) {
			try {
				T target = targetClass.newInstance();
				targets.add(target);
				convert(sources.get(i), target);
			} catch (Exception e) {
			}
		}
	}

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
