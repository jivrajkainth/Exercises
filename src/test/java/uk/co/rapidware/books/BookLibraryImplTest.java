package uk.co.rapidware.books;

import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class BookLibraryImplTest {

    private BookLibrary bookLibrary_;

    private BookTitle theMartianTitle_;
    private BookTitle lifeOnMarsTitle_;
    private String andyWeir_;

    private BookTitle letItSnowTitle_;
    private String johnGreen_;
    private String maureenJohnson_;
    private String laurenMyracle_;

    private BookTitle theFaultInOurStarsTitle_;

    @Before
    public void setUp() throws Exception {
        letItSnowTitle_ = BookTitle.forTitle("Let It Snow");
        johnGreen_ = "Green, John";
        maureenJohnson_ = "Johnson, Maureen";
        laurenMyracle_ = "Myracle, Lauren";

        theFaultInOurStarsTitle_ = BookTitle.forTitle("The Fault in Our Stars");

        theMartianTitle_ = BookTitle.forTitle("The Martian");
        lifeOnMarsTitle_ = BookTitle.forTitle("Life on Mars");
        andyWeir_ = "Weir, Andy";

        final BookImpl theMartian = BookImpl.forTitleAndAuthor(theMartianTitle_.getTitleName(), andyWeir_);
        final BookImpl lifeOnMars = BookImpl.forTitleAndAuthor(lifeOnMarsTitle_.getTitleName(), andyWeir_);
        final BookImpl letItSnow =
                BookImpl.forTitleAndAuthor(letItSnowTitle_.getTitleName(), johnGreen_, maureenJohnson_,
                        laurenMyracle_);
        final BookImpl theFaultInOurStars =
                BookImpl.forTitleAndAuthor(theFaultInOurStarsTitle_.getTitleName(), johnGreen_);

        bookLibrary_ =
                new BookLibraryImpl(() -> FastList.newListWith(theMartian, letItSnow, lifeOnMars, theFaultInOurStars));
    }

    @After
    public void tearDown() throws Exception {

    }

    private BookLibraryImpl getBookLibraryAsImpl() {
        return BookLibraryImpl.class.cast(bookLibrary_);
    }

    @Test
    public void testInitialize() throws Exception {
        final BookLibraryImpl bookLibrary = getBookLibraryAsImpl();
        TestCase.assertFalse("BookLibrary should not be initialized by the constructor",
                bookLibrary.isInitialized());
        bookLibrary.initialize();
        TestCase.assertTrue(bookLibrary.isInitialized());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeCalledTwice() throws Exception {
        final BookLibraryImpl bookLibrary = getBookLibraryAsImpl();
        TestCase.assertFalse("BookLibrary should not be initialized by the constructor",
                bookLibrary.isInitialized());
        bookLibrary.initialize();
        bookLibrary.initialize();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBookLoader() throws Exception {
        new BookLibraryImpl(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testCheckInitializedGetBookTitlesForAuthor() throws Exception {
        bookLibrary_.getBookTitlesForAuthor(andyWeir_);
    }

    @Test(expected = IllegalStateException.class)
    public void testCheckInitializedGetBookAuthorsForTitle() throws Exception {
        bookLibrary_.getBookAuthorsForTitle(theMartianTitle_);
    }

    @Test(expected = IllegalStateException.class)
    public void testCheckInitializedRemoveBook() throws Exception {
        bookLibrary_.removeBook(theMartianTitle_);
    }

    @Test(expected = IllegalStateException.class)
    public void testCheckInitializedRemoveAllBooksForAuthor() throws Exception {
        bookLibrary_.removeAllBooksForAuthor(andyWeir_);
    }

    @Test
    public void testGetBookTitlesForAuthor() throws Exception {
        getBookLibraryAsImpl().initialize();

        final Set<BookTitle> andyWeirBooks = bookLibrary_.getBookTitlesForAuthor(andyWeir_);
        TestCase.assertEquals(2, andyWeirBooks.size());
        TestCase.assertEquals(UnifiedSet.newSetWith(theMartianTitle_, lifeOnMarsTitle_), andyWeirBooks);

        final Set<BookTitle> booksForAuthorNotExist = bookLibrary_.getBookTitlesForAuthor("Not Exists");
        TestCase.assertTrue(booksForAuthorNotExist.isEmpty());

        final Set<BookTitle> johnGreenBooks = bookLibrary_.getBookTitlesForAuthor(johnGreen_);
        TestCase.assertEquals(2, johnGreenBooks.size());
        TestCase.assertEquals(UnifiedSet.newSetWith(theFaultInOurStarsTitle_, letItSnowTitle_), johnGreenBooks);
    }

    @Test
    public void testGetBookAuthorsForTitle() throws Exception {
        getBookLibraryAsImpl().initialize();
        final Set<String> authors = bookLibrary_.getBookAuthorsForTitle(letItSnowTitle_);
        TestCase.assertEquals(3, authors.size());
        TestCase.assertEquals(UnifiedSet.newSetWith(laurenMyracle_, johnGreen_, maureenJohnson_), authors);

        final Set<String> authorsForTitleNotExist = bookLibrary_.getBookAuthorsForTitle(BookTitle.forTitle("None"));
        TestCase.assertTrue(authorsForTitleNotExist.isEmpty());
    }

    @Test
    public void testRemoveBook() throws Exception {
        getBookLibraryAsImpl().initialize();
        TestCase.assertTrue("Book should be removed!", bookLibrary_.removeBook(lifeOnMarsTitle_));
        TestCase.assertFalse("Book has already been removed", bookLibrary_.removeBook(lifeOnMarsTitle_));

        final Set<BookTitle> andyWeirBooks = bookLibrary_.getBookTitlesForAuthor(andyWeir_);
        TestCase.assertEquals("There should only be 1 Andy Weir book after the removal", 1, andyWeirBooks.size());
        TestCase.assertTrue("This book should still be in the Library", andyWeirBooks.contains(theMartianTitle_));
    }

    @Test
    public void testRemoveAllBooksForAuthor() throws Exception {
        getBookLibraryAsImpl().initialize();

        final Set<BookTitle> johnGreenBooks = bookLibrary_.getBookTitlesForAuthor(johnGreen_);
        TestCase.assertEquals(2, johnGreenBooks.size());

        bookLibrary_.removeAllBooksForAuthor(johnGreen_);
        final Set<BookTitle> remainingJohnGreenBooks = bookLibrary_.getBookTitlesForAuthor(johnGreen_);
        TestCase.assertTrue(remainingJohnGreenBooks.isEmpty());

        final Set<BookTitle> maureenJohnsonBooks = bookLibrary_.getBookTitlesForAuthor(maureenJohnson_);
        TestCase.assertTrue("The only Book in the library was co-authored with John Green and this has been removed",
                maureenJohnsonBooks.isEmpty());

        final Set<BookTitle> laurenMyracleBooks = bookLibrary_.getBookTitlesForAuthor(laurenMyracle_);
        TestCase.assertTrue("The only Book in the library was co-authored with John Green and this has been removed",
                laurenMyracleBooks.isEmpty());
    }
}