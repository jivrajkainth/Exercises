package uk.co.rapidware.circles;

import org.junit.Test;
import uk.co.rapidware.words.Chain;

/**
 * Created by Jivraj on 30/07/2017.
 */
public class FriendsTest {

    @Test
    public void testFriends() throws Exception {

        Friends.friendCircles(
            new String[]{
                "YYNN",
                "YYYN",
                "NYYN",
                "NNNY",

                }
        );
    }
}