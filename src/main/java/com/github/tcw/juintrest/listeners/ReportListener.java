package com.github.tcw.juintrest.listeners;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.util.concurrent.TimeUnit;

public class ReportListener implements TestExecutionListener {

    private StringBuilder report = new StringBuilder();

    long startTime = 0;

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {

    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {

    }

    @Override
    public void dynamicTestRegistered(TestIdentifier testIdentifier) {

    }

    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {

    }

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        startTime = System.nanoTime();
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        report.append(testIdentifier.getDisplayName()).append("\n");
        report.append(testExecutionResult.getStatus().name()).append("\n");
        report.append(testIdentifier.getSource().toString()).append("\n");
        report.append("Time:").append(TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - startTime));

    }

    @Override
    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
        report.append(entry.toString()).append("\n");

    }

    public String getReport() {
        return report.toString();
    }
}
