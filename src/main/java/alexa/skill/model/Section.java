package alexa.skill.model;

import com.google.common.base.Objects;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

import java.util.Arrays;

/**
 * Created by ranjiti on 12/27/15.
 */
@Root
public class Section {
    @Element
    private int id;
    @Element (name = "section_number")
    private int sectionNumber;
    @Element (required = false)
    private String title;
    @Element (name = "listen_url", required = false)
    private String listenUrl;
    @Element (name = "language")
    private String lang;
    @Element (name = "playtime")
    private int playTime;
    @Element (name = "file_name",required = false)
    private String fileName;
    private Readers readers;

    public int getId() {
        return id;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getListenUrl() {
        return listenUrl;
    }

    public String getLang() {
        return lang;
    }

    public int getPlayTime() {
        return playTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setListenUrl(String listenUrl) {
        this.listenUrl = listenUrl;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return id == section.id &&
                sectionNumber == section.sectionNumber &&
                playTime == section.playTime &&
                Objects.equal(title, section.title) &&
                Objects.equal(listenUrl, section.listenUrl) &&
                Objects.equal(lang, section.lang) &&
                Objects.equal(fileName, section.fileName) &&
                Objects.equal(readers, section.readers);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, sectionNumber, title, listenUrl, lang, playTime, fileName, readers);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", sectionNumber=" + sectionNumber +
                ", title='" + title + '\'' +
                ", listenUrl='" + listenUrl + '\'' +
                ", lang='" + lang + '\'' +
                ", playTime=" + playTime +
                ", fileName='" + fileName + '\'' +
                ", readers=" + readers +
                '}';
    }
}
