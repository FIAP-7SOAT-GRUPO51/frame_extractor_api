package fiap.grupo51.fase5.frame_extractor_api.api.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserModel {
    private String login;
    private String name;
}
