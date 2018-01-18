/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qait.automation.TestInitiator;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;



public class WebDriverFactory extends EventFiringWebDriver{

	public static String driverPath = "src/test/resources/selenium-drivers/";
	  //private WebDriver REAL_DRIVER = null;
	public WebDriverFactory() throws MalformedURLException {
		super(getDriver());
		// TODO Auto-generated constructor stub
	}

	 private static final Thread CLOSE_THREAD = new Thread() {
	        @Override
	        public void run() {
	      //      REAL_DRIVER.close();
	        }
	    };

	    static {
	        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
	    }
	    
	    @Override
	    public void close() {
	        if (Thread.currentThread() != CLOSE_THREAD) {
	            throw new UnsupportedOperationException("You shouldn't close this WebDriver. It's shared and will close when the JVM exits.");
	        }
	        super.close();
	    }

	
	public static ThreadLocal<WebDriver>  REAL_DRIVER = new ThreadLocal<WebDriver>();
	
	public static WebDriver getDriverThread() {
        return REAL_DRIVER.get();
    }
 
    static void setWebDriver(WebDriver driver) {
    	REAL_DRIVER.set(driver);
    }
	
    static WebDriver driver; 
    public enum DriverType {
        CHROME, FIREFOX, SAFARI, IE, MOBILE
    }

    public enum DriverLocation {
        LOCAL, REMOTE, BROWSERSTACK
    }

    
    public static WebDriver getDriver() throws MalformedURLException {
        DriverType type = DriverType.valueOf(System.getProperty("browser").toUpperCase());
        DriverLocation location = DriverLocation.valueOf(System.getProperty("server").toUpperCase());
        return getDriver(type, location);
    }

    public static WebDriver getDriver(DriverType type, DriverLocation location) throws MalformedURLException {
        switch (location) {
            case LOCAL:
                driver = getLocalDriver(type);
                break;
            case REMOTE:
                driver = getRemoteDriver(type);
                break;
            case BROWSERSTACK:
            	driver = getBrowserStack();break;
            default:
                throw new IllegalArgumentException("unknown selenium server location argument");
        }

      //  setWebDriver(driver);
        return driver;//getDriverThread());
    }

    private static WebDriver getLocalDriver(DriverType type) throws MalformedURLException {
        WebDriver driver;
        switch (type) {
            case CHROME:
                driver = getChromeDriver();
                break;
            case FIREFOX:
                driver = getFirefoxDriver();
                break;
            case SAFARI:
                driver = getSafariDriver();
                break;
            case IE:
                driver = getInternetExplorerDriver();
                break;
            case MOBILE:
                driver = getMobileDriver();
                break;
            default:
                throw new IllegalArgumentException("unknown browser argument");
        }
        return driver;
    }

    private static WebDriver getRemoteDriver(DriverType type) throws MalformedURLException {
        String seleniumHubAddress = System.getProperty("vm.IP");

        DesiredCapabilities desiredCapabilities;
        switch (type) {
            case CHROME:
                desiredCapabilities = DesiredCapabilities.chrome();
                break;
            case FIREFOX:
                desiredCapabilities = DesiredCapabilities.firefox();
                break;
            case SAFARI:
                desiredCapabilities = DesiredCapabilities.safari();
                break;
            case IE:
                desiredCapabilities = DesiredCapabilities.internetExplorer();
                break;
            default:
                throw new IllegalArgumentException("unknown selenium browser argument");
        }

        desiredCapabilities.setJavascriptEnabled(true);

        return new RemoteWebDriver(new URL(seleniumHubAddress), desiredCapabilities);
    }

	@SuppressWarnings("deprecation")
	private static WebDriver getChromeDriver() {
       System.getProperty("driverpath");
        String localMachineEnvironment = System.getProperty("os.name");
        if (localMachineEnvironment.toLowerCase().trim().contains("mac")) {
            System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver");
        } else if (driverPath.endsWith(".exe") || driverPath.endsWith("chromedriver")) {
            System.setProperty("webdriver.chrome.driver", driverPath);
        } else {
            System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver.exe");
        }
        ChromeOptions options = new ChromeOptions();
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        cap.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        cap.setCapability(ChromeOptions.CAPABILITY, options);
        return new ChromeDriver(cap);
    }

    private static WebDriver getInternetExplorerDriver() {
        if (!driverPath.endsWith(".exe")) {
            driverPath = driverPath + "IEDriverServer.exe";
        }
        System.setProperty("webdriver.ie.driver", driverPath);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        capabilities.setCapability("ignoreZoomSetting", true);
        return new InternetExplorerDriver(capabilities);
    }

    private static WebDriver getSafariDriver() {
        return new SafariDriver();
    }

    private static WebDriver getFirefoxDriver() {
   //	System.setProperty("webdriver.firefox.bin", "C:\\Users\\ayushgaur\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
        System.setProperty("webdriver.gecko.driver", driverPath + "geckodriver.exe");
    	FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.cache.disk.enable", false);
        return new FirefoxDriver();
    }

    private static WebDriver getMobileDriver() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("deviceName", System.getProperty("mobileDeviceName", "094c9e7f"));
        capabilities.setCapability("device", System.getProperty("mobileDeviceType", "Android"));
        capabilities.setCapability("platformName", System.getProperty("mobileDeviceType", "Android"));
        capabilities.setCapability("app", System.getProperty("mobileBrowser", "Chrome"));
        capabilities.setCapability(CapabilityType.VERSION, "5.0.2");
        capabilities.setCapability(CapabilityType.PLATFORM, "Windows");
        capabilities.setJavascriptEnabled(true);

        return new RemoteWebDriver(new URL(System.getProperty("appiumServer")), capabilities);
    }

    private static WebDriver configureBrowser(WebDriver driver) throws MalformedURLException {
        Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
        String browserVersion = caps.getVersion();
        String uAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;");
        System.setProperty("userAgent", uAgent);
        System.setProperty("browserVersion", browserVersion);
        System.out.println("Browser Version :::: " + browserVersion);
        if (isNotMobile() && isLocal()) {
            maximizeScreen(driver);
        } else if (!isLocal()) {
            driver.manage().window().maximize(); //TODO: figure out actual logic here? this is probably some combo of
            // browser and OS
        }
        int webDriverTimeout = Integer.parseInt("30");
        driver.manage().timeouts().implicitlyWait(webDriverTimeout, TimeUnit.SECONDS);

        return getDriverThread();//driver;
    }

    private static boolean isNotMobile() {
        return !System.getProperty("browser").equalsIgnoreCase("mobile");
    }

    private static boolean isLocal() {
        return System.getProperty("server").equals("local");
    }

    private static void maximizeScreen(WebDriver driver) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        org.openqa.selenium.Point point = new org.openqa.selenium.Point(0, 0);
        driver.manage().window().setPosition(point);
        org.openqa.selenium.Dimension maximizedScreenSize = new org.openqa.selenium.Dimension((int) screenSize
                .getWidth(), (int) screenSize.getHeight());
        driver.manage().window().setSize(maximizedScreenSize);
    }
    
    public static WebDriver getBrowserStack(){
    	 DesiredCapabilities caps = new DesiredCapabilities();
    	 caps = DesiredCapabilities.chrome();
    	 caps.setCapability("browser", System.getProperty("browser","ie"));
    	 caps.setCapability("browser_version",System.getProperty("browser_version","56"));
    	 caps.setCapability("os", System.getProperty("os","WINDOWS"));
    	 caps.setCapability("os_version",System.getProperty("os_version","8.1"));
    	 caps.setCapability("resolution", System.getProperty("resolution", "1920x1080"));
    	 caps.setCapability("build", System.getProperty("tier")+"_"+System.getProperty("browser")+":	"+System.getProperty("scenario")+"_"+System.getProperty("cucumber.options").split(" ")[3]+"_"+System.currentTimeMillis());
    	 caps.setCapability("project", System.getProperty( "project","Devmath"));
    	 caps.setCapability("browserstack.console", "verbose");
    	 caps.setCapability("browserstack.networkLogs", "true");
    	 caps.setCapability("browserstack.debug", "true");
//    	 caps.setCapability("browserstack.local", "true");
//    	 caps.setCapability("browserstack.localIdentifier",System.getenv("BROWSERSTACK_LOCAL_IDENTIFIER"));//"Test123");

    	    try {
				return new RemoteWebDriver(new URL(System.getProperty("vm.IP","https://mukulmonga1:bJXA7wjt5sbgzDSBszPQ@hub-cloud.browserstack.com/wd/hub")), caps);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
    }
}
