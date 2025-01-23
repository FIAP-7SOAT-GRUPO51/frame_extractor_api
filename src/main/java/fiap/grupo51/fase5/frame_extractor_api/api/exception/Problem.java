package fiap.grupo51.fase5.frame_extractor_api.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(name = "ProblemException", description = "Object Default Problem API")
@JsonInclude(Include.NON_NULL)
@Getter
@Builder
public class Problem {

    @Schema(example = "400")
    private Integer status;

    @Schema(example = "2019-12-01T18:09:02.70844Z")
    private OffsetDateTime timestamp;

    @Schema(example = "https://ds2u.com.br/dados-invalidos")
    private String type;

    @Schema(example = "Dados inválidos")
    private String title;

    @Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
    private String detail;

    @Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
    private String userMessage;

    @Schema(description = "Lista de objetos ou campos que geraram o erro (opcional)")
    private List<Object> objects;

    @Getter
    @Builder
    @Schema(name = "ObjectProblem", description = "Detail Attribute Exception")
    public static class Object {

        @Schema(example = "description")
        private String name;

        @Schema(example = "Descrição é obrigatória")
        private String userMessage;

    }
}