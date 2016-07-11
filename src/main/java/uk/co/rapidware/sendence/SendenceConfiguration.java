package uk.co.rapidware.sendence;

/**
 * Simple configuration class to allow for extensibility in future without causing Cache ManagerAPI refactor.
 */
public class SendenceConfiguration<T_Key, T_Value> {

    public static final int DEFAULT_CACHE_SIZE = 1000;
    private final Class<T_Key> keyClass_;
    private final Class<T_Value> valueClass_;
    private final int cacheSize_;

    public SendenceConfiguration(final Class<T_Key> keyClass, final Class<T_Value> valueClass, final int cacheSize) {
        if (null == keyClass) {
            throw new IllegalArgumentException("keyClass must not be null");
        }

        if (null == valueClass) {
            throw new IllegalArgumentException("valueClass must not be null");
        }

        keyClass_ = keyClass;
        valueClass_ = valueClass;
        cacheSize_ = cacheSize;
    }

    public Class<T_Key> getKeyClass() {
        return keyClass_;
    }

    public Class<T_Value> getValueClass() {
        return valueClass_;
    }

    public int getCacheSize() {
        return cacheSize_;
    }

    public static <T_Key, T_Value> SendenceConfiguration<T_Key, T_Value> createFor(
        final Class<T_Key> keyClass, final Class<T_Value> valueClass
    ) {
        return SendenceConfiguration.createFor(keyClass, valueClass, DEFAULT_CACHE_SIZE);
    }

    public static <T_Key, T_Value> SendenceConfiguration<T_Key, T_Value> createFor(
        final Class<T_Key> keyClass, final Class<T_Value> valueClass, final int cacheSize
    ) {
        return new SendenceConfiguration<>(keyClass, valueClass, cacheSize);
    }
}
