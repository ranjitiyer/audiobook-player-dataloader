package alexa.skill.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by ranjiti on 12/27/15.
 */
@Element(name = "readers")
public class Readers {
    @ElementList(inline = true)
    private List<Reader> listOfReaders;
}
