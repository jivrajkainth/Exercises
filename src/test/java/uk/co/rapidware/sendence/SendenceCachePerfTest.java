package uk.co.rapidware.sendence;

import com.gs.collections.impl.utility.ArrayIterate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * This performance test has been initially developed to measure throughput.  With more time, we need to expand it to
 * also provide a latency distribution so that response times at the 95th and 99th percentile can be determined.
 * </p>
 * The test itself (which is resembles a micro-benchmark) is useful for comparison rather than an absolute measure of
 * performance as that can only be determined in a real system.  To ensure that JVM is warmed-up and code paths are
 * sufficiently optimized/compiled by the JVM, the test is run multiple times.
 */
public class SendenceCachePerfTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendenceCachePerfTest.class);

    public static Logger getLogger() {
        return LOGGER;
    }

    private final int iterationCount_;
    private final SendenceCache sendenceCache_;
    private final String[] keysToRetrieve_;

    public SendenceCachePerfTest(
        final int iterationCount, final SendenceCache sendenceCache, final String[] keysToRetrieve
    ) {
        this.iterationCount_ = iterationCount;
        sendenceCache_ = sendenceCache;
        keysToRetrieve_ = keysToRetrieve;
    }

    public int getIterationCount() {
        return iterationCount_;
    }

    public long test() {
        getLogger().info("Running performance test for [{}] iterations", getIterationCount());

        final long startTime = System.nanoTime();

        for (int count = 0; count < keysToRetrieve_.length; count++) {
            final Object value = sendenceCache_.get(keysToRetrieve_[count]);
            if (null == value) {
                throw new RuntimeException("Something has gone wrong!");
            }
        }
        final long endTime = System.nanoTime();

        return endTime - startTime;
    }

    public static void main(final String[] args) throws Exception {

        // Use a command line arg with default in future.
        final int entryCount = 10_000_000;
        final SendenceCache<String, Object> sendenceCache =
            SendenceCaching.getDefaultCacheManager().createCache(
                "PerfTest", SendenceConfiguration.createFor(String.class, Object.class, entryCount));

        final String[] keysToRetrieve = new String[entryCount];

        for (int index = 0; index < entryCount; index++) {
            final String key = String.valueOf(index);
            final Object value = new Object();
            sendenceCache.put(key, value);
            keysToRetrieve[index] = key;
        }

        final int runCount = 10;
        final String[] resultLines = new String[runCount];
        for (int index = 0; index < runCount; index++) {
            final SendenceCachePerfTest
                perfTestCase = new SendenceCachePerfTest(entryCount, sendenceCache, keysToRetrieve);
            final long durationInNanos = perfTestCase.test();
            final long tput = (perfTestCase.getIterationCount() * 1_000_000_000L) / durationInNanos;
            resultLines[index] = String.format(
                "IterationCount=%,d\tduration=%,dns\treads/sec=%,d",
                perfTestCase.getIterationCount(),
                durationInNanos, tput
            );
        }
        ArrayIterate.forEach(resultLines, SendenceCachePerfTest.getLogger()::info);
    }
}
