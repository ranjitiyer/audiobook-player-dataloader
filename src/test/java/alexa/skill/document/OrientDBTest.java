package alexa.skill.document;

import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.sun.org.apache.bcel.internal.generic.RET;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.fail;

/**
 * Created by ranjiti on 1/3/16.
 */
public class OrientDBTest {
    private static final String databaseURL = "plocal:/Users/ranjiti/work/alexa/AudioBooks/resources/audiobooksmd";
    private static final String username = "admin";
    private static final String password = "admin";

    private static ODatabaseDocument dbo;
    static private OPartitionedDatabasePool dbPool;

    static {
        dbo = new ODatabaseDocumentTx(databaseURL);
        if (!dbo.exists()) {
            dbo.create();
        }

        dbPool = new OPartitionedDatabasePool(databaseURL,username,password);
    }

//    @Test
    public void verifyConnection() {
/*
        String dbUrl = "memory:test";
        ODatabaseDocumentTx db = new ODatabaseDocumentTx(dbUrl);

        String username = "admin";
        String password = "admin";

        if (db.exists()) {
            db.activateOnCurrentThread();
            db.open(username, password);
        }
        else {
            db.create();
        }

        ODocument doc = new ODocument("Person");
        doc.field( "name", "Luke" );
        doc.field( "surname", "Skywalker" );
        doc.field( "city", new ODocument("City").field("name","Rome").field("country", "Italy") );

        db.save(doc);

        for (ODocument person : db.browseClass("Person"))
            System.out.println( person );

        db.drop();
*/

    }

//    @Test
    public void verifyPutJsonDocuments() {
        String dbUrl = "memory:audiobooks";
        ODatabaseDocumentTx db = new ODatabaseDocumentTx(dbUrl);

        String username = "admin";
        String password = "admin";

        if (db.exists()) {
            db.activateOnCurrentThread();
            db.open(username, password);
        }
        else {
            db.create();
        }

        String json = "{\n" +
                "\t\"id\": \"720\",\n" +
                "\t\"title\": \"Pygmalion\",\n" +
                "\t\"description\": \"\\u003cp\\u003e\\u003ci\\u003ePygmalion\\u003c/i\\u003e (1913) is a play by George Bernard Shaw based on the Greek myth of the same name. It tells the story of Henry Higgins, a professor of phonetics (based on phonetician Henry Sweet), who makes a bet with his friend Colonel Pickering that he can successfully pass off a Cockney flower girl, Eliza Doolittle, as a refined society lady by teaching her how to speak with an upper class accent and training her in etiquette. In the process, Higgins and Doolittle grow close, but she ultimately rejects his domineering ways and declares she will marry Freddy Eynsford-Hill – a young, poor, gentleman. - The play was later the basis for the successful movie adaptation \\\"My Fair Lady\\\" with Audrey Hepburn as Eliza and Rex Harrison as Prof. Higgins. (Summary by Wikipedia/Gesine)\\u003c/p\\u003e\\n\\n\\u003cb\\u003eCast list:\\u003c/b\\u003e\\u003cbr\\u003e\\nNarrator – \\u003ca href\\u003d\\\"http://librivox.org/reader/56\\\"\\u003eKirsten Ferreri\\u003c/a\\u003e \\u0026 \\u003ca href\\u003d\\\"http://librivox.org/reader/188\\\"\\u003eMary Anderson\\u003c/a\\u003e\\u003cbr\\u003e\\nThe Daughter / Miss Clara Eynsford Hill – \\u003ca href\\u003d\\\"http://librivox.org/reader/937\\\"\\u003eSusie G.\\u003c/a\\u003e\\u003cbr\\u003e\\nThe Mother / Mrs Eynsford Hill – \\u003ca href\\u003d\\\"http://librivox.org/reader/20\\\"\\u003eGesine\\u003c/a\\u003e\\u003cbr\\u003e\\nA Bystander – \\u003ca href\\u003d\\\"http://librivox.org/reader/167\\\"\\u003ePeter Yearsley\\u003c/a\\u003e\\u003cbr\\u003e\\nFreddy Eynsford Hill – \\u003ca href\\u003d\\\"http://librivox.org/reader/348\\\"\\u003eianish\\u003c/a\\u003e\\u003cbr\\u003e\\nThe Flower Girl / Liza Doolittle – \\u003ca href\\u003d\\\"http://librivox.org/reader/28\\\"\\u003eKristin Hughes\\u003c/a\\u003e\\u003cbr\\u003e\\nA Gentleman / Captain Pickering – \\u003ca href\\u003d\\\"http://librivox.org/reader/31\\\"\\u003eMartin Clifton\\u003c/a\\u003e\\u003cbr\\u003e\\nThe Note Taker / Professor Henry Higgins – \\u003ca href\\u003d\\\"http://librivox.org/reader/66\\\"\\u003eAlex Foster\\u003c/a\\u003e\\u003cbr\\u003e\\nA Sarcastic Bystander – \\u003ca href\\u003d\\\"http://librivox.org/reader/167\\\"\\u003ePeter Yearsley\\u003c/a\\u003e\\u003cbr\\u003e\\nMrs Pearce – \\u003ca href\\u003d\\\"http://librivox.org/reader/112\\\"\\u003eChristiane Levesque\\u003c/a\\u003e\\u003cbr\\u003e\\nAlfred Doolittle – \\u003ca href\\u003d\\\"http://librivox.org/reader/94\\\"\\u003eDavid Barnes\\u003c/a\\u003e\\u003cbr\\u003e\\nMrs Higgins – \\u003ca href\\u003d\\\"http://librivox.org/reader/1154\\\"\\u003eLarysa Jaworski\\u003c/a\\u003e\\u003cbr\\u003e\\nParlour-Maid – \\u003ca href\\u003d\\\"http://librivox.org/reader/167\\\"\\u003eLinda Wilcox\\u003c/a\\u003e\\u003cbr\\u003e\\n\\nDirector/File editor – \\u003ca href\\u003d\\\"http://librivox.org/reader/2911\\\"\\u003eDavid Lawrence\\u003c/a\\u003e\\u003cbr\\u003e\\u003cbr\\u003e\",\n" +
                "\t\"language\": \"English\",\n" +
                "\t\"copyrightYear\": \"1916\",\n" +
                "\t\"numSections\": 7,\n" +
                "\t\"zipFileUrl\": \"http://www.archive.org/download/pygmalion_0906_librivox/pygmalion_0906_librivox_64kb_mp3.zip\",\n" +
                "\t\"librivoxUrl\": \"http://librivox.org/pygmalion-by-george-bernard-shaw/\",\n" +
                "\t\"totalTime\": \"2:54:03\",\n" +
                "\t\"authors\": {\n" +
                "\t\t\"listOfAuthors\": [{\n" +
                "\t\t\t\"id\": 603,\n" +
                "\t\t\t\"firstName\": \"George Bernard\",\n" +
                "\t\t\t\"lastName\": \"Shaw\",\n" +
                "\t\t\t\"dob\": \"1856\",\n" +
                "\t\t\t\"dod\": \"1950\"\n" +
                "\t\t}]\n" +
                "\t},\n" +
                "\t\"sections\": {\n" +
                "\t\t\"listOfSections\": [{\n" +
                "\t\t\t\"id\": 131994,\n" +
                "\t\t\t\"sectionNumber\": 0,\n" +
                "\t\t\t\"title\": \"Introduction\",\n" +
                "\t\t\t\"lang\": \"English\",\n" +
                "\t\t\t\"playTime\": 616\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": 131995,\n" +
                "\t\t\t\"sectionNumber\": 1,\n" +
                "\t\t\t\"title\": \"Act I\",\n" +
                "\t\t\t\"lang\": \"English\",\n" +
                "\t\t\t\"playTime\": 903\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": 131996,\n" +
                "\t\t\t\"sectionNumber\": 2,\n" +
                "\t\t\t\"title\": \"Act II\",\n" +
                "\t\t\t\"lang\": \"English\",\n" +
                "\t\t\t\"playTime\": 2630\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": 131997,\n" +
                "\t\t\t\"sectionNumber\": 3,\n" +
                "\t\t\t\"title\": \"Act III\",\n" +
                "\t\t\t\"lang\": \"English\",\n" +
                "\t\t\t\"playTime\": 1390\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": 131998,\n" +
                "\t\t\t\"sectionNumber\": 4,\n" +
                "\t\t\t\"title\": \"Act IV\",\n" +
                "\t\t\t\"lang\": \"English\",\n" +
                "\t\t\t\"playTime\": 839\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": 131999,\n" +
                "\t\t\t\"sectionNumber\": 5,\n" +
                "\t\t\t\"title\": \"Act V\",\n" +
                "\t\t\t\"lang\": \"English\",\n" +
                "\t\t\t\"playTime\": 2286\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": 132000,\n" +
                "\t\t\t\"sectionNumber\": 6,\n" +
                "\t\t\t\"title\": \"Conclusion\",\n" +
                "\t\t\t\"lang\": \"English\",\n" +
                "\t\t\t\"playTime\": 1779\n" +
                "\t\t}]\n" +
                "\t},\n" +
                "\t\"genres\": {\n" +
                "\t\t\"listOfGenres\": [{\n" +
                "\t\t\t\"id\": \"19\",\n" +
                "\t\t\t\"name\": \"Humorous Fiction\"\n" +
                "\t\t}]\n" +
                "\t}\n" +
                "}";

        ODocument doc = new ODocument("Book");
        doc = doc.fromJSON(json);

        db.save(doc);

        List<ODocument> result = db.query(
                new OSQLSynchQuery<ODocument>("select zipFileUrl from Book where description like '%George Bernard Shaw%'"));

        System.out.println(result.size());

        ODocument resultDoc = result.get(0);

        System.out.println(resultDoc.fieldValues()[0]);

    }

    @Test
    public void verifyPutDocument() {
    }

//    @Test
    public void verify_find_all_titles() {
        ODatabaseDocumentTx db = dbPool.acquire();

        List<ODocument> result = db.query(
                new OSQLSynchQuery<ODocument>("select title from Book"));

        for (ODocument doc : result) {
            System.out.println(doc.field("title").toString());
        }
        db.close();
    }

//    @Test
    public void verify_search_by_title() {

        String title = "Art of War";

        ODatabaseDocumentTx db = dbPool.acquire();

        List<ODocument> result = db.query(
                new OSQLSynchQuery<ODocument>("select title from Book where description like '%" + title + "%'"));

        db.close();
    }

//    @Test
    public void verify_search_by_keyword() {
        fail();
        ;
    }

    @Test
    public void verify_search_genre() {

    }
}
