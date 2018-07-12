package ch.prevo.open.node.data.provider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Helper to load file from classpath or file system.
 */
public class ResourceLoader {

    public static final String CLASSPATH_PREFIX = "classpath:";

    /**
     * Read a file either from the classpath (needs to be prefixed with {@value #CLASSPATH_PREFIX})
     * or from the file system if an absolute path is provided.
     *
     * @param filePath The path to the file
     * @return An input stream to the file
     * @throws IOException
     */
    public InputStream getResource(String filePath) throws IOException {
        if (filePath.startsWith(CLASSPATH_PREFIX)) {
            String fileName = filePath.replaceFirst(CLASSPATH_PREFIX, "");
            return ResourceLoader.class.getClassLoader().getResourceAsStream(fileName);
        }
        Path path = Paths.get(filePath);
        return Files.newInputStream(path);
    }
}
