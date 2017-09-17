package alexa.skill.service;

import alexa.skill.model.Book;
import com.google.common.collect.Range;

/**
 * Created by ranjiti on 12/27/15.
 */
public interface AudioBookService {
    public Book getRandomBook();
}
