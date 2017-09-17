package alexa.skill.request.intent;

import alexa.skill.model.es.SearchResult;
import alexa.skill.service.AudioBookService;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import utils.ESConstants;
import utils.JestUtils;

import java.util.Collections;
import java.util.List;

import static utils.ESConstants.*;

/**
 * Created by ranjiti on 11/29/16.
 */
public class PlayTitle {

    public SpeechletResponse handle(IntentRequest request, Session session) {
        String title = request.getIntent().getSlot("title").getValue();

        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://localhost:9200")
                .multiThreaded(true)
                .build());

        JestClient client = factory.getObject();

        JestUtils utils = new JestUtils(client);
        List<SearchResult> results = utils.matchQuery(TITLE, title, Lists.newArrayList(TITLE, DESCRIPTION, SECTIONS));


        SearchResult topResult = results.get(0);

        List<SearchResult.Book.Section> sections = topResult.get_source().getSections().getSectionUrls();
        Collections.sort(sections);

        SearchResult.Book.Section firstSection = sections.get(0);

        String mp3  = firstSection.getUrl();

        String path = uploadToS3(title, firstSection.getNumber(), mp3);



        return null;
    }

    private String uploadToS3(String title, int number, String fileOnInternetArchive) {
        AmazonS3Client s3Client;
        if (System.getenv("laptop") != null) {
            String access = System.getenv("AWS_ACCESS_KEY_ID");
            String secret = System.getenv("AWS_SECRET_ACCESS_KEY");

            s3Client = new AmazonS3Client(new BasicAWSCredentials(access, secret));
        }
        else
            s3Client = new AmazonS3Client();

        return null;

    }
}
