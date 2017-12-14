package scripts;

//import java.awt.Robot;
//import java.awt.Toolkit;
//import java.awt.Window;
//import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
//import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
//import org.openqa.selenium.interactions.Action;
//import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.remote.server.handler.SendKeys;

import scripts.util.Constants;
import scripts.util.PasswordEncryption;
import scripts.util.Results;
import scripts.util.StopWatch;
import scripts.util.TestUtils;

public class BasicTest {
	
	WebDriver driver;

	Results resultsObjInstance = null;
	
	private String applicationURL = "";
	
	private TestUtils utils = new TestUtils();
	
	private static Logger logger = Logger.getLogger(BasicTest.class);
	
	private long sessionId;
	
	/**
	 * Validates the devops-web application for availability of elements
	 * @throws InterruptedException
	 */
	public void testDevopsWeb() throws InterruptedException {
		String message = "FAIL";		
		WebElement validElement = null;
		ArrayList<String> appURLs = new ArrayList<String>();
		appURLs.add(applicationURL);

		for(int i = 0; i < appURLs.size(); i++) {
			StopWatch timer = new StopWatch();
			driver.get(appURLs.get(i));
			try {
				validElement = verifyElementEnabled(Constants.ELEMENT_XPATH_STRING, "/html/body/div[1]/div/div[1]/h2");

				if(validElement != null) {
					if(validElement.getText().equals("Hello !! Welcome To DevOps !!")) {
						validElement = verifyElementEnabled(Constants.ELEMENT_XPATH_STRING, "/html/body/footer/div/p/a");
						if(validElement.getText().equals("Sudheer Veeravalli")) {
                            message = "PASS";
						}
					}
				}	
				logger.info("==>>" + sessionId + "<<==" + "Finished testDevopsWeb(" + appURLs.get(i) 
						+ ") in " + timer.elapsedTime() + " seconds with status: " + message);
				resultsObjInstance.addTestResult("testDevopsWeb", message, new Double(timer.elapsedTime()).intValue(), "");
			} catch (NoSuchElementException nsee) {
				logger.info("==>>" + sessionId + "<<==" + "Finished testDevopsWeb(" + appURLs.get(i) 
						+ ") in " + timer.elapsedTime() + " seconds with status: " + message);
				String errorMsg = nsee.getMessage();			
				logger.info("==>>" + sessionId + "<<==" + "testDevopsWeb(): Element Not Found => " 
						+ errorMsg.substring(errorMsg.indexOf("Unable"),errorMsg.indexOf("Unable") + 100));
				resultsObjInstance.addTestResult("testDevopsWeb", message, new Double(timer.elapsedTime()).intValue(), errorMsg.substring(0, 100));
			}
		}
	}

	/**
	 * Validates the accessibility of Google Search and GitHub pages
	 * @throws InterruptedException
	 */
	public void testGoogleSearch() throws InterruptedException {
		StopWatch timer = new StopWatch();
		String message = "FAIL";		
		driver.get("https://google.co.in");
		WebElement validElement = null;
		try {
			validElement = verifyElementEnabled(Constants.ELEMENT_XPATH_STRING, "//*[@id=\"lst-ib\"]");
			if(validElement != null) {
				validElement.sendKeys("What is DevOps?");
				validElement.sendKeys(Keys.RETURN);			
				Thread.sleep(Constants.DRIVER_WAIT_FOR_VIEWING); // wait to view the results
				validElement = verifyElementEnabled(Constants.ELEMENT_LINKTEXT_STRING, "DevOps - Wikipedia");				
				if (verifyLinkEnabled(validElement)) {
					validElement.click();					
					Thread.sleep(Constants.DRIVER_WAIT_FOR_VIEWING);
					message = "PASS";
				}
			}	
			logger.info("==>>" + sessionId + "<<==" + "Finished testGoogleSearch in " 
					+ timer.elapsedTime() + " seconds with status: " + message);
			resultsObjInstance.addTestResult("testGoogleSearch", message, new Double(timer.elapsedTime()).intValue(), "");
		} catch (NoSuchElementException nsee) {
			logger.info("==>>" + sessionId + "<<==" + "Finished testGoogleSearch in " 
					+ timer.elapsedTime() + " seconds with status: " + message);
			String errorMsg = nsee.getMessage();			
			logger.info("==>>" + sessionId + "<<==" + "testGoogleSearch(): Element Not Found => " 
					+ errorMsg.substring(errorMsg.indexOf("Unable"),errorMsg.indexOf("Unable") + 100));
			resultsObjInstance.addTestResult("testGoogleSearch", message, new Double(timer.elapsedTime()).intValue(), errorMsg.substring(0, 100));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reflection method used to invoke appropriate test case
	 * @param object
	 * @param method
	 * @throws Exception
	 */
	public void invokeMethod(Object object, Method method) throws Exception {    
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource("log4j.properties");
		PropertyConfigurator.configure(url);
        method.invoke(object);
    }
	
	/**
	 * Loads Chrome driver for input machine
	 * @param machine
	 * @throws IOException
	 */
	public void loadChromeDriver(String machine) throws IOException {
		
		ClassLoader classLoader = getClass().getClassLoader();
	    URL resource = null;
	    
	    String tDir = System.getProperty("java.io.tmpdir"); 
	    
	    if(machine != null && machine.equals(Constants.MACHINE_WINDOWS)) 
	    	resource = classLoader.getResource("resources/drivers/" + machine.toLowerCase() + "_64/chromedriver.exe");
	    else if(machine != null && machine.equals(Constants.MACHINE_LINUX)) {
	    	resource = classLoader.getResource("resources/drivers/" + machine.toLowerCase() + "_64/chromedriver");
	    	tDir = tDir + "/";
	    }
	    
	    // Copying the executable to a temp directory and executing from there
	    String path = tDir + "tmp-chromedriver" ; 
	    
	    File chromeDriver = new File(path);	    
	    chromeDriver.deleteOnExit(); 
	    FileUtils.copyURLToFile(resource, chromeDriver);
	    System.out.println("Driver Details=" + chromeDriver.exists() + " && path = " + chromeDriver.getPath() + " && size = " + chromeDriver.getTotalSpace());
		
	    //File chromeDriver = new File(resource.getFile());
	    chromeDriver.setExecutable(true);
	    System.setProperty("webdriver.chrome.driver", chromeDriver.getAbsolutePath());
	    
	    ChromeOptions options = new ChromeOptions();
	    options.addArguments("--incognito");
	    options.addArguments("test-type");
	    options.addArguments("start-maximized");
	    options.addArguments("--enable-automation");
	    options.addArguments("test-type=browser");
	    options.addArguments("disable-infobars");
	    options.addArguments("--no-sandbox");
	    
	    DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
	    /*if(enableProxy) 
	    	capabilities.setCapability(CapabilityType.PROXY, new Proxy().setHttpProxy(Constants.NETWORK_PROXY));*/
	    driver = new ChromeDriver(capabilities);
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    
	    // Use below code when testing on mobile device like android. This code helps to dismiss the default setup screens. 
	    // Change driver to AndroidDriver if needed
	    
	    /*String context = driver.getContext(); // = "CHROMIUM"
        driver.context("NATIVE_APP");
        driver.findElement(By.id("com.android.chrome:id/terms_accept")).click();
        driver.findElement(By.id("com.android.chrome:id/negative_button")).click();
        driver.context(context);*/
	}
	
	/**
	 * Loads Firefox driver for input machine
	 * @param machine
	 * @throws IOException
	 */
	public void loadFirefoxDriver(String machine) throws IOException {
		URL resource = null;
		ClassLoader classLoader = getClass().getClassLoader();
		
		String tDir = System.getProperty("java.io.tmpdir"); 
		
		if (machine != null && machine.equals(Constants.MACHINE_WINDOWS))
		    resource = classLoader.getResource("resources/drivers/" + machine.toLowerCase() + "_64/geckodriver.exe");
		else if(machine != null && machine.equals(Constants.MACHINE_LINUX)) {
			resource = classLoader.getResource("resources/drivers/" + machine.toLowerCase() + "_64/geckodriver");
			tDir = tDir + "/";
		}
		
		// Copying the executable to a temp directory and executing from there
	    String path = tDir + "tmp-geckodriver" ; 
	    
	    File fireFoxDriver = new File(path);	    
	    fireFoxDriver.deleteOnExit(); 
	    FileUtils.copyURLToFile(resource, fireFoxDriver);
	    fireFoxDriver.setExecutable(true);
	    System.out.println("Driver Details=" + fireFoxDriver.exists() + " && path = " + fireFoxDriver.getPath() + " && size = " + fireFoxDriver.getTotalSpace());
	    System.setProperty("webdriver.gecko.driver", fireFoxDriver.getAbsolutePath());
	    
	    path = tDir + "firebug-2.0.19.xpi" ; 
	    File firebugPlugin = new File(path);	    
	    firebugPlugin.deleteOnExit(); 
	    URL pluginResource = classLoader.getResource("resources/drivers/firebug-2.0.19.xpi");
	    System.out.println("plugin resource = " + pluginResource.getPath());
	    FileUtils.copyURLToFile(pluginResource, firebugPlugin);
	    firebugPlugin.setExecutable(true);   
	    System.out.println("Plugin details=" + firebugPlugin.exists() + " && path = " + firebugPlugin.getPath() + " && size = " + firebugPlugin.getTotalSpace());
	    
		
		ProfilesIni profile = new ProfilesIni();
		FirefoxProfile firefoxProfile = profile.getProfile("default");
		if(firefoxProfile == null) // if default profile is not found - create a new profile
			firefoxProfile = new FirefoxProfile();
	    firefoxProfile.addExtension(firebugPlugin);
	    firefoxProfile.setPreference("extensions.firebug.currentVersion", "2.0.19"); // Avoid startup screen
	    firefoxProfile.setPreference("browser.privatebrowsing.autostart", true);
	    /*
	    if(enableProxy) {
	    	firefoxProfile.setPreference("network.proxy.type", 1);	    
	    	firefoxProfile.setPreference("network.proxy.http", Constants.NETWORK_PROXY_URL);
	    	firefoxProfile.setPreference("network.proxy.http_port", Constants.NETWORK_PROXY_PORT);
	    	firefoxProfile.setPreference("network.proxy.https", Constants.NETWORK_PROXY_URL);
	    	firefoxProfile.setPreference("network.proxy.https_port", Constants.NETWORK_PROXY_PORT);
	    } 
		*/
	    driver = new FirefoxDriver(firefoxProfile);
	    
	    driver.manage().window().maximize();
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	/**
	 * Closes the driver and kills any hanging processes after completion of test cases
	 * @param browser
	 */
	public void closeDriver(String browser) {
		driver.get("about:config");
		driver.quit();
		try {			
			Runtime rt = Runtime.getRuntime();
		    if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
		    	System.out.println("Killing driver process for windows");
		    	if(browser != null && browser.equals(Constants.BROWSER_CHROME)) {
		    		rt.exec("taskkill " + "tmp-chromedriver");
		    		System.out.println("Killed chromedriver.exe");
		    	} else if(browser != null && browser.equals(Constants.BROWSER_FIREFOX)) {
		    		rt.exec("taskkill " + "tmp-geckodriver");
		    		System.out.println("Killed geckodriver.exe");
		    	}
		    } else {
		    	System.out.println("Killing driver process for linux");
		    	if(browser != null && browser.equals(Constants.BROWSER_CHROME)) {
		    		rt.exec("kill -9 " + "tmp-chromedriver");
		    		System.out.println("Killed chromedriver");
		    	} else if(browser != null && browser.equals(Constants.BROWSER_FIREFOX)) {
		    		rt.exec("kill -9 " + "tmp-geckodriver");
		    		System.out.println("Killed geckodriver");
		    	}
		    		
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		// Delete all driver files		
		try {
			FileUtils.deleteDirectory(new File("Driver"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private WebElement verifyElementEnabled(String verifyBy, String verifyString) {
		// Default one that will not scroll to the element
		WebElement element = verifyElementEnabled(verifyBy, verifyString, true);
		return element;
	}
	
	/**
	 * Verifies if the WebElement is available and active at the input xpath and highlights it
	 * @param verifyBy
	 * @param verifyString
	 * @param scrollToElement
	 * @return
	 */
	private WebElement verifyElementEnabled(String verifyBy, String verifyString, boolean scrollToElement) {				
		WebElement element = null;
		switch (verifyBy) {
			case "xpath": 
				element = driver.findElement(By.xpath(verifyString));
				break;
			case "id":
				element = driver.findElement(By.id(verifyString));
				break;
			case "cssSelector":
				element = driver.findElement(By.cssSelector(verifyString));
				break;
			case "linkText":
				element = driver.findElement(By.linkText(verifyString));
				break;
			default:
				break;
		}
		if (element != null) {
			if (element.isDisplayed() && element.isEnabled()) {
				try {
			        JavascriptExecutor jse = (JavascriptExecutor) driver;
			        if(scrollToElement) 
			        	jse.executeScript("arguments[0].scrollIntoView(true);", element);
			        jse.executeScript("arguments[0].style.border='2px solid red'", element);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return element;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * Verifies if the WebElement is available and active - used only to validate links
	 * @param element
	 * @return
	 */
	private boolean verifyLinkEnabled(WebElement element) {
		boolean available = false;
		if (element.isDisplayed() && element.isEnabled()) {
			available = true;
		}
		return available;
	}

	public long getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public void setResultsObjInstance(Results instance) {
		this.resultsObjInstance = instance;
	}
	
	public String getApplicationURL() {
		return applicationURL;
	}

	public void setApplicationURL(String applicationURL) {
		this.applicationURL = applicationURL;
	}	
}
