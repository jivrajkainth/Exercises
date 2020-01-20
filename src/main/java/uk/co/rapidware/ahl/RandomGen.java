package uk.co.rapidware.ahl;

import com.google.common.base.Preconditions;
import com.gs.collections.api.FloatIterable;
import com.gs.collections.api.list.primitive.MutableFloatList;
import com.gs.collections.impl.list.mutable.primitive.FloatArrayList;

import java.util.Arrays;
import java.util.Random;

/**
 * Random integer generator utility that must be configured with a set of possible integer values that can be
 * returned and their associated frequencies (via a probability value).  The probabilities provided must sum to 1.
 */
public final class RandomGen {

    // Allow seed to be injected as a parameter as a future enhancement
    private final Random random_ = new Random(1000L);

    private final int[] randomNums_;
    private final float[] probabilities_;
    private final float[] distributions_;

    private RandomGen(
        final int[] randomNums,
        final float[] probabilities,
        final float[] distributions
    ) {
        randomNums_ = randomNums;
        probabilities_ = probabilities;
        distributions_ = distributions;
    }

    /**
     * @return an integer from the set that this instance was configured with
     */
    public int nextNum() {
        final float aFloat = getRandom().nextFloat();

        // When the random number set is very large, binary search provides speed benefits as complexity is logarithmic
        final int index = Arrays.binarySearch(getDistributions(), aFloat);

        final int indexOfRandomNum = index >= 0 ? index : -1 * (index + 1);
        return getRandomNums()[indexOfRandomNum];
    }

    /**
     * Static factory constructor for providing an instance of the utility.  The length of the two arrays must be
     * exactly the same and probabilities supplied must sum to 1.
     *
     * @param numbers       the set of integers from which to generate a random number
     * @param probabilities the associated probabilities that will determine the frequency of occurrence of each integer
     *
     * @return a new instance of <code>RandomGen</code>
     */
    public static RandomGen forNumbersAndProbabilities(final int[] numbers, final float[] probabilities) {
        Preconditions.checkArgument(
            numbers.length == probabilities.length,
            "Count of numbers does no match count of probabilities"
        );

        final ProbabilityDistributionBuilder builder = new ProbabilityDistributionBuilder();
        FloatArrayList.newListWith(probabilities).forEach(builder::addProbability);

        Preconditions.checkArgument(builder.isComplete(), "Probabilities do not sum to 1.");
        return new RandomGen(numbers, probabilities, builder.toDistribution());
    }

    private Random getRandom() {
        return random_;
    }

    private int[] getRandomNums() {
        return randomNums_;
    }

    private float[] getDistributions() {
        return distributions_;
    }

    private static final class ProbabilityDistributionBuilder {

        static final float EPSILON = 0.00000001f;
        private final MutableFloatList distribution_ = new FloatArrayList();

        MutableFloatList getDistribution() {
            return distribution_;
        }

        void addProbability(final float probability) {
            if (getDistribution().isEmpty()) {
                getDistribution().add(probability);
            }
            else {
                final float nextCumulative = getDistribution().getLast() + probability;
                Preconditions.checkArgument((nextCumulative - 1f) < EPSILON, "Distribution has exceeded 1");
                getDistribution().add(nextCumulative);
            }
        }

        boolean isComplete() {
            return Math.abs(getDistribution().getLast() - 1.0f) < EPSILON;
        }

        float[] toDistribution() {
            return getDistribution().toArray();
        }
    }
}
