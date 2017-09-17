package alexa.skill.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Created by ranjiti on 12/30/15.
 */
@DynamoDBTable( tableName = "AudioBookSession")
public class UserSession {
    private String sessionId;

    private String bookId;

    private int sectionNum;

    private int offset;

    @DynamoDBHashKey (attributeName = "SessionId")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @DynamoDBAttribute (attributeName = "BookId")
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @DynamoDBAttribute (attributeName = "SectionNum")
    public int getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(int sectionNum) {
        this.sectionNum = sectionNum;
    }

    @DynamoDBAttribute (attributeName = "Offset")
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
