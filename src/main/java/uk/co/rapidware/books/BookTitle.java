package uk.co.rapidware.books;

/**
 * Although the title of a book is just a single String value, it is modelled as a first class type for the following
 * reasons:
 * <ol>
 *     <li>It is the Primary Key of <code>Book</code></li>
 *     <li>It is a value of same type as Author - a strong type helps to distinguish it</li>
 *     <li>Allows for easier future extensibility</li>
 * </ol>
 * The reasons above combine to make the system easier to maintain.
 */
public final class BookTitle {

    private final String titleName_;

    BookTitle(final String titleName) {
        titleName_ = titleName;
    }

    public static BookTitle forTitle(final String title) {
        if (null == title || title.isEmpty()) {
            throw new IllegalArgumentException("title must be a non-null non-empty String");
        }
        return new BookTitle(title);
    }

    public String getTitleName() {
        return titleName_;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final BookTitle bookTitle = (BookTitle) o;

        return !(titleName_ != null ? !titleName_.equals(bookTitle.titleName_) : bookTitle.titleName_ != null);

    }

    @Override
    public int hashCode() {
        return titleName_ != null ? titleName_.hashCode() : 0;
    }
}
