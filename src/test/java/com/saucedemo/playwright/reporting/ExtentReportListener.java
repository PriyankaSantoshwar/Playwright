package com.saucedemo.playwright.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentReportListener implements ITestListener, ISuiteListener {
    private final ExtentReports extent = new ExtentReports();
    private final Map<ITestResult, ExtentTest> tests = new ConcurrentHashMap<>();

    @Override
    public void onStart(ISuite suite) {
        Path reportPath = Path.of("target", "extent-report.html");
        ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath.toString());
        reporter.config().setDocumentTitle("SauceDemo Automation Results");
        reporter.config().setReportName("Playwright Test Execution");
        extent.attachReporter(reporter);
        extent.setSystemInfo("Framework", "Playwright Java");
        extent.setSystemInfo("Test runner", "TestNG");
        extent.setSystemInfo("Browser", System.getProperty("playwright.browser", "chromium"));
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = "%s.%s".formatted(
                result.getTestClass().getName(),
                result.getMethod().getMethodName());
        tests.put(result, extent.createTest(testName));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testFor(result).pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testFor(result).fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = tests.computeIfAbsent(
                result,
                ignored -> extent.createTest(result.getMethod().getMethodName()));
        if (result.getThrowable() == null) {
            test.skip("Test skipped");
        } else {
            test.skip(result.getThrowable());
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        extent.flush();
    }

    private ExtentTest testFor(ITestResult result) {
        return tests.computeIfAbsent(
                result,
                ignored -> extent.createTest(result.getMethod().getMethodName()));
    }
}
