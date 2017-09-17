package utils;

import sun.reflect.annotation.ExceptionProxy;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by ranjiti on 1/26/16.
 */
public class SectionMp3Encoder {

    public String encode(String inputFilePath) throws Exception {
        Path inputPath = Paths.get(inputFilePath);
        String outputPath = inputPath.toString().replace("64kb", "48kb");

        ProcessBuilder procbuilder = new ProcessBuilder();
        procbuilder = procbuilder.directory(new File("/Users/ranjiti/work/ffmpg"));
        procbuilder = procbuilder.inheritIO();
        procbuilder = procbuilder.command(new String [] {
                "/Users/ranjiti/work/ffmpg/ffmpeg",
                "-i",
                inputFilePath,
                "-ac",
                "2",
                "-codec:a",
                "libmp3lame",
                "-b:a",
                "48k",
                "-ar",
                "16000",
                outputPath
        });

        Process proc = procbuilder.start();
        proc.waitFor();

        return outputPath;
    }


    public static void main(String[] args) throws Exception {
        String input = "/tmp/Stoicism/06_-_Chapter_VI_-_Conclusion/stoicism_06_stock_64kb.mp3";
        new SectionMp3Encoder().encode(input);
    }
}
