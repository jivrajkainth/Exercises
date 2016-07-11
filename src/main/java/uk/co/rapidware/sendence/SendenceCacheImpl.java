package uk.co.rapidware.sendence;

import com.gs.collections.impl.map.mutable.UnifiedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;

/**
 * This is the proof of concept implementation of the <code>SendenceCache</code>.  As such it does not currently
 * provide Thread safety, which means that no memory visibility guarantees are made when a value is read from the
 * cache.  Values inserted prior may not necessarily be visible to other threads.  Be careful when using in a
 * multi-threaded environment.
 * <p>
 * The underlying storage is a Map from the GS Collections that is sympathetic to CPU memory access optimizations -
 * resulting in reduced pointer chasing especially when there are no hash collisions.  This storage also adds little
 * memory overhead since it does not use entry objects - resulting in little or no garbage under mutation.
 * </p>
 * Performance complexity matches that of other hash based collections in that best case is constant time (for keys
 * with well distributed hashes).  Poor performing hash functions (poorly distributed or take too long to compute)
 * would impact performance.
 */
public class SendenceCacheImpl<T_Key, T_Value> implements SendenceCache<T_Key, T_Value> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendenceCacheImpl.class);

    public static Logger getLogger() {
        return LOGGER;
    }

    private final String cacheName_;
    private final Map<T_Key, T_Value> keyValueStore_;
    private final ScheduledExecutorService executorService_;

    public SendenceCacheImpl(final String cacheName, final ScheduledExecutorService executorService) {
        cacheName_ = cacheName;
        keyValueStore_ = new UnifiedMap<>();
        executorService_ = executorService;
    }

    public SendenceCacheImpl(
        final String cacheName,
        final int expectedCacheSize,
        final ScheduledExecutorService executorService
    ) {
        cacheName_ = cacheName;
        keyValueStore_ = new HashMap<>(expectedCacheSize, 0.67f);
        executorService_ = executorService;
    }

    public String getCacheName() {
        return cacheName_;
    }

    public Map<T_Key, T_Value> getKeyValueStore() {
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
        if (expireAfter.getInMillis() < 0) {
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
        final T_Value previousValue = getKeyValueStore().remove(key);
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
            getLogger().info("Expiring value [{}] for key [{}] from Cache [{}]", value_, key_, getCacheName());
            final T_Value removed;
            if (value_.equals(getKeyValueStore().get(key_))) {
                removed = getKeyValueStore().remove(key_);
                getLogger().info("Value removed from Cache [{}]", getCacheName());
            }
            else {
                getLogger().info("Value is no longer in Cache [{}]", getCacheName());
                removed = null;
            }
            return removed;
        }
    }
}
