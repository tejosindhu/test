package scripts.util;

public class ResultPOJO {

	/**
	 * Name of the test case
	 */
	private String testCaseName; 
    
	/**
	 * When the test case is run.. Input Date is converted to String in Setter
	 */
    private String runDTTM;

    /**
	 * Test Result - Pass or Fail
	 */
    private String testResult;
    
    /**
	 * Duration of test case run in minutes.seconds
	 * that is 7.10 = 7 mins 10 secs
	 */
    private String durationMinDotSec;
    
    /**
	 * Test Failure Reason - Message if failure
	 */
    private String testFailReason;

    public String getTestFailReason() {
		return testFailReason;
	}

	public void setTestFailReason(String testFailReason) {
		this.testFailReason = testFailReason;
	}

	public String getDurationMinDotSec() {
        return durationMinDotSec;
    }

    public String getRunDTTM() {
        return runDTTM;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setDurationMinDotSec(int durationInSec) {
        int hours = durationInSec / 3600;
        int secondsLeft = durationInSec - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10)
            formattedTime += "0";
        formattedTime += hours + ":";

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds ;
        this.durationMinDotSec = formattedTime;
    }

    public void setRunDTTM(String runDTTM) {
        this.runDTTM = runDTTM;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }
    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

}
