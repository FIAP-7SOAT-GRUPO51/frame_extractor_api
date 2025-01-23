package fiap.grupo51.fase5.frame_extractor_api.utils;

import com.google.common.base.Splitter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

public class ResourceUtils {

    public static String getContentFromResource(String resourceName) {
        try {
            InputStream stream = ResourceUtils.class.getResourceAsStream(resourceName);
            return StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> getEnv() {
        String fileEnv = getContentFromResource("/.env");
        String charEndLine = ( fileEnv.contains("\r") ) ? "\r\n" : "\n";
        return Splitter.on(charEndLine).withKeyValueSeparator("=").split(fileEnv);
    }

}
