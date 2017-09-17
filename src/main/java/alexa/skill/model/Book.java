package alexa.skill.model;

import com.google.common.base.Objects;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ranjiti on 12/27/15.
 */
@Root
public class Book {
    @Element
    private String id;
    @Element (required = false)
    private String title;
    @Element (required = false)
    private String description;
    @Element
    private String language;
    @Element (name = "copyright_year", required = false)
    private String copyrightYear;
    @Element (name = "num_sections")
    private int numSections;
    @Element (name = "url_zip_file", required = false)
    private String zipFileUrl;
    @Element (name = "url_librivox", required = false)
    private String librivoxUrl;
    @Element (name = "url_iarchive", required = false)
    private String url_iarchive;
    @Element (name = "totaltime", required = false)
    private String totalTime;
    @Element (name="authors", required = false)
    private Authors authors;
    @Element (name="sections", required = false)
    private Sections sections;
    @Element (name="genres", required = false)
    private Genres genres;


    public Genres getGenres() {
        return genres;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyrightYear() {
        return copyrightYear;
    }

    public int getNumSections() {
        return numSections;
    }

    public String getZipFileUrl() {
        return zipFileUrl;
    }

    public String getLibrivoxUrl() {
        return librivoxUrl;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public Authors getAuthors() {
        return authors;
    }

    public Sections getSections() {
        return sections;
    }

    public String getUrl_iarchive() {
        return url_iarchive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return numSections == book.numSections &&
                Objects.equal(id, book.id) &&
                Objects.equal(title, book.title) &&
                Objects.equal(description, book.description) &&
                Objects.equal(language, book.language) &&
                Objects.equal(copyrightYear, book.copyrightYear) &&
                Objects.equal(zipFileUrl, book.zipFileUrl) &&
                Objects.equal(librivoxUrl, book.librivoxUrl) &&
                Objects.equal(totalTime, book.totalTime) &&
                Objects.equal(authors, book.authors) &&
                Objects.equal(sections, book.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, title, description, language, copyrightYear, numSections, zipFileUrl, librivoxUrl, totalTime, authors, sections);
    }

    public boolean isAudioBook() {
        return zipFileUrl != null && !(zipFileUrl.equals(""));
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", copyrightYear='" + copyrightYear + '\'' +
                ", numSections=" + numSections +
                ", zipFileUrl='" + zipFileUrl + '\'' +
                ", librivoxUrl='" + librivoxUrl + '\'' +
                ", totalTime='" + totalTime + '\'' +
                ", authors=" + authors +
                ", sections=" + sections +
                '}';
    }
}
