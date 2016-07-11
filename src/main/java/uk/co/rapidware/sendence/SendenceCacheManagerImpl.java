package uk.co.rapidware.sendence;

import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * This cache manager uses coarse grained locking to provide concurrency support.  This is an acceptable strategy
 * since these operations are not performance sensitive - typically applications/components will obtain references to
 * caches once and then retain these reference for future use.  Infact this is the usage idiom that is encouraged.
 */
public class SendenceCacheManagerImpl implements SendenceCacheManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendenceCacheManagerImpl.class);

    public static Logger getLogger() {
        return LOGGER;
    }

    private static final float LOAD_FACTOR = 0.67f;
    public static final int DEFAULT_EXPECTED_CACHE_COUNT = 8;

    private final MutableMap<String, CacheTuple<?, ?>> caches_;
    private final ScheduledExecutorService scheduledExecutorService_;

    public SendenceCacheManagerImpl() {
        this(DEFAULT_EXPECTED_CACHE_COUNT, LOAD_FACTOR);
    }

    public SendenceCacheManagerImpl(
        final int expectedCacheCount,
        final float loadFactor
    ) {
        this(
            new UnifiedMap<>(expectedCacheCount, loadFactor),
            Executors.newSingleThreadScheduledExecutor(
                runnable -> new Thread(runnable, "Sendence Cache Servicing Thread")
            )
        );
    }

    /**
     * This constructor is intended for use by Unit tests only.
     *
     * @param executorService for running servicing tasks such as value expiry.
     */
    SendenceCacheManagerImpl(final ScheduledExecutorService executorService) {
        this(new UnifiedMap<>(DEFAULT_EXPECTED_CACHE_COUNT, LOAD_FACTOR), executorService);
    }

    private SendenceCacheManagerImpl(
        final MutableMap<String, CacheTuple<?, ?>> caches,
        final ScheduledExecutorService executoService
    ) {
        caches_ = caches;
        scheduledExecutorService_ = executoService;
    }

    public MutableMap<String, CacheTuple<?, ?>> getCaches() {
        return caches_;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService_;
    }

    @Override
    public <T_Key, T_Value> SendenceCache<T_Key, T_Value> createCache(
        final String cacheName, final SendenceConfiguration<T_Key, T_Value> configuration
    ) {
        if (null == cacheName || cacheName.isEmpty()) {
            throw new IllegalArgumentException("cacheName must not be null or empty");
        }

        final SendenceCacheImpl<T_Key, T_Value> newCache;
        synchronized (getCaches()) {
            final CacheTuple<?, ?> cacheTuple = getCaches().get(cacheName);
            if (null != cacheTuple) {
                throw new IllegalArgumentException(String.format("Cache [%s] already exists.", cacheName));
            }
            final int cacheSize = configuration.getCacheSize();
            getLogger().info("Creating Cache with name [{}], size [{}]", cacheName, cacheSize);
            newCache = new SendenceCacheImpl<>(cacheName, cacheSize, getScheduledExecutorService());
            getCaches().put(cacheName, new CacheTuple<>(newCache, configuration));
        }
        return newCache;
    }

    @Override
    public void destroyCache(final String cacheName) {
        throw new UnsupportedOperationException("To Be Implemented");
    }

    @Override
    public <T_Key, T_Value> SendenceCache<T_Key, T_Value> getCache(
        final String cacheName, final T_Key keyClass, final T_Value valueClass
    ) {
        if (null == cacheName || cacheName.isEmpty()) {
            throw new IllegalArgumentException("cacheName must not be null or empty");
        }

        if (null == keyClass) {
            throw new IllegalArgumentException("keyClass must not be null");
        }

        if (null == valueClass) {
            throw new IllegalArgumentException("keyClass must not be null");
        }

        final SendenceCacheImpl<T_Key, T_Value> cache;
        synchronized (getCaches()) {
            final CacheTuple<?, ?> cacheTuple = getCaches().get(cacheName);
            final Class<?> expectedKeyClass = cacheTuple.getConfiguration().getKeyClass();
            if (!expectedKeyClass.equals(keyClass)) {
                final String message =
                    String.format("Expected key class [%s] but [%s] was specified", expectedKeyClass, keyClass);
                throw new IllegalArgumentException(message);
            }

            final Class<?> expectedValueClass = cacheTuple.getConfiguration().getValueClass();
            if (!expectedValueClass.equals(valueClass)) {
                final String message =
                    String.format("Expected value class [%s] but [%s] was specified", expectedValueClass, valueClass);
                throw new IllegalArgumentException(message);
            }

            getLogger().info("Retrieving Cache with name [{}] with types Key[{}] and Value[{}]", cacheName, keyClass,
                             valueClass
            );
            cache = (SendenceCacheImpl<T_Key, T_Value>) cacheTuple.getCache();
        }
        return cache;
    }

    private static final class CacheTuple<T_Key, T_Value> {

        private final SendenceCacheImpl<T_Key, T_Value> cache_;
        private final SendenceConfiguration<T_Key, T_Value> configuration_;

        public CacheTuple(
            final SendenceCacheImpl<T_Key, T_Value> cache,
            final SendenceConfiguration<T_Key, T_Value> configuration
        ) {
            cache_ = cache;
            configuration_ = configuration;
        }

        public SendenceCacheImpl<T_Key, T_Value> getCache() {
            return cache_;
        }

        public SendenceConfiguration<T_Key, T_Value> getConfiguration() {
            return configuration_;
        }
    }
}
