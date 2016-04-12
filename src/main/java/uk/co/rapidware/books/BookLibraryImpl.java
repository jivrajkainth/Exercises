package uk.co.rapidware.books;

import com.gs.collections.api.map.MutableMapIterable;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.set.UnifiedSetMultimap;
import com.gs.collections.impl.utility.Iterate;

import java.util.Collections;
import java.util.Set;

/**
 * This implementation of the <code>BookLibrary</code> uses the <code>BookLoader</code> abstraction to
 * initialize the library with a working set of Books.
 * <p/>
 * The working set is loaded only during the call to <code>initialize</code> - therefore a reference to the Library
 * should only be passed to clients once initialized.
 */
public class BookLibraryImpl implements BookLibrary {

    private final BookLoader bookLoader_;
    private boolean initialized_ = false;

    /**
     * <code>UnifiedMap</code> is optimized for lookups as
     * entries are stored in as individual key and value objects
     * in consecutive slots which is CPU cache friendly.
     */
    private final MutableMapIterable<BookTitle, Book> bookByTitle_ = new UnifiedMap<>();

    /**
     * Similar lookup performance properties to a <code>UnifiedMap</code>
     */
    private final MutableSetMultimap<String, Book> booksByAuthor_ = new UnifiedSetMultimap<>();

    public BookLibraryImpl(final BookLoader bookLoader) {
        if (null == bookLoader) {
            throw new IllegalArgumentException("BookLoader cannot be null");
        }
        bookLoader_ = bookLoader;
    }

    public void initialize() {
        if (isInitialized()) {
            throw new IllegalStateException("BookLibrary has already been initialized");
        }

        for( final Book eachBook : getBookLoader().getAllBooks() ) {
            getBookByTitle().put(eachBook.getTitle(), eachBook);
        }
        getBookByTitle().groupByEach(Book::getAuthors, getBooksByAuthor());

        initialized_ = true;
    }

    BookLoader getBookLoader() {
        return bookLoader_;
    }

    boolean isInitialized() {
        return initialized_;
    }

    /**
     * Convenience checker method that throws an IllegalStateException if <code>BookLibrary</code> API methods
     * are called before being initialized.
     */
    void checkInitialized() {
        if (!isInitialized()) {
            throw new IllegalStateException("BookLibrary has not been initialized");
        }
    }

    MutableMapIterable<BookTitle, Book> getBookByTitle() {
        return bookByTitle_;
    }

    MutableSetMultimap<String, Book> getBooksByAuthor() {
        return booksByAuthor_;
    }

    @Override
    public Set<BookTitle> getBookTitlesForAuthor(final String author) {
        checkInitialized();
        return getBooksByAuthor().get(author).collect(Book::getTitle);
    }

    @Override
    public Set<String> getBookAuthorsForTitle(final BookTitle bookTitle) {
        checkInitialized();
        final Book book = getBookByTitle().get(bookTitle);
        return null == book ? Collections.emptySet() : book.getAuthors();
    }

    @Override
    public boolean removeBook(final BookTitle bookTitle) {
        checkInitialized();

        final Book book = getBookByTitle().removeKey(bookTitle);
        if (book != null) {
            Iterate.forEach(book.getAuthors(), (author) -> getBooksByAuthor().remove(author, book));
        }
        return null != book;
    }

    @Override
    public void removeAllBooksForAuthor(final String author) {
        checkInitialized();
        final MutableSet<Book> removedBooks = getBooksByAuthor().removeAll(author);
        if (null != removedBooks) {
            removedBooks.each((book) -> removeBook(book.getTitle()));
        }
    }
}
