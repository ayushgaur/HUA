package com.qait.automation.TestInitiator;


import java.net.MalformedURLException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import com.qait.automation.TestInitiator.WebDriverFactory;



public class TestSessionInitiator {

	public static String browserVersion = "";
	public TakesScreenshot takescreenshot;
	protected WebDriver driver;
	String browser;
	String seleniumserver;
	String seleniumserverhost;
	private String testname;

	// --------------------Keywords--------------


	public TestSessionInitiator(String testname) throws MalformedURLException {
		this.testname = testname;
		testInitiator(testname);
	}

	public WebDriver getDriver() {
		return this.driver;
	}

	public void codestartedFor(String s) {
		Reporter.log("--------------------------------------------------", true);
		Reporter.log("--------------------------------------------------", true);
		Reporter.log("Code Started for :-  \"" + s + "\"", true);
		Reporter.log("--------------------------------------------------", true);
		Reporter.log("--------------------------------------------------", true);
	}

	public void launchApplicationAsStudent() {
		launchApplication("student");
	}

	public void launchApplication(String base_url) {
		Reporter.log(" The application url is :- " + base_url, true);
		String uAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;");
		Reporter.log("Current OS Browser configuration:" + uAgent, true);
		driver.manage().deleteAllCookies();
		driver.get(base_url);
	}

	public void launchUrl(String url) {
		Reporter.log("[INFO]: The Generic URL :- " + url, true);
		driver.get(url);
	}

	public void closeTestSession() {
		System.out.println("\n");
		Reporter.log("[INFO]: The Test: \"" + this.testname.toUpperCase() + "\" COMPLETED!" + "\n", true);
		driver.quit();
	}

	public void closeWebDriver() {
		Reporter.log("[INFO]: The Test: \"" + this.testname.toUpperCase() + "\" COMPLETED!" + "\n", true);
		driver.close();
	}

	private void _initPage() throws MalformedURLException {
		this.driver = WebDriverFactory.getDriver();

		// -----------------------
		// Keywords----------------------------------------
		}

	private void testInitiator(String testname) throws MalformedURLException {
		_initPage();
		codestartedFor(testname);
	}

	public void stepStartMessage(String testStepName) {
		Reporter.log(" ", true);
		Reporter.log("------------------------------------------------------------------------------", true);
		Reporter.log("***** STARTING TEST STEP:- " + testStepName + " *****", true);
		Reporter.log(" ", true);
	}
}
