package uk.co.rapidware.sendence;

import com.gs.collections.impl.map.mutable.UnifiedMap;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;

/**
 */
public class SendenceCacheImpl<T_Key, T_Value> implements SendenceCache<T_Key, T_Value> {

    private final UnifiedMap<T_Key, T_Value> keyValueStore_ = new UnifiedMap<>();
    private final ScheduledExecutorService executorService_;

    public SendenceCacheImpl(final ScheduledExecutorService executorService) {
        executorService_ = executorService;
    }

    public UnifiedMap<T_Key, T_Value> getKeyValueStore() {
        return keyValueStore_;
    }

    public ScheduledExecutorService getExecutorService() {
        return executorService_;
    }

    @Override
    public void put(final T_Key key, final T_Value value) {
        verifyKeyNotNull(key);
        verifyNotNull(value, "Value must not be null");

        getKeyValueStore().put(key, value);
    }

    private void verifyNotNull(final Object value, final String s) {
        if (null == value) {
            throw new IllegalArgumentException(s);
        }
    }

    private void verifyKeyNotNull(final T_Key key) {
        verifyNotNull(key, "Key must not be null");
    }

    @Override
    public void put(final T_Key key, final T_Value value, final Duration expireAfter) {
        if (expireAfter.getEpoch() < 0) {
            throw new IllegalArgumentException("expireAfter cannot be a negative duration");
        }

        put(key, value);
        getExecutorService().schedule(
            new ExpirationFunction(key, value),
            expireAfter.getDurationAmount(),
            expireAfter.getDurationUnit()
        );
    }

    @Override
    public T_Value get(final T_Key key) {
        verifyKeyNotNull(key);
        return getKeyValueStore().get(key);
    }

    @Override
    public boolean remove(final T_Key key) {
        final T_Value previousValue = getKeyValueStore().removeKey(key);
        return previousValue != null;
    }

    class ExpirationFunction implements Callable<T_Value> {

        private final T_Key key_;
        private final T_Value value_;

        public ExpirationFunction(final T_Key key, final T_Value value) {
            key_ = key;
            value_ = value;
        }

        @Override
        public T_Value call() throws Exception {
            final T_Value removed;
            if (value_.equals(getKeyValueStore().get(key_))) {
                removed = getKeyValueStore().remove(key_);
            }
            else {
                removed = null;
            }
            return removed;
        }
    }
}
