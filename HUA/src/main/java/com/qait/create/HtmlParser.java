package com.qait.create;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.xpath.XPathExpressionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.qait.utils.Spec_Writer;

public class HtmlParser {

	public static void start() throws XPathExpressionException {
		Document doc = null;
		try {
			doc = Jsoup.parse(new File(".//output//doms//index.html"), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String title = doc.title();
		System.out.println(title.substring(0, Math.min(title.length(), 15)));
		Spec_Writer spec = new Spec_Writer(title.substring(0, Math.min(title.length(), 15)).replaceAll(" ", "_"));
		String getLocator=null;;
		List<Element> ele = doc.body().getAllElements();
		for (Element node : ele) {
			switch (node.tagName()) {
			//case "a":
			case "button":
			case "input":
			case "select":
			case "label":
			case "textarea":
			case "frame":
			case "iframe":
				getLocator = checkforUniqueAttribute(node, doc);
				System.out.println(getLocator);
				String getPageObejct = pageObjectName(doc,node, getLocator);
				spec.writeToFile(getPageObejct);
				break;
			}
		}
	}

	public static boolean checkForAlphanumeric(String string) {
		if (string != null && !string.isEmpty()) {
			for (char c : string.toCharArray()) {
				if (Character.isDigit(c)) {
					return false;
				}
			}
		}

		return true;
	}
	
	public static boolean checkForSpecialCharacter(String string){
		if (string != null && !string.isEmpty()) {
			for (char c : string.toCharArray()) {
				if (!Character.isLetter(c)) {
					return false;
				}
			}
		}

		return true;
	}

	static String locatorType = null;
	static String locatorValue = null;
	static String getLocator = null;
	static String tagType = null;
	public static String checkforUniqueAttribute(Element node, Document doc) throws XPathExpressionException {
		

		if (node.hasAttr("id") && checkForAlphanumeric(node.attr("id"))
				&& doc.getElementsByAttributeValue("id", node.attr("id")).size() <= 1) {
			locatorType = "id";
			locatorValue = node.attr("id");
		}

		else if (node.hasAttr("class") && !node.attr("class").isEmpty()&&!doc.getElementsByAttributeValue("class", node.attr("class")).isEmpty()
				&& checkForAlphanumeric(node.attr("class"))
				&& doc.getElementsByAttributeValue("class", node.attr("class")).size() <= 1) {
			locatorType = "class";
			locatorValue = node.attr("class");
		}

		else if (node.hasAttr("name") && checkForAlphanumeric(node.attr("name"))&&!node.attr("name").isEmpty()
				&& doc.getElementsByAttributeValue("name", node.attr("name")).size() <= 1) {
			locatorType = "name";
			locatorValue = node.attr("name");

		} else if (node.tagName().equals("img")&&node.attr("src").isEmpty()
				&& !node.getElementsByAttribute("src").isEmpty()&&doc.getElementsByAttributeValue("src", node.attr("src")).size() <= 1) {
			locatorType = "css";
			locatorValue = "[src='" + node.attr("src") + "']";
		} else if (!node.cssSelector().isEmpty() && checkForAlphanumeric(node.cssSelector())) {
			locatorType = "css";
			locatorValue = node.cssSelector();
		} else if (node.hasAttr("title") &&node.attr("title").isEmpty()&& doc.getElementsByAttributeValue("title", node.attr("title")).size() <= 1) {
			locatorType = "css";
			locatorValue = "[title='" + node.attr("title") + "']";
		} else if (node.tagName().equals("a")) {
			int uniqueCounter = 0;
			if (node.hasText()&&checkForSpecialCharacter(node.text())) {
				for (Element anchor : doc.getElementsByTag("a")) {
					if (anchor.text().equals(node.text())) {
						uniqueCounter++;
					}
				}
				if (uniqueCounter == 1)
				locatorType = "linkText";
				locatorValue = node.text();
			} else{
				System.out.println("getLocator"+locatorType+locatorType);
				return getLocator = createXpath(node, doc);}
		} else
			return getLocator = createXpath(node, doc);

		return getLocator = "       " + locatorType + "       " + locatorValue;
	}
	static int count;
	public static String createXpath(Element node, Document doc) throws XPathExpressionException {
       count=0;
        for(Element ele:doc.body().getElementsByTag(node.tagName())){
        	if(node.toString().equals(ele.toString())){
        		break;
        	}
        	count++;
        }
        locatorType = "xpath";
        locatorValue = "(//"+node.tagName()+")["+count+"]";
        System.out.println("getLocator"+"   "+locatorType+"   " + locatorValue);
		return "       "+locatorType+"       " + locatorValue;
	}		
	
	public static String pageObjectName(Document doc, Element node, String getLocator){
		String locatorName;
		String locatorkey = node.parent().text().substring(0, Math.min(node.parent().text().length(), 15))+"_"+count;
		locatorName=node.tagName().toLowerCase()+"_";
		if(node.tagName().equals("a")){
			int uniqueCounter = 0;
			if (node.hasText()&&checkForSpecialCharacter(node.text())) {
				for (Element anchor : doc.getElementsByTag("a")) {
					if (anchor.toString().equals(node.toString())) {
						uniqueCounter++;
					}
				}
				if (uniqueCounter == 1){
					locatorkey=node.text().substring(0, Math.min(node.text().length(), 15));	
				}
				else
				      locatorkey=node.attr("href").replaceAll(" ", "").substring(0, Math.min(node.attr("href").length()-1, 15)).replaceAll("https://", "").replaceAll("/", "_").replaceAll("\\?", "_").replaceAll(":","_").replaceAll(".", "_");

			} 
		}else if(node.hasAttr("title")&&doc.getElementsMatchingText(node.attr("title")).size()<=1){
				locatorkey=node.attr("title").substring(0, Math.min(node.text().length(), 15));
		}else if(node.hasText()){//&&doc.getElementsMatchingText(node.text()).size()<=1){
			locatorkey=node.text().substring(0, Math.min(node.text().length(), 15));
		}else if(node.hasAttr("value")&&!node.attr("value").isEmpty()&&doc.getElementsMatchingText(node.attr("value")).size()<=1){
			locatorkey=node.attr("value").substring(0, Math.min(node.attr("value").length(), 15));
		}else{ if(!node.text().isEmpty()){locatorkey=node.text().substring(0, Math.min(node.text().length(), 15))+"_"+count;}
		      else if(!node.attr("title").isEmpty()){locatorkey=node.attr("title").substring(0, Math.min(node.attr("title").length(), 15))+"_"+count;}
		      else if(!node.attr("value").isEmpty()){locatorkey=node.attr("value").substring(0, Math.min(node.attr("value").length(), 15))+"_"+count;}
		      else if(!node.attr("src").isEmpty()){locatorkey=node.attr("src").replaceAll(" ", "").substring(0, Math.min(node.attr("src").length(), 15)).replaceAll("https://", "").replaceAll("/", "_").replaceAll("\\?", "_").replaceAll(":","_").replaceAll(".", "_")+"_"+count;}
		    }		
		return locatorName.trim()+locatorkey.substring(0, Math.min(locatorkey.length(), 15)).replace(" ", "_").trim()+"        "+getLocator;
	}
}
