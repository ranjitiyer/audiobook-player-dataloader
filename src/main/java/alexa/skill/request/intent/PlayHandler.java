package alexa.skill.request.intent;

import alexa.skill.service.AudioBookService;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.google.inject.Inject;

import java.util.Map;

/**
 * Created by ranjiti on 12/27/15.
 */
public class PlayHandler {
    @Inject
    AudioBookService service;

    public SpeechletResponse handle(IntentRequest request, Session session) {
        String title = request.getIntent().getSlot("title").getValue();

        String audioBookUrl = "https://s3.amazonaws.com/alexa-audio-books/01/01/lettersofapostimpressionist_01_vangogh_64kb.mp3";

        String ssmlText = String.format("<speak>\n"
                + "Playing audio book %s. <audio src=\"%s\" /> " +
                "</speak>\n", title, audioBookUrl);

        // Speech response
        String speechOutput = "<speak>"
                + "Playing audio book "
                + "<audio src='https://s3.amazonaws.com/ask-storage/tidePooler/OceanWaves.mp3'/>"
                + "</speak>";

        SsmlOutputSpeech ssml = new SsmlOutputSpeech();
        ssml.setSsml(ssmlText);

        SpeechletResponse response = new SpeechletResponse();
        response.setOutputSpeech(ssml);
        response.setShouldEndSession(false);

        return response;
    }
}
