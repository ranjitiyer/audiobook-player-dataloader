package alexa.skill.model;

import com.google.common.base.Objects;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.strategy.Type;

/**
 * Created by ranjiti on 12/27/15.
 */
@Root
public class Author {
    @Element
    private int id;
    @Element (name = "first_name", required = false)
    private String firstName;
    @Element (name = "last_name")
    private String lastName;
    @Element (required = false)
    private String dob;
    @Element (required = false)
    private String dod;

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDob() {
        return dob;
    }

    public String getDod() {
        return dod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id == author.id &&
                dob == author.dob &&
                dod == author.dod &&
                Objects.equal(firstName, author.firstName) &&
                Objects.equal(lastName, author.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, firstName, lastName, dob, dod);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob=" + dob +
                ", dod=" + dod +
                '}';
    }
}
