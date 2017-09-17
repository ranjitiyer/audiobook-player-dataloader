package utils;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created by ranjiti on 1/26/16.
 */
public class SectionMp3Uploader {

    private String accessKey = "DRFxoSxmOEwMfks7";
    private String secretKey = "bJ8ZAavmUUwOf4ul";

    public SectionMp3Uploader() {
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String uploadToInternetArchive(String bookTitle,
                                        String normalizedSectionTitle,
                                        String localPathTo48KbEncodedMp3) throws Exception {

        String filename = Paths.get(localPathTo48KbEncodedMp3).getFileName().toString();

        String ia_url = String.format("https://s3.us.archive.org/%s/%s/%s",bookTitle,normalizedSectionTitle,filename);
        System.out.println(ia_url);

        ProcessBuilder procbuilder = new ProcessBuilder();
        procbuilder = procbuilder.directory(new File("/Users/ranjiti/work/ffmpg"));
        procbuilder = procbuilder.inheritIO();
        procbuilder = procbuilder.command(new String [] {
                "curl",
                "--location",
                "--header",
                "'x-amz-auto-make-bucket:1'",
                "--header",
                "'x-archive-meta-mediatype:audio'",
                "--header",
                "'x-archive-meta-title:" + normalizedSectionTitle + "'",
                "--header",
                String.format("\"authorization: LOW %s:%s\"",accessKey,secretKey),
                "--upload-file",
                localPathTo48KbEncodedMp3,
                ia_url
        });

        System.out.println(procbuilder.command());

        Process proc = procbuilder.start();
        proc.waitFor();

        return ia_url;
    }

    public String uploadToGoogleCloudDrive(String bookTitle,
                                           String normalizedSectionTitle,
                                           String localPathTo48kbEncodedMp3) throws Exception {
        return null;
    }
}
