package fiap.grupo51.fase5.frame_extractor_api.utils;


import fiap.grupo51.fase5.frame_extractor_api.api.model.RequestFrameExtractorModel;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorInput;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorUpdate;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;

public class RequestFrameExtractorHandler {

    public static RequestFrameExtractor generateRequestFrameExtractor() {
        return RequestFrameExtractor.builder()
                .description("TST DESCRICAO")
                .fileName("tst.mp4")
                .email("teste@gmail.com")
                .fps(1)
                .build();
    }

    public static RequestFrameExtractorModel generateRequestFrameExtractorModel() {
        return RequestFrameExtractorModel.builder()
                .description("TST DESCRICAO")
                .fileName("tst.mp4")
                .email("teste@gmail.com")
                .fps(1)
                .accessKey("E4A8F533-DC7F-4766-B782-D731E14764FC")
                .build();
    }

    public static RequestFrameExtractorInput generateRequestFrameExtractorInput() {
        return RequestFrameExtractorInput.builder()
                .description("TST FULANO")
                .fileName("tst.mp4")
                .email("teste@gmail.com")
                .fps(1)
                .build();
    }

    public static RequestFrameExtractorUpdate generateRequestFrameExtractorUpdate() {
        return RequestFrameExtractorUpdate.builder()
                .description("TST FULANO UPDATE")
                .fileName("tst.mp4")
                .email("teste@gmail.com")
                .fps(1)
                .build();
    }
}
