package com.ecommerce.back.util;

import java.io.IOException;
import java.io.Writer;

public class IOUtil {
    public static void writeMessage(Writer writer, String message) throws IOException {
        writer.write(message);
        writer.flush();
        writer.close();
    }
}
