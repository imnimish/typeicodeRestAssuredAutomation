package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;  // Initialize the retry count
    private final int maxRetryCount = 3;  // Set the maximum number of retries

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;  // Increment retry count
            return true;  // Indicate that the test should be retried
        }
        return false;  // No retries if maxRetryCount is reached
    }
}