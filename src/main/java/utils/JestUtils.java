package utils;

import alexa.skill.model.es.SearchResult;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Index;
import io.searchbox.core.Index.Builder;
import io.searchbox.core.Search;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.mapping.PutMapping;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


/**
 * Created by ranjiti on 11/2/16.
 */
public class JestUtils {
    JestClient client;
    AtomicBoolean stop = new AtomicBoolean(false);

    public JestUtils(JestClient client) {
        this.client = client;
    }

    public boolean enableTimestamps(String index, String type) throws IOException {
        String mapping = "{\n" +
                "                \"mappings\": {\n" +
                "                \"<type>\": {\n" +
                "                    \"_timestamp\": {\n" +
                "                        \"enabled\": true\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n";

        PutMapping putMapping = new PutMapping.Builder(
                index,
                type,
                mapping
        ).build();

        return client.execute(putMapping).isSucceeded();
    }

    public boolean deleteIndex(String indexName) throws IOException {
        return client.execute(new DeleteIndex.Builder(indexName)
                .build()).isSucceeded();
    }

    public boolean createIndex(String indexName) throws IOException {
        Settings settings = Settings.builder()
                .put("number_of_shards",5)
                .put("number_of_replicas",1)
                .build();

        JestResult result = client.execute(new CreateIndex.Builder(indexName).settings(settings).build());

        System.out.println(result);
        return result.isSucceeded();
    }

    public List<SearchResult> matchQuery(String field, String value, List<String> resultFields) {
        QueryBuilder termsQ =
                QueryBuilders.matchQuery(field, value);
        try {
            JestResult result = client.execute( new Search.Builder(termsQ.toString()).build());
            JsonArray hits = result.getJsonObject().getAsJsonObject("hits").getAsJsonArray("hits");

            Gson gson = new Gson();
            SearchResult[] results = gson.fromJson(hits, SearchResult[].class);

            List<SearchResult> resultsAsList = Lists.newArrayList(results);


            Collections.sort(resultsAsList);

            return resultsAsList;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String join(List<String> list, String separator) {
        return list.stream().reduce((s, s2) -> s + "," + s2).get();
    }


    public void startInserter(String index,
                              String type,
            final BlockingQueue<JSONObject> workQ) {
        stop.set(true);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<JSONObject> list = Lists.newArrayList();
                            while (stop.get()) {
                                try {
                                    int items = workQ.drainTo(list);
                                    if (items > 0) {
                                        bulkInsert(index, type, list);
                                        System.out.println("Inserted " + list.size());
                                        list.clear();
                                    }
                                    Thread.sleep(2000);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            // we were asked to stop. Drain the queue
                            System.out.println("Inserter stopping");
                            workQ.removeAll(list);
                            bulkInsert(index, type, list);
                            list.clear();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
    }

    public void stopInserter() {
        stop.set(false);
    }

    public int bulkInsert(String index,
                                         String type,
            Collection<JSONObject> json) throws IOException {
        Collection<Index> esDocs = json.stream().map(doc -> {
            return new Builder(doc.toString()).build();
        }).collect(Collectors.toList());

        Bulk bulk = new Bulk.Builder()
                .defaultIndex(index)
                .defaultType(type)
                .addAction(esDocs)
                .build();

        BulkResult result = client.execute(bulk);
        System.out.println(result.isSucceeded());

        return result.getItems().size();
    }

    public boolean indexExists(String index) {
        return false;
    }

    public static void main(String[] args) throws IOException {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://localhost:9200")
                .multiThreaded(true)
                .build());

        JestClient client = factory.getObject();

        JestUtils utils = new JestUtils(client);

        String index = "audiobooks";
        String type = "book";

        utils.createIndex(index);
        utils.enableTimestamps(index, type);
        System.out.println("Index exists " + utils.indexExists("audiobooks"));
    }

}
