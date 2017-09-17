package alexa.skill.util;


import com.google.common.base.Function;
import com.google.common.base.Predicate;

import java.io.IOException;
import java.nio.file.*;
import java.util.EnumSet;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 1/22/12
 * Time: 12:46 PM
 */

public class DirUtils {

    private DirUtils() {
    }


    /**
     * Copies a directory tree
     *
     * @param from
     * @param to
     * @throws IOException
     */
    public static void copy(Path from, Path to) throws IOException {
        validate(from);
        Files.walkFileTree(from, EnumSet.of(FileVisitOption.FOLLOW_LINKS),Integer.MAX_VALUE,new CopyDirVisitor(from, to));
    }


    private static void validate(Path... paths) {
        for (Path path : paths) {
            Objects.requireNonNull(path);
            if (!Files.isDirectory(path)) {
                throw new IllegalArgumentException(String.format("%s is not a directory", path.toString()));
            }
        }
    }


}