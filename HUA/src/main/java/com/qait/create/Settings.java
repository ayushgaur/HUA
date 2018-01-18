package com.qait.create;


import java.io.File;
import java.net.MalformedURLException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import com.qait.automation.TestInitiator.WebDriverFactory;



public class Settings {

	public static String seleniumserver;
	public static String seleniumserverhost;
	public static WebDriver driver;
	public static final int CRAWL_DEPTH = 2; /* 0 = unlimited. */
	public static final int MAX_STATES = 2; /* 0 = unlimited. */
	public static final int MAX_RUNTIME = 5; /* 5 minutes. */
	public static final int WAIT_TIME_AFTER_EVENT = 500;
	public static final int WAIT_TIME_AFTER_RELOAD = 500;
	public static String URL = "";
	public static String user_value="ayushgaur";
	public static String password_value="@yush@1234512";
	public static String user_field="txtUserName";
	public static String password_field="txtPassword";
	public static String submit_field="btnSignIn";
	public static String submit_text="Sign In";

	// Output Settings
	public static final String FILESEPARATOR = File.separator;
	public static final String GEN_PO_DIR = "po" + FILESEPARATOR;
	public static final String OUT_DIR = "output" + FILESEPARATOR;
	public static final String DOMS_DIR = OUT_DIR + "doms" + FILESEPARATOR;
	public static final String CLUST_DIR = OUT_DIR + "clusters" + FILESEPARATOR;
	public static final String BROWSER = "CH"; // PH-FF

}
