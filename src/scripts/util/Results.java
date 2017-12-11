package scripts.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Results {

	private static Results instance = null;
	private static String machine = null;
	private static String browser = null;

	private Results(String machine, String browser) {
		this.machine = machine;
		this.browser = browser;
	}

	public static Results getInstance(String machine, String browser) {

		// Lazy Loading - singleton instance created only when required
		if (instance == null) {
			instance = new Results(machine, browser);
		}

		return instance;
	}

	private List<ResultPOJO> allTestResults = new ArrayList<ResultPOJO>();

	/**
	 * Add results
	 * @param testCaseName
	 * @param testResult
	 * @param durationInSec
	 * @param testFailReason
	 */
	public void addTestResult(String testCaseName, String testResult, int durationInSec, String testFailReason) {
		ResultPOJO resultPOJO = new ResultPOJO();
		resultPOJO.setTestCaseName(testCaseName);
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss z");
		String strDate= formatter.format(date);
		resultPOJO.setRunDTTM(strDate);
		resultPOJO.setTestResult(testResult);
		resultPOJO.setDurationMinDotSec(durationInSec);
		if (testResult.equalsIgnoreCase("PASS")) {
			resultPOJO.setTestFailReason("");
		} else {
			resultPOJO.setTestFailReason(testFailReason);
		}
		allTestResults.add(resultPOJO);
	}

	/**
	 * Publish Test execution results to HTML file
	 */
	public void publishResults() {

		// header content with js and css files
		String htmlHeaders = "<html>\n"
				+ "<head>\n"
				+ "<title>Test Results</title>\n"
				+ "<link href=\"css/style.css\" rel=\"stylesheet\">\n"
				+ "<script src=\"js/filter.js\"></script>\n"
			        + "<script src=\"js/sort.js\"></script>\n"
				+ "</head>\n"
				+ "<body>\n"
				+ "<input type=\"text\" id=\"myInput\" onkeyup=\"myFunction(event)\" placeholder=\"Search for test case name..\">\n"
				+ "<table id=\"myTable\" border ='1'>\n"
				+ "<tr class=\"header\">\n"
				+ "<th style=\"width:5%;\" onclick=\"sortTable(0)\">Test Case #</th>\n"
				+ "<th style=\"width:20%;\" onclick=\"sortTable(1)\">Test Case Name</th>\n"
				+ "<th style=\"width:5%;\" onclick=\"sortTable(2)\">Machine</th>\n"
				+ "<th style=\"width:5%;\" onclick=\"sortTable(3)\">Browser</th>\n"
				+ "<th style=\"width:10%;\" onclick=\"sortTable(4)\">Run Date Time</th>\n"
				+ "<th style=\"width:5%;\" onclick=\"sortTable(5)\">Run Duration (HH:MM:SS)</th>\n"
				+ "<th style=\"width:10%;\" onclick=\"sortTable(6)\">Test Result</th>\n"
				+ "<th style=\"width:40%;\" onclick=\"sortTable(7)\">Test Failure Reason</th>\n"
				+ "</tr>\n";

		// body with data
		StringBuffer htmlContent = new StringBuffer();
		htmlContent.append(htmlHeaders);

		for (int i = 0; i < allTestResults.size(); i++) {
			int tcNumber = i + 1;
			htmlContent = htmlContent.append(
					"<tr>"
							+ "<td>" + tcNumber + "</td>"
							+ "<td>" + allTestResults.get(i).getTestCaseName() + "</td>"
							+ "<td>" + machine + "</td>"
							+ "<td>" + browser + "</td>"
							+ "<td>" + allTestResults.get(i).getRunDTTM() + "</td>"
							+ "<td>" + allTestResults.get(i).getDurationMinDotSec() + "</td>"
							+ "<td>" + allTestResults.get(i).getTestResult() + "</td>"
							+ "<td>" + allTestResults.get(i).getTestFailReason() + "</td>"
							+ "</tr>\n");
		}

		// closing of html elements
		htmlContent.append("</table>\n</body>\n</html>");

		FileOutputStream fileOutputStream = null;
		try {
			String RESULT_FILE_PATH = System.getProperty("user.dir");
			System.out.println("RESULT_FILE_PATH=" + RESULT_FILE_PATH);
			RESULT_FILE_PATH += "/../output/index.html";
			fileOutputStream = new FileOutputStream(RESULT_FILE_PATH);
			fileOutputStream.write(htmlContent.toString().getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
