package com.qait.tags;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.qait.utils.Spec_Writer;

public class Anchor {

	public String title;
	public Anchor(String title){
		this.title=title;
	}
	
	public void createAnchorLocators(Spec_Writer spec,Elements anchors){
		
		for(Element anchorTag: anchors){
			String locator=null;
			String locatorValue=null;
			String locatorType=null;
			System.out.println("anchor tag"+ anchorTag.toString());
			System.out.println("anchor id"+anchorTag.attr("id"));
			System.out.println("anchor id"+anchorTag.attr("class"));

				if(anchorTag.hasAttr("id")){
					locatorType="id";
					locatorValue=anchorTag.attr("id");
				}else if(anchorTag.hasAttr("class")){
					locatorType="classname";
					locatorValue=anchorTag.attr("class");
				}
			spec.writeToFile("a_"+locatorValue+"    "+locatorType+"   "+locatorValue);		
	
		}
	}
	
}
