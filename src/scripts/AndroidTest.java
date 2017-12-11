package scripts;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import scripts.util.Constants;
import scripts.util.TestUtils;

public class AndroidTest {
	
	TestUtils testUtils = new TestUtils();

	@Test
	public void testAndroid() throws MalformedURLException {
		try {
			URL url = new URL("http://127.0.0.1:4723/wd/hub");
			
			DesiredCapabilities capabilities = DesiredCapabilities.android();
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
			capabilities.setCapability(MobileCapabilityType.PLATFORM, Platform.ANDROID);
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
			capabilities.setCapability(MobileCapabilityType.VERSION, ">=7.1.1");
			/*capabilities.setCapability(MobileCapabilityType.PROXY,
			new Proxy().setHttpProxy("http://proxy-tvm.quest-global.com:8080"));*/
			
			boolean testNativeApp = false;
			
			if (testNativeApp) { // To test a native/hybrid app
				
				// Install and launch app
				File appPackage = new File("C:\\Users\\1017141\\Downloads\\appium-chrome-drivers\\MyApp.apk");
				capabilities.setCapability("app", appPackage.getAbsolutePath());
				capabilities.setCapability("appPackage", "io.sudheer.android.helloandroid");
				capabilities.setCapability("appActivity","io.sudheer.android.helloandroid.HomePageActivity");
				AndroidDriver driverAndroid = new AndroidDriver(url, capabilities);
				driverAndroid.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				System.out.println("MC session was successfully created [Android Device]");
				
				Thread.sleep(Constants.DRIVER_WAIT_FOR_REFRESH);
				System.out.println("Finished Wait");
				
				// Uninstall the app after testing
				driverAndroid.removeApp("io.sudheer.android.helloandroid");
				driverAndroid.quit();
				
			} else { // To test url over chrome browser		
				
				capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);
				
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--incognito");
				//options.addArguments("test-type");
				options.addArguments("start-maximized");
				options.addArguments("--enable-automation");
				options.addArguments("test-type=browser");
				options.addArguments("disable-infobars");
				options.addArguments("--no-sandbox");
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				
				
				
				WebDriver driver = new AndroidDriver(url, capabilities);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				
				// Local URLs to validate
				driver.navigate().to("http://10.0.2.2:8090/examples/");
				Thread.sleep(Constants.DRIVER_WAIT_FOR_VIEWING); // wait to view the page
				driver.get("http://10.0.2.2:8090/examples/servlets/");
				Thread.sleep(Constants.DRIVER_WAIT_FOR_VIEWING); // wait to view the page				
				//driver.get("http://10.0.2.2:8090/devops-web/");
				//Thread.sleep(Constants.DRIVER_WAIT_FOR_VIEWING); // wait to view the page
				
				// Remote URLs to validate
				// driver.get("https://predix-static-app.run.aws-usw02-pr.ice.predix.io/index.html");
				// driver.get("http://d1680/webui/");
				
				// zooming
				testUtils.zoomBrowserContents(driver, 30);
				Thread.sleep(Constants.DRIVER_WAIT_FOR_VIEWING);
				testUtils.zoomBrowserContents(driver, 100);
				Thread.sleep(Constants.DRIVER_WAIT_FOR_VIEWING);
								
				driver.quit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
