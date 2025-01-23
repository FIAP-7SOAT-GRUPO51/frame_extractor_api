package fiap.grupo51.fase5.frame_extractor_api.api.assembler;

import fiap.grupo51.fase5.frame_extractor_api.api.model.RequestFrameExtractorModel;
import fiap.grupo51.fase5.frame_extractor_api.core.modelmapper.ModelMapperConfig;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestFrameExtractorAssembler {

    private final ModelMapperConfig modelMapperConfig = new ModelMapperConfig();
    private final ModelMapper modelMapper = modelMapperConfig.modelMapper();

    public RequestFrameExtractorModel toModel(RequestFrameExtractor requestFrameExtractor) {
        return modelMapper.map(requestFrameExtractor, RequestFrameExtractorModel.class);
    }

    public List<RequestFrameExtractorModel> toCollectionModel(Collection<RequestFrameExtractor> customers) {
        return customers.stream().map(this::toModel).collect(Collectors.toList());
    }

}