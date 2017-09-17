package utils;

import alexa.skill.model.Book;
import alexa.skill.model.Section;
import com.google.common.base.Preconditions;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.iterator.ORecordIteratorClass;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Updates the audio book database with
 * section urls
 *
 */
public class SectionMp3Updater {
    ODatabaseDocumentTx db;
    public SectionMp3Updater (ODatabaseDocumentTx db) {
        this.db = db;
    }

    public void update(String title,
                       Map<String, String> sectionMp3UrlsMap) {
        JSONArray array = new JSONArray();

//        sectionMp3UrlsMap.keySet().forEach (key -> {
//            JSONObject object = new JSONObject();
//            object.put("name", key);
//            object.put("url", sectionMp3UrlsMap.get(key));
//
//            array.put(object);
//        });
//
        System.out.println(array.toString());
    }

/*    public void update(String title,
                         Map<String, String> sectionMp3UrlsMap) {


        List<ODocument> result = db.query(
                new OSQLSynchQuery<ODocument>("select * from Book"));
        System.out.println("Num records in DB : " + result.size());

        // pull out the record
        result = db.query(
                new OSQLSynchQuery<ODocument>("select * from Book where title LIKE '" + title + "'"));

        Preconditions.checkArgument(result.size() == 1);

        // turn it into the internal Book representation
        ODocument doc = result.get(0);
        String json = doc.toJSON();
        Book book = new GsonBuilder().create().fromJson(json, Book.class);

        // update the mp3 url in each section
        for (Section section : book.getSections().getListOfSections()) {
            Preconditions.checkArgument(sectionMp3UrlsMap.containsKey(section.getTitle()));
            String encodedMp3Url = sectionMp3UrlsMap.get(section.getTitle());
            section.setListenUrl(encodedMp3Url);
        }

        String updatedSections = new GsonBuilder().create().toJsonTree(book.getSections()).toString();
        System.out.println(updatedSections);

        // update
        db.command(new OCommandSQL( "UPDATE Book set listOfSections = " + updatedSections )).execute().toString();

        // close
        db.close();
    }*/
}
