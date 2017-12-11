package scripts;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import scripts.util.Constants;
import scripts.util.Results;

public class Starter {
	
	private static Logger logger = Logger.getLogger(Starter.class);
	
	private static String machine = Constants.MACHINE_DEFAULT; 
	private static String browser = Constants.BROWSER_DEFAULT; 
	
	private static long sessionId;
	
	public static void main(String[] args) {
		//PropertyConfigurator.configure("log4j.properties");
		// Place log4j.properties file in src directory and load through class loader
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource("log4j.properties");
		PropertyConfigurator.configure(url);
		
		sessionId = System.currentTimeMillis();
		logger.info("==>>" + sessionId + "<<==" + "Started Selenium Automated Testing");
		Starter start = new Starter();
		ArrayList<String> methodNames = new ArrayList<String>();

		methodNames.add("testDevopsWeb");
		//methodNames.add("testGoogleSearch");

		if(args.length == 0) {
			machine = Constants.MACHINE_DEFAULT;
			browser = Constants.BROWSER_DEFAULT;
		} else if(args.length == 2) {
			machine = args[0].toUpperCase();
			browser = args[1].toUpperCase();
		}
		logger.info("==>>" + sessionId + "<<==" + "System=" + machine + "   Browser=" + browser);
		Results resultsObjInstance = Results.getInstance(machine, browser);
		start.runTests(methodNames, resultsObjInstance);
		logger.info("==>>" + sessionId + "<<==" + "Finished Selenium Automated Testing" + "\n======================================");

		resultsObjInstance.publishResults();
	}
	
	public void runTests(ArrayList<String> methodNames, Results resultsObjInstance) {
		
		try {
			BasicTest test = new BasicTest();
			test.setResultsObjInstance(resultsObjInstance);
			test.setSessionId(sessionId);
			Method method;
			try {
				if(browser != null && browser.equals(Constants.BROWSER_CHROME)) {
					test.loadChromeDriver(machine);
				} else if(browser != null && browser.equals(Constants.BROWSER_FIREFOX)) {
					test.loadFirefoxDriver(machine);
				}
				for(int i=0; i < methodNames.size(); i++) {
					method = BasicTest.class.getMethod(methodNames.get(i).toString());
					test.invokeMethod(test, method);	
				}
				test.closeDriver(browser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
