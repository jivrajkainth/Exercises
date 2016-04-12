package uk.co.rapidware.books;

import java.util.Set;

/**
 * Book is the main value object that is the subject of the <code>BookLibrary</code>.  Hence it is modelled via an
 * Interface.
 */
public interface Book {

    BookTitle getTitle();

    Set<String> getAuthors();
}
