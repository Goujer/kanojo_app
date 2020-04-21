package twitter4j.management;

public class InvocationStatisticsCalculator implements InvocationStatistics {
    private long callCount;
    private long errorCount;
    private int index;
    private String name;
    private long[] times;
    private long totalTime;

    public InvocationStatisticsCalculator(String name2, int historySize) {
        this.name = name2;
        this.times = new long[historySize];
    }

    /* access modifiers changed from: package-private */
    public void increment(long time, boolean success) {
        long j = 1;
        this.callCount++;
        long j2 = this.errorCount;
        if (success) {
            j = 0;
        }
        this.errorCount = j + j2;
        this.totalTime += time;
        this.times[this.index] = time;
        int i = this.index + 1;
        this.index = i;
        if (i >= this.times.length) {
            this.index = 0;
        }
    }

    public String getName() {
        return this.name;
    }

    public long getCallCount() {
        return this.callCount;
    }

    public long getErrorCount() {
        return this.errorCount;
    }

    public long getTotalTime() {
        return this.totalTime;
    }

    public synchronized long getAverageTime() {
        long j;
        int stopIndex = Math.min(Math.abs((int) this.callCount), this.times.length);
        if (stopIndex == 0) {
            j = 0;
        } else {
            long totalTime2 = 0;
            for (int i = 0; i < stopIndex; i++) {
                totalTime2 += this.times[i];
            }
            j = totalTime2 / ((long) stopIndex);
        }
        return j;
    }

    public synchronized void reset() {
        this.callCount = 0;
        this.errorCount = 0;
        this.totalTime = 0;
        this.times = new long[this.times.length];
        this.index = 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("calls=").append(getCallCount()).append(",").append("errors=").append(getErrorCount()).append(",").append("totalTime=").append(getTotalTime()).append(",").append("avgTime=").append(getAverageTime());
        return sb.toString();
    }
}
