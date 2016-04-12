package uk.co.rapidware.books;

/**
 * This interface is an abstraction for supporting the loading of Books into the BookLibrary.
 * The functionality provided by the library is agnostic to where/how the books are loaded.
 */
public interface BookLoader {

    Iterable<? extends Book> getAllBooks();

}
