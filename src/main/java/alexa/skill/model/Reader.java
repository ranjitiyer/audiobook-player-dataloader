package alexa.skill.model;

import com.google.common.base.Objects;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by ranjiti on 12/27/15.
 */
@Root
public class Reader {
    @Element (name = "reader_id")
    private String id;
    @Element(name = "display_name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return Objects.equal(id, reader.id) &&
                Objects.equal(name, reader.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name);
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
