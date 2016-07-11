package uk.co.rapidware.sendence;

/**
 * This API is for a simple in-memory key value store and is inspired by the JCache (JSR 017) API.  The only methods
 * provided are those stated in the requirements in order to keep the exercise small in scope.  As such this
 * represents no more than a proof of concept as at a minimum the API should contain the following capabilities to be
 * considered for use in a production system
 * <ul>
 *     <li>Close/Clear the cache</li>
 *     <li>Atomic replace</li>
 *     <li>Test the state of the cache</li>
 *     <li>Test for existence of keys</li>
 * </ul>
 *
 * In order to obtain an instance of a Cache, see the javadoc for <code>SendenceCacheProvider</code>
 */
public interface SendenceCache<T_Key, T_Value> {

    void put(final T_Key key, final T_Value value);

    void put(final T_Key key, final T_Value value, final Duration expireAfter);

    T_Value get(final T_Key key);

    boolean remove(final T_Key key);

}
