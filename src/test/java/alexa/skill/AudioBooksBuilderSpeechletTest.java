package alexa.skill;

import alexa.skill.inject.AudioBooksModule;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by ranjiti on 1/9/16.
 */

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(Intent.class)
public class AudioBooksBuilderSpeechletTest {

    @Test
    public void testOnSessionStarted() throws Exception {

    }

    @Test
    public void testOnLaunch() throws Exception {

    }

    @Test
    public void testOnIntent() throws Exception {
//        final File f = new File(AudioBooksBuilderSpeechletTest.class.getProtectionDomain().getCodeSource().getLocation().getPath());
//        System.out.println(f);

//        Injector injector = Guice.createInjector(new AudioBooksModule());
//
//        AudioBooksBuilderSpeechlet speechlet =
//                injector.getInstance(AudioBooksBuilderSpeechlet.class);
//        System.out.println(speechlet);
//
//        speechlet.onIntent(null,null);

//        Intent intent = PowerMockito.mock(Intent.class);
//        when(intent.getName()).thenReturn("RandomPlayIntent");
//
//        IntentRequest intentRequest = mock(IntentRequest.class);
//        when(intentRequest.getIntent()).thenReturn(intent);

        //speechlet.onIntent(intentRequest,null);
    }

    @Test
    public void testOnSessionEnded() throws Exception {

    }

}