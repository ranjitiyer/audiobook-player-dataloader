package alexa.skill;

import alexa.skill.model.Book;
import alexa.skill.request.intent.ListHandler;
import alexa.skill.request.intent.PlayHandler;
import alexa.skill.request.intent.RandomBookPlayHandler;
import alexa.skill.request.intent.SkipHandler;
import alexa.skill.request.lifecycle.LaunchHandler;
import alexa.skill.request.lifecycle.StopHandler;
import alexa.skill.service.AudioBookService;
import com.amazon.speech.speechlet.*;
import com.google.inject.Inject;

import java.util.Map;

/**
 * Created by ranjiti on 12/27/15.
 */
public class AudioBooksBuilderSpeechlet implements Speechlet {
    LaunchHandler launchHandler;
    StopHandler stopHandler;
    ListHandler listHandler;
    PlayHandler playHandler;
    SkipHandler skipHandler;
    RandomBookPlayHandler randomBookPlayHandler;

    @Inject
    public AudioBooksBuilderSpeechlet(LaunchHandler launchHandler,
                                      StopHandler stopHandler,
                                      ListHandler listHandler,
                                      PlayHandler playHandler,
                                      SkipHandler skipHandler,
                                      RandomBookPlayHandler randomBookPlayHandler) {
        this.launchHandler = launchHandler;
        this.stopHandler = stopHandler;
        this.listHandler = listHandler;
        this.playHandler = playHandler;
        this.skipHandler = skipHandler;
        this.randomBookPlayHandler = randomBookPlayHandler;
    }


    enum Commands {
        RandomPlayIntent,
        PlayIntent,
        SkipIntent,
        ResumeIntent,
        ListIntent
    }

    @Override
    public void onSessionStarted(SessionStartedRequest sessionStartedRequest, Session session) throws SpeechletException {

    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest launchRequest, Session session) throws SpeechletException {
        return launchHandler.getResponse(launchRequest,session);
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest intentRequest, Session session) throws SpeechletException {
        switch (Commands.valueOf(intentRequest.getIntent().getName())) {
            /* Play a random audio book */
            case RandomPlayIntent: {
                System.out.println("Playing Random book");
                return randomBookPlayHandler.handle(intentRequest,session);
            }
            case PlayIntent: {
                return playHandler.handle(intentRequest,session);
            }
            default : {

            }
        }
        return null;
    }

    @Override
    public void onSessionEnded(SessionEndedRequest sessionEndedRequest, Session session) throws SpeechletException {

    }
}
