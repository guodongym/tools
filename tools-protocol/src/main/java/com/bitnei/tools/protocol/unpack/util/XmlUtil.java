package com.bitnei.tools.protocol.unpack.util;

import org.jdom2.*;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author chenpeng
 * @date 2020-02-21 10:25
 */
public class XmlUtil {

    /**
     * 将xml文件转为json对象
     * @param inputStream  xml文件流
     * @return
     */
    public static Element getRootElement(final InputStream inputStream){

        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(inputStream);
            Element root = doc.getRootElement();
            return root;
        }
        catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将xml文件转为json对象
     * @param filePath  xml文件路径
     * @return
     */
    public static Element getRootElement(final String filePath){

        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(filePath);
            Element root = doc.getRootElement();
            return root;
        }
        catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * XPath获取属性值
     * @param root
     * @param xPath
     * @return
     */
    public static String getXPathAttributeValue(Element root, String xPath){
        XPathExpression<Attribute> pp = XPathFactory.instance().compile(xPath, Filters.attribute());
        return pp.evaluateFirst(root).getValue();
    }

    /**
     * XPath获取节点值
     * @param root 主节点
     * @param xPath XPath字符串
     * @return 没有找到返回null
     */
    public static String getXPathText(Element root, String xPath){
        XPathExpression<Text> pp = XPathFactory.instance().compile(xPath,Filters.text());
        return pp.evaluateFirst(root).getText();
    }

    /**
     * XPath获取节点
     * @param root 主节点
     * @param xPath XPath字符串
     * @return 没有找到返回null
     */
    public static Element getXPathNode(Element root, String xPath){
        XPathExpression<Element> pp = XPathFactory.instance().compile(xPath,Filters.element());
        return pp.evaluateFirst(root);
    }

    /**
     * XPath获取节点集合
     * @param root 主节点
     * @param xPath XPath字符串
     * @return XPath得到的节点集合
     */
    public static List<Element> getXPathNodes(Element root, String xPath){
        XPathExpression<Element> pp = XPathFactory.instance().compile(xPath,Filters.element());
        return pp.evaluate(root);
    }
}
