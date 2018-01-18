package com.qait.create;

public class Start_Hua {
	
	public static void main(String []arg) throws Exception{
		Settings.URL="http://hris.qainfotech.com/";
		
		Crawler.start(Settings.URL);
		HtmlParser.start();
		
	}

}

