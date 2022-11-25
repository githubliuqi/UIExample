package com.shell.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class XmlHelper
{
	private static final String NODE_MANIFEST = "manifest";
	private static final String NODE_APPLICATION = "application";
	private static final String ATTR_PACKAGE_ = "package";
	private static final String ATTR_ANDROID_NAME = "android:name";
	public static String getAppName(InputStream is)  {
//		File file = new File("file/AndroidManifest.xml");
//		try {
//			FileInputStream f= new FileInputStream(file);
//			System.out.println();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		InputStream fis;
		
		//fis = new FileInputStream(file);
		//fis = new ByteArrayInputStream(data);
//			List list1 = getList(fis, NODE_MANIFEST);
//			Map<String, String> map1 = (Map<String, String>) list1.get(0);
//			if (map1.containsKey(ATTR_PACKAGE_))
//			{
//				String packageName =map1.get(ATTR_PACKAGE_);
//				System.out.println("包名： "+packageName);
//			}
		
		//fis = new FileInputStream(file);
		
		System.out.println("-----------");
		//System.out.println(fis);
		List list2 = getList(is, NODE_APPLICATION);
		Map<String, String> map2 = (Map<String, String>) list2.get(0);
		if (map2.containsKey(ATTR_ANDROID_NAME))
		{
			String applicationName =map2.get(ATTR_ANDROID_NAME);
			System.out.println("应用程序名： "+applicationName);
		}
		
		return "";
	}
	
	private static List getList(InputStream is, String nodeName) {
		// 创建一个解析XML的工厂对象
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		// 创建一个解析XML的对象
		try {
			SAXParser parser = parserFactory.newSAXParser();
			// 创建一个解析助手类
			XmlDocHelper myhandler = new XmlDocHelper(nodeName);
			// parser.parse(uri, myhandler);

			parser.parse(is, myhandler);
			List<Map<String, String>> list = myhandler.getList();
			System.out.println("list :" + list);
			return list;
		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getApplicationName(File xml)
	{
		DocumentBuilderFactory docFact = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFact.newDocumentBuilder();
			Document doc = docBuilder.parse(xml);
			NodeList nodeList = doc.getElementsByTagName("application");
			Element element = (Element) nodeList.item(0);
			return element.getAttribute("android:name");
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 设置androidmanifest.xml的Application name属性
	 * @param xml
	 * @param name
	 */
	public static boolean setApplicationName(File xml, String name)
	{
		DocumentBuilderFactory docFact = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFact.newDocumentBuilder();
			Document doc = docBuilder.parse(xml);
			NodeList nodeList = doc.getElementsByTagName("application");
			Element element = (Element) nodeList.item(0);
			element.setAttribute("android:name", name);
			// 删除 debugable和allowbackup属性
//			if (!element.hasAttribute("android:debuggable"))
//			{
//				element.removeAttribute("android:debuggable");
//			}
			element.setAttribute("android:debuggable", "false");
			element.setAttribute("android:allowBackup", "false");
//			if (element.hasAttribute("android:allowBackup"))
//			{
//				element.removeAttribute("android:allowBackup");
//			}
			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer transformer = transFact.newTransformer();
			DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xml);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            return true;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
}

class XmlDocHelper extends DefaultHandler{

	// 存储正在解析的元素的数据
	private Map<String, String> map = null;
	// 存储所有解析的元素的数据
	private List<Map<String, String>> list = null;
	//正在解析的元素的名字
	private String currentTag = null;
	//正在解析的元素的元素值
	private String currentValue = null;
	// 开始解析的元素
	private String nodeName = null;
	
	
	
	public XmlDocHelper(String nodeName)
	{
		this.nodeName = nodeName;
	}
	
//	public XmlDocHelper(String[] nodeNames)
//	{
//		this.nodeName = nodeName;
//	}

	public List<Map<String, String>> getList() {
		return list;
	}
	 
	@Override
	public void startDocument() throws SAXException {
		System.out.println("--startDocument()--");
		list=new ArrayList<Map<String,String>>();
	}

	@Override
	public void endDocument() throws SAXException {
		System.out.println("--endDocument()--");
		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		System.out.println("--startElement()--"+qName);
		//判断正在解析的元素是不是开始解析的元素
		if (qName.equals(nodeName))
		{
			map = new HashMap<String, String>();
		}else
		{
			return;
		}
		//判断正在解析的元素是否有属性值,如果有则将其全部取出并保存到map对象中，如:<person id="00001"></person>
	    if (attributes != null && map != null)
	    {
	    	for (int i=0 ; i< attributes.getLength();i++)
	    	{
	    		//System.out.println("key = "+attributes.getQName(i) +", value = "+ attributes.getValue(i));
	    		map.put(attributes.getQName(i), attributes.getValue(i));
	    	}
	    }
	    currentTag=qName;  //正在解析的元素
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		System.out.println("--endElement()--" + qName);
		// 判断是否为一个节点结束的元素标签
		if (qName.equals(nodeName)) {
			list.add(map);
			map = null;
		}
	}
	 //解析到每个元素的内容时会调用此方法
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (currentTag != null && map != null) {
			currentValue = new String(ch, start, length);
			// 如果内容不为空和空格，也不是换行符则将该元素名和值和存入map中
			if (currentValue != null && !currentValue.trim().equals("")
					&& !currentValue.trim().equals("\n")) {
				map.put(currentTag, currentValue);
				System.out.println("-----" + currentTag + " " + currentValue);
			}
			// 当前的元素已解析过，将其置空用于下一个元素的解析
			currentTag = null;
			currentValue = null;
		}
	}
	
}
