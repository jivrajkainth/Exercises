package uk.co.rapidware.words;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Jivraj on 30/07/2017.
 */
public class ChainTest {

    @Test
    public void testLongestChain() {
        final int longestChain = Chain.longestChain(new String[]{
            "a",
            "b",
            "ba",
            "bca",
            "bda",
            "bdca",
            });

        TestCase.assertEquals(4, longestChain);
    }
}