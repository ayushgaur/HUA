package com.qait.create;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.configuration.Form;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.plugins.crawloverview.CrawlOverview;
import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion.Setting;



public class CrawlerTest {

	public static void main(String arg[]) throws IOException{
	CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor("http://www.google.com");
	builder.crawlRules().insertRandomDataInInputForms(false);

	// click these elements
	builder.crawlRules().clickDefaultElements();
	builder.crawlRules().click("div");
	builder.crawlRules().crawlFrames(true);
	builder.crawlRules().crawlHiddenAnchors(true);
	builder.crawlRules().click("input").withAttribute("type", "submit");
	builder.crawlRules().click("input").withAttribute("type", "button");
	builder.crawlRules().click("button").withAttribute("type", "submit");
	builder.crawlRules().dontClick("a");
	
	

	builder.setMaximumStates(1);
	builder.setMaximumDepth(1);
	builder.crawlRules().clickElementsInRandomOrder(true);

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
	Form contactForm = new Form();
	contactForm.field("male").setValues(true, false);
	contactForm.field("female").setValues(false, true);
	contactForm.field("name").setValues("Bob", "Alice", "John");
	contactForm.field("phone").setValues("1234567890", "1234888888", "");
	contactForm.field("mobile").setValues("123", "3214321421");
	contactForm.field("type").setValues("Student", "Teacher");
	contactForm.field("active").setValues(true);
	input.setValuesInForm(contactForm).beforeClickElement("button").withText("Save");
	
	Form loginForm = new Form();
	loginForm.field(Settings.user_field).setValue(Settings.user_value);
	loginForm.field(Settings.password_field).setValue(Settings.password_value);
	input.setValuesInForm(loginForm).beforeClickElement(Settings.submit_field).withText(Settings.submit_text);

	return input;
}

}

