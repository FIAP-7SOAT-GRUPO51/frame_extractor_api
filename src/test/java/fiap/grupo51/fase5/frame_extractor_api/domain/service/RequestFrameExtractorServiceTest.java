package fiap.grupo51.fase5.frame_extractor_api.domain.service;

import fiap.grupo51.fase5.frame_extractor_api.api.assembler.RequestFrameExtractorAssembler;
import fiap.grupo51.fase5.frame_extractor_api.api.assembler.RequestFrameExtractorDisassembler;
import fiap.grupo51.fase5.frame_extractor_api.api.model.RequestFrameExtractorModel;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorInput;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorUpdate;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractorStatus;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.User;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.RequestFrameExtractorRepository;
import fiap.grupo51.fase5.frame_extractor_api.utils.FrameExtractorUserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.Optional;

import static fiap.grupo51.fase5.frame_extractor_api.utils.RequestFrameExtractorHandler.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RequestFrameExtractorRequestFrameExtractorTest {

    @InjectMocks
    private RequestFrameExtractorService requestFrameExtractorService;

    @Mock
    private RequestFrameExtractorRepository requestFrameExtractorRepository;

    @Mock
    private RequestFrameExtractorAssembler requestFrameExtractorAssembler;

    @Mock
    private RequestFrameExtractorDisassembler requestFrameExtractorDisassembler;

    @Mock
    private Authentication authentication;

    @Mock
    private UserService userService;

    @Mock
    private FrameExtractorUserUtils frameExtractorUserUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        RequestFrameExtractor requestFrameExtractor = generateRequestFrameExtractor();
        Page<RequestFrameExtractor> requestFrameExtractorPage = new PageImpl<>(Collections.singletonList(requestFrameExtractor));
        RequestFrameExtractorModel requestFrameExtractorModel = new RequestFrameExtractorModel();

        when(requestFrameExtractorRepository.findAll(pageable)).thenReturn(requestFrameExtractorPage);
        when(requestFrameExtractorAssembler.toCollectionModel(any())).thenReturn(Collections.singletonList(requestFrameExtractorModel));

        Page<RequestFrameExtractorModel> result = requestFrameExtractorService.findAll(pageable);

        assertEquals(1, result.getContent().size());
        verify(requestFrameExtractorRepository, times(1)).findAll(pageable);
        verify(requestFrameExtractorAssembler, times(1)).toCollectionModel(any());
    }

    @Test
    void testFindByAccessKey() {
        var accessKey = "E4A8F533-DC7F-4766-B782-D731E14764FF";
        RequestFrameExtractor requestFrameExtractor = generateRequestFrameExtractor();
        RequestFrameExtractorModel requestFrameExtractorModel = generateRequestFrameExtractorModel();

        when(requestFrameExtractorRepository.findByAccessKey(accessKey)).thenReturn(Optional.of(requestFrameExtractor));
        when(requestFrameExtractorAssembler.toModel(requestFrameExtractor)).thenReturn(requestFrameExtractorModel);

        RequestFrameExtractorModel result = requestFrameExtractorService.findByAccessKeyToModel(accessKey);

        assertEquals(requestFrameExtractorModel, result);
        verify(requestFrameExtractorRepository, times(1)).findByAccessKey(accessKey);
        verify(requestFrameExtractorAssembler, times(1)).toModel(requestFrameExtractor);
    }

    @Test
    void testSave() {
        RequestFrameExtractorInput requestFrameExtractorInput = generateRequestFrameExtractorInput();
        RequestFrameExtractor requestFrameExtractor = generateRequestFrameExtractor();
        requestFrameExtractor.setStatus(RequestFrameExtractorStatus.EM_ABERTO);
        RequestFrameExtractorModel requestFrameExtractorModel = generateRequestFrameExtractorModel();
        User user = new User();

        when(requestFrameExtractorDisassembler.toDomainObject(requestFrameExtractorInput)).thenReturn(requestFrameExtractor);
        when(userService.findOrElseByLogin(any(String.class))).thenReturn(user);
        when(frameExtractorUserUtils.getUser(authentication)).thenReturn(user);
        when(requestFrameExtractorRepository.save(requestFrameExtractor)).thenReturn(requestFrameExtractor);
        when(requestFrameExtractorAssembler.toModel(requestFrameExtractor)).thenReturn(requestFrameExtractorModel);

        RequestFrameExtractorModel result = requestFrameExtractorService.save(requestFrameExtractorInput, authentication);

        assertEquals(requestFrameExtractorModel, result);
        verify(requestFrameExtractorDisassembler, times(1)).toDomainObject(requestFrameExtractorInput);
        verify(requestFrameExtractorRepository, times(1)).save(requestFrameExtractor);
        verify(requestFrameExtractorAssembler, times(1)).toModel(requestFrameExtractor);
    }

    @Test
    void testDelete() {

        RequestFrameExtractorModel requestFrameExtractorModel = generateRequestFrameExtractorModel();
        RequestFrameExtractor requestFrameExtractor = generateRequestFrameExtractor();
        requestFrameExtractor.setId(999L);
        requestFrameExtractor.setAccessKey(requestFrameExtractorModel.getAccessKey());
        when(requestFrameExtractorRepository.findByAccessKey(requestFrameExtractorModel.getAccessKey())).thenReturn(Optional.of(requestFrameExtractor));
        doNothing().when(requestFrameExtractorRepository).deleteById(requestFrameExtractor.getId());

        requestFrameExtractorService.delete(requestFrameExtractorModel.getAccessKey());

        verify(requestFrameExtractorRepository, times(1)).findByAccessKey(any(String.class));
        verify(requestFrameExtractorRepository, times(1)).deleteById(any(Long.class));

    }

    @Test
    void testUpdate() {
        String accessKey = "accessKey";
        RequestFrameExtractorUpdate requestFrameExtractorUpdate = generateRequestFrameExtractorUpdate();
        RequestFrameExtractor requestFrameExtractor = generateRequestFrameExtractor();
        RequestFrameExtractorModel requestFrameExtractorModel = generateRequestFrameExtractorModel();
        User user = new User();

        when(requestFrameExtractorRepository.findByAccessKey(accessKey)).thenReturn(Optional.of(requestFrameExtractor));
        doNothing().when(requestFrameExtractorDisassembler).copyToDomainObject(requestFrameExtractorUpdate, requestFrameExtractor);
        when(frameExtractorUserUtils.getUser(authentication)).thenReturn(user);
        when(requestFrameExtractorRepository.save(requestFrameExtractor)).thenReturn(requestFrameExtractor);
        when(requestFrameExtractorAssembler.toModel(requestFrameExtractor)).thenReturn(requestFrameExtractorModel);

        RequestFrameExtractorModel result = requestFrameExtractorService.update(accessKey, requestFrameExtractorUpdate, authentication);

        assertEquals(requestFrameExtractorModel, result);
        verify(requestFrameExtractorRepository, times(1)).findByAccessKey(accessKey);
        verify(requestFrameExtractorDisassembler, times(1)).copyToDomainObject(requestFrameExtractorUpdate, requestFrameExtractor);
        verify(requestFrameExtractorRepository, times(1)).save(requestFrameExtractor);
        verify(requestFrameExtractorAssembler, times(1)).toModel(requestFrameExtractor);
    }

    @Test
    void testFindByAccessKey_NotFound() {
        String accessKey = "12345678901234";

        when(requestFrameExtractorRepository.findByAccessKey(accessKey)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> requestFrameExtractorService.findByAccessKey(accessKey));
        verify(requestFrameExtractorRepository, times(1)).findByAccessKey(accessKey);
    }

    @Test
    void testUpdate_NotFound() {
        String accessKey = "accessKey";
        RequestFrameExtractorUpdate requestFrameExtractorUpdate = generateRequestFrameExtractorUpdate();

        when(requestFrameExtractorRepository.findByAccessKey(accessKey)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> requestFrameExtractorService.update(accessKey, requestFrameExtractorUpdate, authentication));
        verify(requestFrameExtractorRepository, times(1)).findByAccessKey(accessKey);
    }

}
