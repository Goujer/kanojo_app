package twitter4j.management;

import java.util.HashMap;
import java.util.Map;

public class APIStatistics implements APIStatisticsMBean {
    private final InvocationStatisticsCalculator API_STATS_CALCULATOR;
    private final int HISTORY_SIZE;
    private final Map<String, InvocationStatisticsCalculator> METHOD_STATS_MAP = new HashMap(100);

    public APIStatistics(int historySize) {
        this.API_STATS_CALCULATOR = new InvocationStatisticsCalculator("API", historySize);
        this.HISTORY_SIZE = historySize;
    }

    public synchronized void methodCalled(String method, long time, boolean success) {
        getMethodStatistics(method).increment(time, success);
        this.API_STATS_CALCULATOR.increment(time, success);
    }

    private synchronized InvocationStatisticsCalculator getMethodStatistics(String method) {
        InvocationStatisticsCalculator methodStats;
        methodStats = this.METHOD_STATS_MAP.get(method);
        if (methodStats == null) {
            methodStats = new InvocationStatisticsCalculator(method, this.HISTORY_SIZE);
            this.METHOD_STATS_MAP.put(method, methodStats);
        }
        return methodStats;
    }

    public synchronized Iterable<? extends InvocationStatistics> getInvocationStatistics() {
        return this.METHOD_STATS_MAP.values();
    }

    public synchronized void reset() {
        this.API_STATS_CALCULATOR.reset();
        this.METHOD_STATS_MAP.clear();
    }

    public String getName() {
        return this.API_STATS_CALCULATOR.getName();
    }

    public long getCallCount() {
        return this.API_STATS_CALCULATOR.getCallCount();
    }

    public long getErrorCount() {
        return this.API_STATS_CALCULATOR.getErrorCount();
    }

    public long getTotalTime() {
        return this.API_STATS_CALCULATOR.getTotalTime();
    }

    public long getAverageTime() {
        return this.API_STATS_CALCULATOR.getAverageTime();
    }

    public synchronized Map<String, String> getMethodLevelSummariesAsString() {
        Map<String, String> summariesMap;
        summariesMap = new HashMap<>();
        for (InvocationStatisticsCalculator methodStats : this.METHOD_STATS_MAP.values()) {
            summariesMap.put(methodStats.getName(), methodStats.toString());
        }
        return summariesMap;
    }

    public synchronized String getMethodLevelSummary(String methodName) {
        return this.METHOD_STATS_MAP.get(methodName).toString();
    }
}
