package alexa.skill.inject;

import alexa.skill.AudioBooksBuilderSpeechlet;
import alexa.skill.dao.AudioBook;
import alexa.skill.request.intent.RandomBookPlayHandler;
import alexa.skill.service.AudioBookService;
import alexa.skill.service.LibrivoxAudioService;
import alexa.skill.util.DirUtils;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by ranjiti on 12/27/15.
 */
public class AudioBooksModule extends AbstractModule {
    @Provides
    @Singleton
    public OPartitionedDatabasePool provideOPartitionedDatabasePool()
    {
        String databaseURL = "plocal:" + "/tmp/" + "audiobooksmd";
        String username = "admin";
        String password = "admin";

        String base = AudioBooksModule.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            FileUtils.forceMkdir(new File("/tmp/audiobooksmd"));
            DirUtils.copy(Paths.get(base, "audiobooksmd"), Paths.get("/tmp/audiobooksmd"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new OPartitionedDatabasePool(databaseURL,username,password);
    }

    @Provides
    @Singleton
    public AudioBookService provideAudioBookService() {
        return new LibrivoxAudioService();
    }

    @Override
    protected void configure() {
        bind(AudioBooksBuilderSpeechlet.class);
        bind(RandomBookPlayHandler.class);
    }
}
