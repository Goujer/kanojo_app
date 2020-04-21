package twitter4j.management;

import java.util.Map;

public interface APIStatisticsMBean extends InvocationStatistics {
    Iterable<? extends InvocationStatistics> getInvocationStatistics();

    Map<String, String> getMethodLevelSummariesAsString();

    String getMethodLevelSummary(String str);
}
