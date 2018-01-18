package com.qait.create;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.Form;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.plugins.crawloverview.CrawlOverview;

public class Crawler {

	protected static String diff_dir;

	/**
	 * Initialize and Run the crawler
	 * 
	 * @throws Exception
	 */
	public static void start(String url) throws Exception {

		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(url);
		builder.crawlRules().insertRandomDataInInputForms(false);

		// click these elements
//		builder.crawlRules().clickDefaultElements();
//		builder.crawlRules().click("div");

		builder.setMaximumStates(2);
		builder.setMaximumDepth(2);
		builder.crawlRules().clickElementsInRandomOrder(false);
		builder.crawlRules().click("input").withAttribute("type", "submit");
		builder.crawlRules().click("input").withAttribute("type", "button");
		builder.crawlRules().click("button").withAttribute("type", "submit");
		builder.crawlRules().dontClick("a");


		// Set timeouts
		builder.crawlRules().waitAfterReloadUrl(200, TimeUnit.MILLISECONDS);
		builder.crawlRules().waitAfterEvent(20, TimeUnit.MILLISECONDS);
		builder.crawlRules().setInputSpec(getInputSpecification());

		
		// We want to use two browsers simultaneously.
		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.CHROME, 1));

		// CrawlOverview
		builder.addPlugin(new CrawlOverview());
		File outFolder = new File(Settings.OUT_DIR);
		if (outFolder.exists()) {
			FileUtils.deleteDirectory(outFolder);
		}
		builder.setOutputDirectory(outFolder);

		CrawljaxRunner crawljax = new CrawljaxRunner(builder.build());
		crawljax.call();
	}

	private static InputSpecification getInputSpecification() {
		InputSpecification input = new InputSpecification();
		Form loginForm = new Form();
		loginForm.field(Settings.user_field).setValue(Settings.user_value);
		loginForm.field(Settings.password_field).setValue(Settings.password_value);
		input.setValuesInForm(loginForm).beforeClickElement(Settings.submit_field).withText(Settings.submit_text);
		return input;
	}

}
