package uk.co.rapidware.books;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

public class BookTitleTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreation() {
        final BookTitle newBookTitle = BookTitle.forTitle("Created");

        TestCase.assertNotNull("Static factory method should have constructed a new instance", newBookTitle);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTitle() {
        BookTitle.forTitle(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTitle() {
        BookTitle.forTitle("");
    }

    @Test
    public void testGetTitle() {
        final String title = "A Brief History of Time";
        final BookTitle aBriefHistoryOfTime = BookTitle.forTitle(title);

        TestCase.assertEquals(String.format("getTitleName did not return [%s]", title), title,
                aBriefHistoryOfTime.getTitleName());
    }

    @Test
    public void testEquality() throws Exception {
        final BookTitle aBook = BookTitle.forTitle("A Book");
        final BookTitle aDifferentBook = BookTitle.forTitle("A Different Book");

        TestCase.assertFalse("BookTitle objects should not be equal - check equals() implementation",
                aBook.equals(aDifferentBook));
        TestCase.assertEquals("BookTitle should be equal to itself", aBook, aBook);

        final BookTitle aBookThatIsTheSame = BookTitle.forTitle("A Book");

        TestCase.assertEquals("BookTitle objects with the same Title should be equal", aBook, aBookThatIsTheSame);
    }

    @Test
    public void testNullInEquals() {
        final BookTitle aBookTitle = BookTitle.forTitle("A Book");
        TestCase.assertFalse("Null argument to equals should evaluate to false result", aBookTitle.equals(null));
    }

    @Test
    public void testCanBeUsedInHashBasedCollection() {
        final HashSet<BookTitle> hashSet = new HashSet<>();

        final BookTitle aBookTitle = BookTitle.forTitle("A Book");
        final BookTitle aDifferentBookTitle = BookTitle.forTitle("A Different Book");
        final BookTitle aBookTitleThatIsTheSame = BookTitle.forTitle("A Book");

        TestCase.assertTrue("aBookTitle should have been added as Set is empty", hashSet.add(aBookTitle));
        TestCase.assertTrue("aDifferentBookTitle should have been added as it has not been added before.",
                hashSet.add(aDifferentBookTitle));

        TestCase.assertTrue(hashSet.contains(aBookTitle));
        TestCase.assertTrue(hashSet.contains(aDifferentBookTitle));
        TestCase.assertTrue("A Key equal to this one was added so result should be true",
                hashSet.contains(aBookTitleThatIsTheSame));
    }
}