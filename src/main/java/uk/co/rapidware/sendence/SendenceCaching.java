package uk.co.rapidware.sendence;

/**
 * For brevity and to keep the proof of concept simple, we use a holder type pattern to encapsulate the
 * <code>SendenceCacheManager</code> implementation.  A single static method is provided to obtain the reference the
 * manager and it is guaranteed to return a full constructed (guarantee provided by JMM due to use of a final field)
 * non-null value.
 */
public class SendenceCaching {

    private static final SendenceCacheManager THE_CACHE_MANAGER = new SendenceCacheManagerImpl();

    public static SendenceCacheManager getDefaultCacheManager() {
        return THE_CACHE_MANAGER;
    }
}
