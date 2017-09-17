package utils;

import alexa.skill.model.Book;
import alexa.skill.model.Books;
import alexa.skill.model.Section;
import com.google.common.base.Preconditions;
import com.google.gson.*;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.iterator.ORecordIteratorClass;
import com.orientechnologies.orient.core.iterator.ORecordIteratorCluster;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import retrofit.*;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created by ranjiti on 12/27/15.
 */
public class AudioBooksDownloader {
    interface AudioBookService {
        @GET ("/api/feed/audiobooks")
        public Call<Books> getAudioBook(@Query("id") String id,
                                        @Query("extended") boolean flag);
    }

    private static final String databaseURL = "plocal:/Users/ranjiti/work/alexa/AudioBooks/resources/audiobooksmd";
    private static final String username = "admin";
    private static final String password = "admin";

    private ODatabaseDocument dbo;
    static private OPartitionedDatabasePool dbPool;

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://librivox.org")
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
            .build();

    AudioBookService service = retrofit.create(AudioBookService.class);

    public AudioBooksDownloader() {
        dbo = new ODatabaseDocumentTx(databaseURL);
        if (!dbo.exists()) {
            dbo.create();
        }

        dbPool = new OPartitionedDatabasePool(databaseURL,username,password);
    }

    public void downloadAudioBooks(int from, int to) {
        IntStream.rangeClosed(from,to).parallel().forEach( id -> {
            try {
                System.out.println("Processing " + id);
                Call<Books> call = service.getAudioBook(String.valueOf(id), true);
                Response<Books> response = call.execute();
                if (response.code() == 200) {
                    Books books = response.body();

                    List<Book> bookList = books != null ? books.getBooks() : null;
                    if (bookList != null && bookList.size() > 0) {
                        Book book = bookList.get(0);
                        if (book.isAudioBook()) {
                            System.out.println(" *** ");
                            System.out.println(book.getId());
                            System.out.println(book.getTitle());
                            System.out.println(book.getGenres().getListOfGenres().get(0).getName());
                            if (book.getZipFileUrl() != null)
                                System.out.println(book.getZipFileUrl());
                            System.out.println(" *** ");

                            String json = gson.toJson(book);
                            writeJsonToDB(json);
                        }
                        else {
                            System.err.println("Id " + id + " is not an audio book");
                        }
                    }
                    else {
                        System.err.println(" Id " + id  + " not found");
                    }
                }
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch (Exception ex) {
                System.err.println("!!Unable to download " + id + "!!");
                //ex.printStackTrace();
            }
        });
    }

    private void writeJsonToDB(String json) {
        ODatabaseDocumentTx db = dbPool.acquire();

        ODocument doc = new ODocument("Book");
        doc.fromJSON(json);
        db.save(doc);

        db.close();
    }

    public void verify_update() {
        String title = "Stoicism";

        ODatabaseDocumentTx db = dbPool.acquire();

        List<ODocument> result = db.query(
                new OSQLSynchQuery<ODocument>("select * from Book where title LIKE '%" + title + "%'"));
        ODocument doc = result.get(0);

        Map<String, Object> values = doc.toMap();
        values.keySet().forEach( key -> {
            System.out.println(key);
            System.out.println(values.get(key));
        });

        String json = doc.toJSON();
        Book book = new GsonBuilder().create().fromJson(json, Book.class);

        Section section = book.getSections().getListOfSections().get(0);
        System.out.println("Updating " + section.getTitle());

        section.setListenUrl("http://www.google.com");

        String updatedSection = new GsonBuilder().create().toJsonTree(section).toString();
        String updatedSections = new GsonBuilder().create().toJsonTree(book.getSections()).toString();

        System.out.println(updatedSections);

        System.out.println(db.command(new OCommandSQL( "UPDATE Book set listOfSections = " + updatedSections )).execute().toString());

        // Verify the updated was successful
        result = db.query(
                new OSQLSynchQuery<ODocument>("select * from Book where title LIKE '%" + title + "%'"));
        doc = result.get(0);
        System.out.println(doc.toJSON());

        db.close();
    }

    public boolean verify(int from, int to) {
        ODatabaseDocumentTx db = dbPool.acquire();
        List<ODocument> result = db.query(
                new OSQLSynchQuery<ODocument>("select * from Book"));
        System.out.println("Num records in DB : " + result.size());

        for (ODocument doc : result) {
            ORecord rec  = doc.getRecord();
            ODatabaseDocument dbDoc = rec.getDatabase();

            ORecordIteratorClass<ODocument> iter = dbDoc.browseClass("Book");
            while (iter.hasNext()) {
                ODocument document = iter.next();
                String json = document.toJSON();
                System.out.println(json);
            }
        }

        db.close();
        return true;
    }

    public static void main(String[] args) throws Exception {
        int from = 1; int to   = 15000;
        AudioBooksDownloader downloader = new AudioBooksDownloader();
        downloader.downloadAudioBooks(from,to);
        dbPool.close();

    }
}
