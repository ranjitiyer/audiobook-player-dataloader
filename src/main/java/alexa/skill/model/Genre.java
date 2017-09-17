package alexa.skill.model;

import com.google.common.base.Objects;
import org.simpleframework.xml.Element;

/**
 * Created by ranjiti on 12/30/15.
 */
@Element
public class Genre {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equal(id, genre.id) &&
                Objects.equal(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name);
    }
}
