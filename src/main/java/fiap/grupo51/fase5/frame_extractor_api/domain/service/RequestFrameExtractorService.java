package fiap.grupo51.fase5.frame_extractor_api.domain.service;

import fiap.grupo51.fase5.frame_extractor_api.api.assembler.RequestFrameExtractorAssembler;
import fiap.grupo51.fase5.frame_extractor_api.api.assembler.RequestFrameExtractorDisassembler;
import fiap.grupo51.fase5.frame_extractor_api.api.model.RequestFrameExtractorModel;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorInput;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorUpdate;
import fiap.grupo51.fase5.frame_extractor_api.domain.exception.EntityInUseException;
import fiap.grupo51.fase5.frame_extractor_api.domain.exception.RequestFrameExtractorNotFindException;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractorStatus;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.User;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.RequestFrameExtractorRepository;
import fiap.grupo51.fase5.frame_extractor_api.utils.FrameExtractorUserUtils;
import fiap.grupo51.fase5.frame_extractor_api.utils.MessageProperty;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestFrameExtractorService {

    private final RequestFrameExtractorRepository requestFrameExtractorRepository;
    private final RequestFrameExtractorAssembler requestFrameExtractorAssembler;
    private final RequestFrameExtractorDisassembler requestFrameExtractorDisassembler;
    private final FrameExtractorUserUtils frameExtractorUserUtils;


    @MessageProperty("request.frame.extractor.not-found")
    private String requestFrameExtractorNotFound;

    @MessageProperty("data-integrity.violation")
    private String dataIntegrityViolation;

    public RequestFrameExtractorService(
            RequestFrameExtractorRepository requestFrameExtractorRepository,
            RequestFrameExtractorAssembler requestFrameExtractorAssembler,
            RequestFrameExtractorDisassembler requestFrameExtractorDisassembler,
            FrameExtractorUserUtils frameExtractorUserUtils) {

        this.requestFrameExtractorRepository = requestFrameExtractorRepository;
        this.requestFrameExtractorAssembler = requestFrameExtractorAssembler;
        this.requestFrameExtractorDisassembler = requestFrameExtractorDisassembler;
        this.frameExtractorUserUtils = frameExtractorUserUtils;

    }

    public Page<RequestFrameExtractorModel> findAll(Pageable pageable) {
        Page<RequestFrameExtractor> requestFrameExtractors = requestFrameExtractorRepository.findAll(pageable);
        List<RequestFrameExtractorModel> requestFrameExtractorModelList = requestFrameExtractorAssembler.toCollectionModel(requestFrameExtractors.getContent());
        return new PageImpl<>(requestFrameExtractorModelList, pageable,requestFrameExtractors.getTotalElements());
    }

    public RequestFrameExtractorModel save(RequestFrameExtractorInput requestFrameExtractorInput, Authentication authentication) {
        RequestFrameExtractor requestFrameExtractor = requestFrameExtractorDisassembler.toDomainObject(requestFrameExtractorInput);
        requestFrameExtractor.setStatus(RequestFrameExtractorStatus.EM_ABERTO);
        User user = frameExtractorUserUtils.getUser(authentication);
        requestFrameExtractor.setUserInsert(user);
        return upsert(requestFrameExtractor);
    }

    public RequestFrameExtractorModel update(String accessKey, RequestFrameExtractorUpdate requestFrameExtractorUpdate, Authentication authentication) {
        RequestFrameExtractor currentRequestFrameExtractor = requestFrameExtractorRepository.findByAccessKey(accessKey)
                .orElseThrow(() -> new RequestFrameExtractorNotFindException(requestFrameExtractorNotFound));

        requestFrameExtractorDisassembler.copyToDomainObject(requestFrameExtractorUpdate, currentRequestFrameExtractor);
        currentRequestFrameExtractor.setAccessKey(accessKey);

        User user = frameExtractorUserUtils.getUser(authentication);
        currentRequestFrameExtractor.setUserUpdate(user);

        return upsert(currentRequestFrameExtractor);
    }

    public void delete(String accessKey) {
        RequestFrameExtractor requestFrameExtractor = findByAccessKey(accessKey);
        delete(requestFrameExtractor.getId());
    }

    public RequestFrameExtractorModel findByAccessKeyToModel(String accessKey) {
        return requestFrameExtractorAssembler.toModel(findByAccessKey(accessKey));
    }

    public RequestFrameExtractor findByAccessKey(String accessKey) {
        return requestFrameExtractorRepository.findByAccessKey(accessKey)
                .orElseThrow(() -> new RequestFrameExtractorNotFindException(requestFrameExtractorNotFound));
    }

    @Transactional
    private RequestFrameExtractorModel upsert(RequestFrameExtractor frameExtractor) {
        RequestFrameExtractor requestFrameExtractorSaved = requestFrameExtractorRepository.save(frameExtractor);
        return requestFrameExtractorAssembler.toModel(requestFrameExtractorSaved);
    }

    @Transactional
    private void delete(Long requestFrameExtractorId) {
        try {
            requestFrameExtractorRepository.deleteById(requestFrameExtractorId);
            requestFrameExtractorRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new RequestFrameExtractorNotFindException(String.valueOf(requestFrameExtractorId));

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(
                    String.format(dataIntegrityViolation, requestFrameExtractorId));
        }
    }

}
