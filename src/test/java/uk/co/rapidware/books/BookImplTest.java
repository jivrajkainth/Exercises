package uk.co.rapidware.books;

import com.gs.collections.impl.set.mutable.UnifiedSet;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Sonny on 14/11/2015.
 */
public class BookImplTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testForTitleAndAuthor() throws Exception {
        final BookImpl theMartian = BookImpl.forTitleAndAuthor("The Martian", "Weir, Andy");

        TestCase.assertEquals("The Martian", theMartian.getTitle().getTitleName());
        TestCase.assertEquals(theMartian.getTitle().getTitleName(), theMartian.getTitle().getTitleName());
        TestCase.assertEquals(1, theMartian.getAuthors().size());

        final Book letItSnow = BookImpl.forTitleAndAuthor("Let It Snow", "Green, John", "Johnson, Maureen",
                "Myracle, Lauren");

        TestCase.assertEquals(3, letItSnow.getAuthors().size());

        TestCase.assertEquals(
                UnifiedSet.newSetWith("Johnson, Maureen", "Myracle, Lauren", "Green, John"),
                letItSnow.getAuthors());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAuthorsImmutable() {
        final Book matilda = BookImpl.forTitleAndAuthor("Matilda", "Dahl, Roald");

        matilda.getAuthors().add("Kainth, Jivraj");
    }
}