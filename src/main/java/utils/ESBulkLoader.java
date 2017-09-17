package utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by ranjiti on 10/23/16.
 */
public class ESBulkLoader {
    private static final String databaseURL = "plocal:/Users/ranjiti/work/alexa/AudioBooks/resources/audiobooksmd";
    private static final String username = "admin";
    private static final String password = "admin";

    private ODatabaseDocument dbo;
    static private OPartitionedDatabasePool dbPool;

    public ESBulkLoader () {
        dbo = new ODatabaseDocumentTx(databaseURL);
        if (!dbo.exists()) {
            dbo.create();
        }

        dbPool = new OPartitionedDatabasePool(databaseURL,username,password);
    }

    private List<String> getAllTitles() {
        List titles = Lists.newArrayList();
        ODatabaseDocumentTx db = dbPool.acquire();
        try {
            List<ODocument> result = db.query(
                    new OSQLSynchQuery<ODocument>("select title from Book"));

            for (ODocument doc : result) {
                titles.add(doc.field("title").toString());
            }
        }
        finally {
            db.close();
        }
        return titles;
    }

    /**
     * Assumes AudioBooksDownloader has populated
     * the OrientDB with books.
     *
     * For each book, get the section mp3 urls,
     * updates the JSON object.
     *
     * Uses JestUtils to upload a collection
     * of audio books JSON objects to ES
     *
     * @throws Exception
     */

    public void uploadToES() throws Exception {
        ODatabaseDocumentTx db = dbPool.acquire();
        List<ODocument> result = db.query(
                new OSQLSynchQuery<ODocument>("select * from Book "));

        System.out.println("Num records in DB : " + result.size());
        System.out.println("Building up a list of documents");
        List<ODocument> orientDocList = Lists.newArrayList();
        for (ODocument doc : result) {
            orientDocList.add(doc);
        }

        // Initialize Jest Client
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://search-audiobooks-brqd376uelsbgtdu6z3ufkzyt4.us-east-1.es.amazonaws.com")
                .multiThreaded(true)
                .build());
        JestClient client = factory.getObject();
        JestUtils jestUtil = new JestUtils(client);

        BlockingQueue<JSONObject> queue = new ArrayBlockingQueue<JSONObject>(15000);
        jestUtil.startInserter("audiobooks","book", queue);
        System.out.println("Processing document list");

        orientDocList.parallelStream().forEach( document -> {
            try {
                String jsonString = document.toJSON();
                JsonElement elem = new JsonParser().parse(jsonString);

                String title = elem.getAsJsonObject().get("title").getAsString();
                String librivoxId = elem.getAsJsonObject().get("url_iarchive").getAsString();
                librivoxId = librivoxId.substring(librivoxId.lastIndexOf("/")+1);

                // Get the section mp3 urls from Section mp3 downloader
                SectionMp3Downloader downloader = new SectionMp3Downloader();
                Map<String,String> sectionMp3OnIAMap = downloader.getSectionMp3sOnInternetArchive(
                        title, librivoxId, false);

                Pattern p = Pattern.compile("_[0-9][0-9]_");
                JSONArray iaMp3Urls = new JSONArray();

                sectionMp3OnIAMap.keySet().forEach( key -> {
                    JSONObject sectionMp3 = new JSONObject();
                    String url = sectionMp3OnIAMap.get(key);
                    Matcher matcher = p.matcher(url);
                    if (matcher.find()) {
                        String sectionNumberMatch = matcher.group();
                        String sectionNumber = sectionNumberMatch.substring(1,sectionNumberMatch.length() - 1);
                        sectionMp3.put("number", Integer.valueOf(sectionNumber));
                    }
                    sectionMp3.put("name", key);
                    sectionMp3.put("url", sectionMp3OnIAMap.get(key));

                    iaMp3Urls.put(sectionMp3);
                });

                JSONObject esJsonObj = new JSONObject(jsonString);
                JSONObject sectionsJson = esJsonObj.getJSONObject("sections");
                sectionsJson.put("sectionUrls", iaMp3Urls);

                // add it to our internal list
                queue.add(transformToES(esJsonObj));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        jestUtil.stopInserter();

        db.close();
    }

    public JSONObject transformToES(JSONObject jsonObject) {
        JSONObject sections = (JSONObject)  jsonObject.remove("sections");
        List<JSONObject> sectionsMetaList = jsonArrayToList(sections.getJSONArray("listOfSections"));
        List<JSONObject> sectionsUrlsList = jsonArrayToList(sections.getJSONArray("sectionUrls"));

        /**
         * 1 : {name: ""}
         * 2 : {name: "")
         */
        Map<Integer, JSONObject> sectionMetaListMap = sectionsMetaList
                .stream()
                .collect(Collectors
                        .toMap(jsonObject1 -> jsonObject1.getInt("sectionNumber"),
                                jsonObject12 -> jsonObject12));



        if (
            // Can't process URLs if the urls map doesn't have section numbers
            sectionsUrlsList
                .stream()
                .anyMatch(sectionUrlObj -> sectionUrlObj.has("number"))) {

            try {
                /**
                 * 1 : "http://.mp3"
                 * 2 : "http://.mp3"
                 *
                 * Some section urls just don't have the 'number' field
                 *
                 */

                Map<Integer, String> sectionsUrlsListMap = sectionsUrlsList
                        .stream()
                        .collect(Collectors
                                .toMap(jsonObject1 -> jsonObject1.getInt("number"),
                                        jsonObject12 -> jsonObject12.getString("url")));


                // Join the two Maps

                Map<Integer, JSONObject> duplicateMetaListMap = Maps.newHashMap(sectionMetaListMap);
                duplicateMetaListMap.keySet().forEach(sectionNumber -> {
                    JSONObject sectionMeta = sectionMetaListMap.get(sectionNumber);
                    String url = sectionsUrlsListMap.get(sectionNumber);

                    sectionMeta.put("url", url);
                    sectionMetaListMap.put(sectionNumber, sectionMeta);
                });
            } catch (Exception ex) {
                // Some sections Urls objects have the same number for all sections
                // This results in a a DuplicateKey exception when mapping the list
                // to a Map
                System.err.println(ex.getMessage());
            }
        }


        // Map it to a JSONArray
        JSONArray sectionsArray = new JSONArray();
        sectionMetaListMap.values().forEach(v -> sectionsArray.put(v));

        // Transform Genres
        JSONObject genres = (JSONObject) jsonObject.remove("genres");
        List<JSONObject> genresList = jsonArrayToList(genres.getJSONArray("listOfGenres"));
        List<String> genreNamesList = genresList
                .stream()
                .map(genreObject -> genreObject.getString("name"))
                .collect(Collectors.toList());

        // Map it to JSONArray
        JSONArray genresArray = new JSONArray();
        genreNamesList.stream().forEach(v -> genresArray.put(v));

        // Transform Authors
        JSONObject authors = (JSONObject) jsonObject.remove("authors");
        List<JSONObject> authorsList = jsonArrayToList(authors.getJSONArray("listOfAuthors"));
        List<String> authorsNamesList = authorsList
                .stream()
                .map(authorObject -> {
                    String fullName = "";
                    if (authorObject.has("firstName")) {
                        fullName += authorObject.getString("firstName") + " ";
                    }
                    if (authorObject.has("lastName")) {
                        fullName += authorObject.getString("lastName");
                    }
                    return fullName;
                 })
                .collect(Collectors.toList());

        // Map it to JSONArray
        JSONArray authorsArray = new JSONArray();
        authorsNamesList.stream().forEach(v -> authorsArray.put(v));

        jsonObject.put("sections", sectionsArray);
        jsonObject.put("genres", genresArray);
        jsonObject.put("authors", authorsArray);

        return jsonObject;
    };

    private List<JSONObject> jsonArrayToList(JSONArray jsonArray) {
        List<JSONObject> jsonObjectsList = Lists.newArrayList();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObjectsList.add(jsonArray.getJSONObject(i));
        }

        return jsonObjectsList;
    }


    public static void main(String[] args) throws Exception {
        new ESBulkLoader().uploadToES();
    }
}
