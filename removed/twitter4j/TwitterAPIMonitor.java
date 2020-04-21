package twitter4j;

import java.lang.management.ManagementFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.logging.Logger;
import twitter4j.management.APIStatistics;
import twitter4j.management.APIStatisticsMBean;
import twitter4j.management.APIStatisticsOpenMBean;

public class TwitterAPIMonitor {
    private static final TwitterAPIMonitor SINGLETON = new TwitterAPIMonitor();
    private static final APIStatistics STATISTICS = new APIStatistics(100);
    private static final Logger logger = Logger.getLogger(TwitterAPIMonitor.class);
    private static final Pattern pattern = Pattern.compile("https?:\\/\\/[^\\/]+\\/[0-9.]*\\/([a-zA-Z_\\.]*).*");

    static {
        boolean isJDK14orEarlier = false;
        try {
            String versionStr = System.getProperty("java.specification.version");
            if (versionStr != null) {
                isJDK14orEarlier = 1.5d > Double.parseDouble(versionStr);
            }
            if (ConfigurationContext.getInstance().isDalvik()) {
                System.setProperty("http.keepAlive", "false");
            }
            try {
                MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
                if (isJDK14orEarlier) {
                    mbs.registerMBean(STATISTICS, new ObjectName("twitter4j.mbean:type=APIStatistics"));
                } else {
                    mbs.registerMBean(new APIStatisticsOpenMBean(STATISTICS), new ObjectName("twitter4j.mbean:type=APIStatisticsOpenMBean"));
                }
            } catch (InstanceAlreadyExistsException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            } catch (MBeanRegistrationException e2) {
                e2.printStackTrace();
                logger.error(e2.getMessage());
            } catch (NotCompliantMBeanException e3) {
                e3.printStackTrace();
                logger.error(e3.getMessage());
            } catch (MalformedObjectNameException e4) {
                e4.printStackTrace();
                logger.error(e4.getMessage());
            }
        } catch (SecurityException e5) {
            isJDK14orEarlier = true;
        }
    }

    private TwitterAPIMonitor() {
    }

    public static TwitterAPIMonitor getInstance() {
        return SINGLETON;
    }

    public APIStatisticsMBean getStatistics() {
        return STATISTICS;
    }

    /* access modifiers changed from: package-private */
    public void methodCalled(String twitterUrl, long elapsedTime, boolean success) {
        Matcher matcher = pattern.matcher(twitterUrl);
        if (matcher.matches() && matcher.groupCount() > 0) {
            STATISTICS.methodCalled(matcher.group(1), elapsedTime, success);
        }
    }
}
