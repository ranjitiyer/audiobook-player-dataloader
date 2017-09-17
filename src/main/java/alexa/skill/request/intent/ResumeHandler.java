package alexa.skill.request.intent;

import alexa.skill.service.AudioBookService;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.google.inject.Inject;

/**
 * Created by ranjiti on 12/27/15.
 */
public class ResumeHandler {
    public SpeechletResponse handle(IntentRequest request, Session session) {
        // use an id to retrieve state

        // pull down the section mp3 file

        // seek to offset when last stopped, keep the original

        // encode the section

        // upload the new sub section

        // send the play directive for the sub-section

        // update state

        return null;
    }
}
