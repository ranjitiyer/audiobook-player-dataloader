package alexa.skill.request.intent;

import alexa.skill.AudioBooksBuilderSpeechlet;
import alexa.skill.inject.AudioBooksModule;
import alexa.skill.service.AudioBookService;
import alexa.skill.service.LibrivoxAudioService;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.*;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.apache.commons.lang3.RandomUtils;
import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;

import java.util.List;

/**
 * Created by ranjiti on 1/9/16.
 */
public class RandomBookPlayHandler {

    public RandomBookPlayHandler() {}

    @Inject
    OPartitionedDatabasePool dbPool;

    public SpeechletResponse handle(IntentRequest request, Session session) {

/*
        ODatabaseDocumentTx conn = dbPool.acquire();

        String sql = String.format("select * from Book where id = %s", 1000);

        List<Object> resultSet = conn.query(new OSQLSynchQuery<ODocument>(sql));

        Object res = resultSet.get(0);

        System.out.printf("res " + res);
*/

        //String audioBookUrl = "https://s3.amazonaws.com/alexa-audio-books/1000/01/childrhymes_01_riley_64kb.mp3";

        //String audioBookUrl = "https://ia802604.us.archive.org/32/items/moby_dick_librivox/mobydick_000_melville_64kb.mp3";
        String audioBookUrl = "https://s3.amazonaws.com/ranjitiyer-alexa-skills/mobydick_000_melville_48kb.mp3";

        String title = "Selected Riley Child Rhymes";

        //String ssmlText = String.format("<speak>Playing title %s. <audio src=\"%s\"/> </speak>\n", title, audioBookUrl);
        //String ssmlText = "<speak>Playing title. <audio src=\"https://s3.amazonaws.com/ranjitiyer-alexa-skills/mobydick_000_melville_48kb.mp3\"/> </speak>";

/*
        // Speech response
        String ssmlText = "<speak>"
                + "Playing Selected Riley Child Rhymes "
                + "<audio src='https://s3.amazonaws.com/ranjitiyer-alexa-skills/mobydick_000_melville_48kb.mp3'/>"
                + "</speak>";
*/


        // Speech response
        String ssmlText = "<speak>"
                + "Playing Selected Riley Child Rhymes from Google drive"
                + "<audio src='https://s3.amazonaws.com/ranjitiyer-alexa-skills/mobydick_000_melville_48kb.mp3'/>"
                + "</speak>";


/*
        String ssmlText = "<speak>"
                + "Playing title. "
                + "<audio src=\"https://s3.amazonaws.com/ask-storage/tidePooler/OceanWaves.mp3\"/>"
                + "</speak>";
*/



        SsmlOutputSpeech ssml = new SsmlOutputSpeech();
        ssml.setSsml(ssmlText);

//        SpeechletResponse response = new SpeechletResponse();
//        response.setOutputSpeech(ssml);
//        response.setShouldEndSession(false);

        Reprompt reprompt = new Reprompt();
        PlainTextOutputSpeech prompt = new PlainTextOutputSpeech();
        prompt.setText("Playing a book");
        reprompt.setOutputSpeech(prompt);

        return SpeechletResponse.newAskResponse(ssml, reprompt);
    }

/*
    public static void main(String[] args) {
        class TestModule extends AbstractModule {
            @Provides
            public OPartitionedDatabasePool provideOPartitionedDatabasePool() {
                String databaseURL = "plocal:/Users/ranjiti/work/alexa/AudioBooks/resources/audiobooksmd";
                String username = "admin";
                String password = "admin";
                return new OPartitionedDatabasePool(databaseURL,username,password);
            }

            @Override
            protected void configure() {
                bind(RandomBookPlayHandler.class);
            }
        }

        Injector injector = Guice.createInjector(new TestModule());

        RandomBookPlayHandler handler = injector.getInstance(RandomBookPlayHandler.class);

        handler.handle(null,null);
    }
*/

}
