package uk.co.rapidware.books;

import com.gs.collections.impl.set.mutable.UnifiedSet;

import java.util.Collections;
import java.util.Set;

public class BookImpl implements Book {

    private final BookTitle title_;
    private final Set<String> authors_;

    BookImpl(final BookTitle title, final Set<String> authors) {
        title_ = title;
        authors_ = Collections.unmodifiableSet(authors);
    }

    public static BookImpl forTitleAndAuthor(final String title, final String... authors) {
        return new BookImpl(BookTitle.forTitle(title), UnifiedSet.newSetWith(authors));
    }

    @Override
    public BookTitle getTitle() {
        return title_;
    }

    @Override
    public Set<String> getAuthors() {
        return authors_;
    }
}
