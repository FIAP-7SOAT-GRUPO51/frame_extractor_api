package fiap.grupo51.fase5.frame_extractor_api.api.openapi;

import fiap.grupo51.fase5.frame_extractor_api.api.model.PageableCustom;
import fiap.grupo51.fase5.frame_extractor_api.api.model.RequestFrameExtractorModel;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorInput;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorUpdate;
import fiap.grupo51.fase5.frame_extractor_api.api.openapi.model.PageableParameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

@Tag(name = "Solicitação de Processamento", description = "Gerencia solicitações de processamento de videos")
public interface RequestFrameExtractorControllerOpenApi {

    @Operation(summary = "Lista solicitações com paginação")
    @PageableParameter
    PageableCustom<RequestFrameExtractorModel> list(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Encontra solicitação por Chave de Acesso")
    RequestFrameExtractorModel findByAccessKey(
            @Parameter(description = "Chave de acesso do serviço", required = true)
            String acessKey);

    @Operation(summary = "Inclui solicitações")
    RequestFrameExtractorModel add(
            @Parameter(description = "Informações para inclusão de solicitações", required = true)
            RequestFrameExtractorInput requestFrameExtractorInput,
            @Parameter(hidden = true)
            Authentication authentication);

    @Operation(summary = "Atualiza solicitações")
    RequestFrameExtractorModel update(
            @Parameter(description = "Chave de acesso da solicitação", required = true)
            String accessKey,
            @RequestBody(description = "Informações para edição da solicitação", required = true)
            RequestFrameExtractorUpdate requestFrameExtractorUpdate,
            @Parameter(hidden = true)
            Authentication authentication);

    @Operation(summary = "Atualiza status para [Em Aberto]")
    RequestFrameExtractorModel setStatusOpen(
            @Parameter(description = "Chave de acesso da solicitação", required = true)
            String accessKey,
            @Parameter(hidden = true)
            Authentication authentication);

    @Operation(summary = "Atualiza status para [Em Processamento]")
    RequestFrameExtractorModel setStatusInProgress(
            @Parameter(description = "Chave de acesso da solicitação", required = true)
            String accessKey,
            @Parameter(hidden = true)
            Authentication authentication);

    @Operation(summary = "Atualiza status para [Concluído]")
    RequestFrameExtractorModel setStatusDone(
            @Parameter(description = "Chave de acesso da solicitação", required = true)
            String accessKey,
            @Parameter(hidden = true)
            Authentication authentication);

    @Operation(summary = "Atualiza status para [Falha]")
    RequestFrameExtractorModel setStatusFail(
            @Parameter(description = "Chave de acesso da solicitação", required = true)
            String accessKey,
            @Parameter(hidden = true)
            Authentication authentication);

    @Operation(summary = "Exclui solicitação")
    void delete(
            @Parameter(description = "Chave de acesso da solicitação", required = true)
            String accessKey);

}