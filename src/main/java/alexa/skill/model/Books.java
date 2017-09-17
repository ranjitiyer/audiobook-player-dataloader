package alexa.skill.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by ranjiti on 12/27/15.
 */
@Root
public class Books {
    @ElementList
    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Books{" +
                "books=" + books +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Books that = (Books) o;
        return com.google.common.base.Objects.equal(books, that.books);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(books);
    }
}
