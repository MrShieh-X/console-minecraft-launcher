import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Appender {
    public static void main(String[] args) throws IOException {
        try (FileOutputStream a = new FileOutputStream(args[0], true)) {
            a.write(toByteArray(Files.newInputStream(Paths.get(args[1]))));
        }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
