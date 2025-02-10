package fiap.grupo51.fase5.frame_extractor_api.api.assembler;

import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorInput;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorUpdate;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RequestFrameExtractorDisassemblerTest {

    private RequestFrameExtractorDisassembler disassembler;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        modelMapper = mock(ModelMapper.class);
        disassembler = new RequestFrameExtractorDisassembler(modelMapper);
    }

    @Test
    public void testToDomainObject() {
        RequestFrameExtractorInput input = new RequestFrameExtractorInput();
        RequestFrameExtractor expected = new RequestFrameExtractor();

        when(modelMapper.map(input, RequestFrameExtractor.class)).thenReturn(expected);

        RequestFrameExtractor result = disassembler.toDomainObject(input);

        assertEquals(expected, result);
        verify(modelMapper).map(input, RequestFrameExtractor.class);
    }

    @Test
    public void testCopyToDomainObject_Input() {
        RequestFrameExtractorInput input = new RequestFrameExtractorInput();
        RequestFrameExtractor domainObject = new RequestFrameExtractor();

        disassembler.copyToDomainObject(input, domainObject);

        verify(modelMapper).map(input, domainObject);
    }

    @Test
    public void testCopyToDomainObject_Update() {
        RequestFrameExtractorUpdate update = new RequestFrameExtractorUpdate();
        RequestFrameExtractor domainObject = new RequestFrameExtractor();

        disassembler.copyToDomainObject(update, domainObject);

        verify(modelMapper).map(update, domainObject);
    }
}