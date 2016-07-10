package uk.co.rapidware.sendence;

/**
 */
public interface SendenceCache<T_Key, T_Value> {

    void put(final T_Key key, final T_Value value);

    void put(final T_Key key, final T_Value value, final Duration expireAfter);

    T_Value get(final T_Key key);

    boolean remove(final T_Key key);

}
