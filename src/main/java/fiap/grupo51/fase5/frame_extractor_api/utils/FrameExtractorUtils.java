package fiap.grupo51.fase5.frame_extractor_api.utils;

import java.util.UUID;

public class FrameExtractorUtils {

    public static String getNewAccessKey() {
        return UUID.randomUUID().toString().toUpperCase();
    }

}
