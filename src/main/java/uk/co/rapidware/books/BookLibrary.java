package uk.co.rapidware.books;

import java.util.Set;

/**
 * A basic interface for a Book Library - does not currently support adding Books.
 */
public interface BookLibrary {

    Set<BookTitle> getBookTitlesForAuthor(final String author);

    Set<String> getBookAuthorsForTitle(final BookTitle bookTitle);

    boolean removeBook(BookTitle bookTitle);

    void removeAllBooksForAuthor(String author);
}
