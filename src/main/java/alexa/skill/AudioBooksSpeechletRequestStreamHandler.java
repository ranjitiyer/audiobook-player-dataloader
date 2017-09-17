package alexa.skill;

import alexa.skill.inject.AudioBooksModule;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ranjiti on 12/27/15.
 */
public class AudioBooksSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
        supportedApplicationIds.add("amzn1.echo-sdk-ams.app.c8a39a5b-57e9-4d4b-8497-a85cef253e40");
    }

    public AudioBooksSpeechletRequestStreamHandler() {
        super(Guice.createInjector(new AudioBooksModule()).getInstance(AudioBooksBuilderSpeechlet.class),
                supportedApplicationIds);
    }
}
