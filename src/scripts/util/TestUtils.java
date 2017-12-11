package scripts.util;

import org.openqa.selenium.By;
//import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class TestUtils {
	
	/**
	 * Flashes the web element for visibility
	 * @param element
	 * @param driver
	 */
	public void flash(WebElement element, WebDriver driver) {
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		String bgcolor  = element.getCssValue("backgroundColor");
		for (int i = 0; i < 2; i++) {
			changeColor("rgb(300,0,0)", element, js);
			changeColor(bgcolor, element, js);
		}
	}
	
	/**
	 * Changes the background color of the element
	 * @param color
	 * @param element
	 * @param js
	 */
	public void changeColor(String color, WebElement element,  JavascriptExecutor js) {
		js.executeScript("arguments[0].style.backgroundColor = '"+color+"'",  element);

		try {
			Thread.sleep(60);
		}  catch (InterruptedException e) {
		}
	}
	
	/**
	 * Draws a border around the element
	 * @param driver
	 * @param xpath
	 */
	public void drawBorder(WebDriver driver, String xpath){
		try {
	        WebElement element_node = driver.findElement(By.xpath(xpath));
	        JavascriptExecutor jse = (JavascriptExecutor) driver;
	        jse.executeScript("arguments[0].style.border='3px solid red'", element_node);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * Zoom to the required percentage - applies only to WebDriver
	 * @param driver
	 * @param zoomPercentage
	 * @throws InterruptedException
	 */
	public void zoomBrowserContents(WebDriver driver, int zoomPercentage) throws InterruptedException {	
		// TODO: Robot may be used at times - may be works only with firefox - have to test
		/*
		for(int i=0; i<3; i++){
			System.out.println("Calling zoom out");
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_DOWN);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyRelease(KeyEvent.VK_DOWN);
		}
		Thread.sleep(Constants.DRIVER_WAIT_FOR_VIEWING);
		for(int i=0; i<3; i++){
			System.out.println("Calling zoom in");
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_UP);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyRelease(KeyEvent.VK_UP);
		}
		Thread.sleep(Constants.DRIVER_WAIT_FOR_VIEWING);
		*/
				
		System.out.println("Zooming to " + zoomPercentage + " %");
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("document.body.style.zoom = '" + zoomPercentage +"%';");
	}
	
	/**
	 * Pinch in and out of the browser on a native app - to be worked on
	 * @param driver
	 * @throws InterruptedException
	 */
	public void nativeAppTouchEvents(AndroidDriver driver) throws InterruptedException { 
		// TODO : Needs to work on this
		TouchAction touch = new TouchAction(driver).press(100,100).release();
		touch.perform();
		TouchAction swipeDown = new TouchAction(driver).press(100,100);
		swipeDown.perform();
		driver.zoom(100,100);
	}
	
	/**
	 * Accepts the default google acceptance page and say's NoThanks to the sign-in page - to be used when needed
	 * Applicable only for chrome browser
	 * @param driver
	 * @throws InterruptedException
	 */
	public void acceptChromeTerms(AndroidDriver driver) throws InterruptedException {		
		String context = driver.getContext();
		driver.context("NATIVE_APP");
		if(driver.findElement(By.id("com.android.chrome:id/terms_accept")).isDisplayed()) {
			driver.findElement(By.id("com.android.chrome:id/terms_accept")).click();
			if(driver.findElement(By.id("com.android.chrome:id/negative_button")).isDisplayed())
			driver.findElement(By.id("com.android.chrome:id/negative_button")).click();
		}
		driver.context(context);	
	}
}
