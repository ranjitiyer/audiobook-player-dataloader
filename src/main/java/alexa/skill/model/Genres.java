package alexa.skill.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by ranjiti on 12/30/15.
 */
@Element(name = "genres")
public class Genres {
    @ElementList (inline = true)
    private List<Genre> listOfGenres;

    public List<Genre> getListOfGenres() {
        return listOfGenres;
    }
}
