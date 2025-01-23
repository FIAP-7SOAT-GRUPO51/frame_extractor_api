package fiap.grupo51.fase5.frame_extractor_api.api.assembler;

import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorInput;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorUpdate;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RequestFrameExtractorDisassembler {

    private final ModelMapper modelMapper;

    public RequestFrameExtractorDisassembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public RequestFrameExtractor toDomainObject(RequestFrameExtractorInput requestFrameExtractorInput) {
        return modelMapper.map(requestFrameExtractorInput, RequestFrameExtractor.class);
    }

    public void copyToDomainObject(RequestFrameExtractorInput requestFrameExtractorInput, RequestFrameExtractor requestFrameExtractor) {
        modelMapper.map(requestFrameExtractorInput, requestFrameExtractor);
    }

    public void copyToDomainObject(RequestFrameExtractorUpdate requestFrameExtractorUpdate, RequestFrameExtractor requestFrameExtractor) {
        modelMapper.map(requestFrameExtractorUpdate, requestFrameExtractor);
    }
    
}