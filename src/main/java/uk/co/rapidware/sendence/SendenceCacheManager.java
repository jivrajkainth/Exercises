package uk.co.rapidware.sendence;

/**
 * Basic entry point for clients to obtain a reference to an existing Cache or create a new Cache.
 */
public interface SendenceCacheManager {

    /**
     * This method will create a new cache with the specified name unless it already exists - in which case a
     * <code>Runtime</code> exception is thrown.
     *
     * @param cacheName     unique cache name
     * @param configuration used to to create the cache - specfies parameters that provide optionality on cache
     *                      characteristics
     * @param <T_Key>       type of the Key
     * @param <T_Value>     type of the Class
     *
     * @return a newly created Cache
     */
    <T_Key, T_Value> SendenceCache<T_Key, T_Value> createCache(
        final String cacheName,
        final SendenceConfiguration<T_Key, T_Value> configuration
    );

    /**
     * @param cacheName the cache to be destroyed
     */
    void destroyCache(final String cacheName);

    /**
     * This method will retrieve the cache with the specified name as long as it exists and has matching type
     * parameters to those specified.  If the cache does not exist or type parameters do not match then a
     * <code>Runtime</code> exception is thrown.
     *
     * @param cacheName  unique cache name
     * @param keyClass   used to verify the type parameters of the cache
     * @param valueClass used to verify the type parameters of the cache
     * @param <T_Key>    type of the Key
     * @param <T_Value>  type of the Class
     *
     * @return a strongly typed reference to the cache with name specified in the parameters.
     */
    <T_Key, T_Value> SendenceCache<T_Key, T_Value> getCache(
        final String cacheName,
        T_Key keyClass,
        T_Value valueClass
    );
}
