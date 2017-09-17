package alexa.skill.request.lifecycle;

import alexa.skill.service.AudioBookService;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.interfaces.audioplayer.AudioItem;
import com.amazon.speech.speechlet.interfaces.audioplayer.PlayBehavior;
import com.amazon.speech.speechlet.interfaces.audioplayer.Stream;
import com.amazon.speech.speechlet.interfaces.audioplayer.directive.PlayDirective;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.apache.http.impl.client.HttpClients;

import java.util.UUID;

/**
 * Created by ranjiti on 12/27/15.
 */
public class LaunchHandler {
    @Inject
    AudioBookService service;

    public SpeechletResponse getResponse(LaunchRequest request, Session session) {
        String speechText = "Ready to play Audio Books";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Audio Books started");
        card.setContent(speechText);

        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText("Playing section 4 of gone to earth");

        String mp3 = "https://s3.amazonaws.com/alexa-audio-books/gone_to_earth/04/gonetoearth_04_webb_64kb.mp3";

        PlayDirective playDirective = new PlayDirective();
        playDirective.setPlayBehavior(PlayBehavior.REPLACE_ALL);

        Stream stream = new Stream();
        stream.setUrl(mp3);
        stream.setOffsetInMilliseconds(0);
        stream.setToken(UUID.randomUUID().toString());

        AudioItem item = new AudioItem();
        item.setStream(stream);

        playDirective.setAudioItem(item);

        SpeechletResponse speechletResponse = new SpeechletResponse();
        speechletResponse.setOutputSpeech(outputSpeech);
        speechletResponse.setDirectives(Lists.newArrayList(playDirective));
        speechletResponse.setCard(card);
        speechletResponse.setOutputSpeech(new PlainTextOutputSpeech());
        speechletResponse.setShouldEndSession(false);

        return speechletResponse;
    }
}
