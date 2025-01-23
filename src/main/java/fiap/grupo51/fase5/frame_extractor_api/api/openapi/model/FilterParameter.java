package fiap.grupo51.fase5.frame_extractor_api.api.openapi.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        in = ParameterIn.QUERY,
        name = "accessKey",
        description = "Chave de acesso",
        schema = @Schema(type = "string", defaultValue = "")
)
@Parameter(
        in = ParameterIn.QUERY,
        name = "code",
        description = "Código",
        schema = @Schema(type = "string", defaultValue = "")
)
@Parameter(
        in = ParameterIn.QUERY,
        name = "description",
        description = "Descricao",
        schema = @Schema(type = "string", defaultValue = "")
)
public @interface FilterParameter {
}