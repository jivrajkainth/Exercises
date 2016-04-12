package uk.co.rapidware.ahl;

import com.gs.collections.api.map.primitive.MutableIntObjectMap;
import com.gs.collections.impl.map.mutable.primitive.IntObjectHashMap;
import junit.framework.TestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 */
public class RandomGenTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomGenTest.class);

    public static Logger getLogger() {
        return LOGGER;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayLengthsMismatch() throws Exception {
        final int[] randomNumberSet = {-1, 0, 1, 2, 3};
        final float[] probabilities = {0.01f, 0.3f, 0.58f, 0.1f};

        RandomGen.forNumbersAndProbabilities(randomNumberSet, probabilities);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityDistributionShort() throws Exception {
        final int[] randomNumberSet = {-1, 0, 1, 2, 3};
        final float[] probabilities = {0.01f, 0.3f, 0.58f, 0.1f, 0.001f};

        RandomGen.forNumbersAndProbabilities(randomNumberSet, probabilities);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityDistributionLong() throws Exception {
        final int[] randomNumberSet = {-1, 0, 1, 2, 3};
        final float[] probabilities = {0.01f, 0.3f, 0.58f, 0.1f, 0.1f};

        RandomGen.forNumbersAndProbabilities(randomNumberSet, probabilities);
    }

    @Test
    public void testNextNum() throws Exception {
        final int[] randomNumberSet = {-1, 0, 1, 2, 3};
        final float[] probabilities = {0.01f, 0.3f, 0.58f, 0.1f, 0.01f};
        final int numberOfGenerations = 10000000;

        final RandomGen randomGen = RandomGen.forNumbersAndProbabilities(randomNumberSet, probabilities);

        final MutableIntObjectMap<LongAdder> frequencyCounters = new IntObjectHashMap<>(randomNumberSet.length);

        for (int eachRandomNumber : randomNumberSet) {
            frequencyCounters.put(eachRandomNumber, new LongAdder());
        }

        // Machine has two cores
        final ForkJoinPool defaultPool = new ForkJoinPool(2);

        getLogger().info("ThreadCount [{}]", defaultPool.getParallelism());

        for (int count = 0; count < numberOfGenerations; count++) {
            defaultPool.execute(() -> {
                final int nextNum = randomGen.nextNum();
                frequencyCounters.get(nextNum).increment();
            });
        }
        defaultPool.awaitQuiescence(1, TimeUnit.MINUTES);
        getLogger().info("Random number histogram: {}", frequencyCounters.toString());

        for (int index = 0; index < randomNumberSet.length; index++) {
            final float probability = probabilities[index];
            final LongAdder frequency = frequencyCounters.get(randomNumberSet[index]);

            getLogger().info("Random Int [{}], Expected Probability [{}], Actual Probability [{}]",
                    randomNumberSet[index], probability, frequency.floatValue() / numberOfGenerations);

            final float expectedFrequency = numberOfGenerations * probability;
            final int actualFrequency = frequency.intValue();

            long expectedFrequencyOrderOfMagnitude = Math.round(Math.log10(expectedFrequency));
            long actualFrequencyOrderOfMagnitude = Math.round(Math.log10(actualFrequency));

            TestCase.assertEquals("Expected and Actual frequencies appear to differ by more than one order of " +
                    "magnitude", expectedFrequencyOrderOfMagnitude, actualFrequencyOrderOfMagnitude);
        }
    }
}