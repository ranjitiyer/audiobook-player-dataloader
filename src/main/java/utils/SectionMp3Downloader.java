package utils;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.request.GetRequest;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.squareup.okhttp.*;
import org.apache.commons.lang3.StringUtils;
import retrofit.http.Url;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

/**
 * Created by ranjiti on 1/26/16.
 */
public class SectionMp3Downloader {
    public Map<String,Map<String,String>> getSectionMp3ForTitle(String title,
                                                                String librivoxId) throws Exception {

        Map<String, Map<String, String>> titleSectionMap = new HashMap<>();

        OkHttpClient client = new OkHttpClient();

        List<String> librivoxIds = new ArrayList<String>();

        try {
            if (librivoxId == null) {
                Map<String, String> params = new HashMap<>();
                params.put("q", "\"" + title + "\"");
                params.put("fl[]", "identifier");
                params.put("page", "1");
                params.put("output", "json");

                String urlParams = buildQueryParams(params);

                Call call = client.newCall(new Request.Builder().url("https://archive.org/advancedsearch.php?" + urlParams).get().build());
                Response response = call.execute();
                ResponseBody body = response.body();
                Reader reader = body.charStream();

                // Extract numFound
                JsonReader jsonReader = new JsonReader(reader);
                jsonReader.setLenient(true);

                JsonElement elem = new JsonParser().parse(jsonReader);
                JsonObject responseObj = elem.getAsJsonObject().getAsJsonObject("response");

                String rows = responseObj.get("numFound").getAsString();
                params.put("rows", rows);

                // Get the identifiers
                urlParams = buildQueryParams(params);

                call = client.newCall(new Request.Builder().url("https://archive.org/advancedsearch.php?" + urlParams).get().build());
                reader = call.execute().body().charStream();
                ;

                // Extrat numFound
                jsonReader = new JsonReader(reader);
                jsonReader.setLenient(true);


                elem = new JsonParser().parse(jsonReader);
                JsonArray array = elem.getAsJsonObject().getAsJsonObject("response").getAsJsonArray("docs");
                array.forEach(jsonElement -> {
                    String id = jsonElement.getAsJsonObject().get("identifier").getAsString();
                    if (id.endsWith("librivox")) {
                        String url = "http://archive.org/metadata/" + id + "/metadata/title";
                        try {

                            System.out.println(title + ":" + id);
                            Reader r = client.newCall(new Request.Builder().url(url).get().build())
                                    .execute()
                                    .body()
                                    .charStream();

                            // Extrat numFound
                            JsonReader jr = new JsonReader(r);
                            jr.setLenient(true);

                            String metaTitle = new JsonParser().parse(jr).getAsJsonObject().get("result").getAsString();

                            if (metaTitle.contains(title) || title.contains(metaTitle)) {
                                librivoxIds.add(id);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
            else {
                librivoxIds.add(librivoxId);
            }

            // Get the server and directory
            String detailsUrl = "https://archive.org/details/" + librivoxIds.get(0) + "?output=json";
            Reader r = client.newCall(new Request.Builder().url(detailsUrl).get().build())
                    .execute()
                    .body()
                    .charStream();

            JsonReader jr = new JsonReader(r);
            jr.setLenient(true);


            JsonObject jsonObject = new JsonParser().parse(jr).getAsJsonObject();
            String server = jsonObject.get("server").getAsString();
            String dir = jsonObject.get("dir").getAsString();

            String baseUrl = "http://" + server + dir + "/";

            // we have the title and the id of the librivox audio
            String filesUrl = "http://archive.org/metadata/" + librivoxIds.get(0) + "/files";
            r = client.newCall(new Request.Builder().url(filesUrl).get().build())
                    .execute()
                    .body()
                    .charStream();

            Map<String,String> sectionMp3sMap = new HashMap<>();

            jr = new JsonReader(r);
            jr.setLenient(true);
            JsonObject obj2 = new JsonParser().parse(jr).getAsJsonObject();
            //System.out.println(obj2);

            JsonArray array = obj2.get("result").getAsJsonArray();
            //System.out.println(array.size());
            array.forEach( element -> {
                JsonObject resultObj = element.getAsJsonObject();
                String name = resultObj.get("name").getAsString();
                if (name.endsWith("_64kb.mp3")) {
                    String fullUrl = baseUrl + name;
                    String sectionTitle = name;
                    if (resultObj.has("title"))
                        sectionTitle = resultObj.get("title").getAsString();

                    sectionMp3sMap.put(sectionTitle, fullUrl);
                }
            });

            titleSectionMap.put(title, sectionMp3sMap);

            //System.out.println(title + ", numFound = " + responseObj.get("numFound").getAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return titleSectionMap;
    }

    public Map<String, String> encodeSections(String title,
                                                Map<String,String> ia_section_urls) throws Exception {
        SectionMp3Encoder encoder = new SectionMp3Encoder();
        Map<String,String> encodedFilesMap = new HashMap<>();
        OkHttpClient client = new OkHttpClient();

        ia_section_urls.entrySet().forEach( entry -> {
            try {
                String section_no = entry.getKey();

                section_no = section_no.replaceAll(" ", "_");
                String ia_url = entry.getValue();
                String filename = ia_url.substring(ia_url.lastIndexOf("/") + 1);

                Path tmpFile = Paths.get("/tmp", title, section_no, filename);

                System.out.println(tmpFile);

                // Ensure dirs exist
                Files.createDirectories(tmpFile.getParent());

                // Download file
                Call call = client.newCall(new Request.Builder().url(ia_url).get().build());
                InputStream is = call.execute().body().byteStream();

                // Write to tmp file on local machine
                FileOutputStream fos = new FileOutputStream(tmpFile.toFile());

                byte[] buff = new byte[1024 * 4];

                int b = is.read(buff);
                while (b != -1) {
                    byte[] destbuff = new byte[b];
                    System.arraycopy(buff,0,destbuff,0,b);
                    fos.write(destbuff);
                    b = is.read(buff);
                }
                fos.close();

                // File is in tmpFile
                String encodedFile = encoder.encode(tmpFile.toString());
                encodedFilesMap.put(section_no, encodedFile);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return encodedFilesMap;
    }


    /**
     * 1. Get section mp3 urls from IA
     * 2. Encode on local machine
     * 3. Upload to IA
     * 4. Return the encoded IA URL for each section
     *
     * @param title
     * @return
     */
    public Map<String,String> getSectionMp3sOnInternetArchive(String title,
                                                              String librivoxId,
                                                              boolean upload) throws Exception {
        try {
            Map<String, Map<String, String>> ia_title_section_urls =
                    getSectionMp3ForTitle(title, librivoxId);

            final Map<String,String> uploadedMp3Urls = new HashMap<>();
            ia_title_section_urls.keySet().parallelStream().forEach(title_key -> {
                try {
                    System.out.println(title_key);
                    Map<String, String> ia_section_urls = ia_title_section_urls.get(title_key);

                    if (upload) {
                        // encode sections to 48kb
                        Map<String,String> sectionToEncodedMp3Map =
                                encodeSections(title, ia_section_urls);

                        System.out.println(" ***** ");
                        // upload to ia
                        SectionMp3Uploader uploader = new SectionMp3Uploader();

                        sectionToEncodedMp3Map.entrySet().stream().forEach(entry -> {
                            String sectionTitle = entry.getKey();
                            String localEncodedUrl = entry.getValue();

                            try {
                                String ia_url = uploader.uploadToInternetArchive(title,
                                        sectionTitle,
                                        localEncodedUrl);

                                uploadedMp3Urls.put(sectionTitle, ia_url);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        uploadedMp3Urls.putAll(ia_section_urls);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            return uploadedMp3Urls;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String buildQueryParams(Map<String, String> params) {
        List<String> paramsList = Lists.newArrayList();
        params.forEach( (key, value) -> {
            paramsList.add(key + "=" + value);
        });
        return StringUtils.join(paramsList, "&");
    }
}
