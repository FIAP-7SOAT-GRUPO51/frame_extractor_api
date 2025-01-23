package fiap.grupo51.fase5.frame_extractor_api.api.controller;

import fiap.grupo51.fase5.frame_extractor_api.api.model.RequestFrameExtractorModel;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorInput;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorUpdate;
import fiap.grupo51.fase5.frame_extractor_api.api.openapi.RequestFrameExtractorControllerOpenApi;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractorStatus;
import fiap.grupo51.fase5.frame_extractor_api.domain.service.RequestFrameExtractorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1/requestFrameExtractor", produces = MediaType.APPLICATION_JSON_VALUE)
public class RequestFrameExtractorController implements RequestFrameExtractorControllerOpenApi {

    private final RequestFrameExtractorService requestFrameExtractorService;

    public RequestFrameExtractorController(RequestFrameExtractorService requestFrameExtractorService) {
        this.requestFrameExtractorService = requestFrameExtractorService;
    }

    @GetMapping
    @Override
    public Page<RequestFrameExtractorModel> list(Pageable pageable) {
        return requestFrameExtractorService.findAll(pageable);
    }

    @GetMapping(value = "/{accessKey}")
    @Override
    public RequestFrameExtractorModel findByAccessKey(@PathVariable final String accessKey) {
        return requestFrameExtractorService.findByAccessKeyToModel(accessKey);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public RequestFrameExtractorModel add(@RequestBody @Valid RequestFrameExtractorInput requestFrameExtractorInput,
                             Authentication authentication) {
        return requestFrameExtractorService.save(requestFrameExtractorInput, authentication);
    }

    @PutMapping("/{accessKey}")
    @Override
    public RequestFrameExtractorModel update(
        @PathVariable String accessKey,
        @RequestBody @Valid RequestFrameExtractorUpdate requestFrameExtractorUpdate,
        Authentication authentication) {
        return requestFrameExtractorService.update(accessKey, requestFrameExtractorUpdate, authentication);
    }

    @DeleteMapping("/{accessKey}")
    @Override
    public void delete(@PathVariable String accessKey) {
        requestFrameExtractorService.delete(accessKey);
    }
}