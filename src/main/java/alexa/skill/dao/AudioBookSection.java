package alexa.skill.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.net.URL;

/**
 * Created by ranjiti on 12/30/15.
 */
@DynamoDBTable( tableName = "AudioBookSections")
public class AudioBookSection {
    private String bookIdSectionId;
    private String title;
    private int duration;
    private URL mp3Path;

    @DynamoDBHashKey (attributeName = "BookId_SectionId")
    public String getBookIdSectionId() {
        return bookIdSectionId;
    }

    public void setBookIdSectionId(String bookIdSectionId) {
        this.bookIdSectionId = bookIdSectionId;
    }

    @DynamoDBAttribute (attributeName = "Title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute (attributeName = "Duration")
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @DynamoDBAttribute (attributeName = "Mp3Path")
    public URL getMp3Path() {
        return mp3Path;
    }

    public void setMp3Path(URL mp3Path) {
        this.mp3Path = mp3Path;
    }
}
