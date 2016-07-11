package uk.co.rapidware.sendence;

import java.util.concurrent.TimeUnit;

/**
 * Simple wrapper class to keep the concept of duration coherent and allow APIs to be leaner.
 */
public class Duration {

    private final long durationAmount_;
    private final TimeUnit durationUnit_;

    public Duration(final long durationAmount, final TimeUnit durationUnit) {
        durationAmount_ = durationAmount;
        durationUnit_ = durationUnit;
    }

    public long getDurationAmount() {
        return durationAmount_;
    }

    public TimeUnit getDurationUnit() {
        return durationUnit_;
    }

    /**
     * @return the duration as milleseconds
     */
    public long getInMillis() {
        return getDurationUnit().toMillis(getDurationAmount());
    }

    /**
     * Static factory method to provide less cumbersome instantiation syntax.
     *
     * @param durationAmount duration in milliseconds
     *
     * @return a newly created <code>Duration</code> instance.
     */
    public static Duration forMs(final long durationAmount) {
        return new Duration(durationAmount, TimeUnit.MILLISECONDS);
    }
}
