package uk.co.rapidware.sendence;

import java.util.concurrent.TimeUnit;

/**
 * Created by Jivraj on 10/07/2016.
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

    public long getEpoch() {
        return getDurationUnit().toMillis(getDurationAmount());
    }

    public static Duration forMs(final long durationAmount) {
        return new Duration(durationAmount, TimeUnit.MILLISECONDS);
    }
}
