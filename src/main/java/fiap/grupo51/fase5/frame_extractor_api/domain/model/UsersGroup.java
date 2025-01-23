package fiap.grupo51.fase5.frame_extractor_api.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper=false)
@Entity(name = "users_group")
public class UsersGroup extends Audit {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_group_permissions", joinColumns = @JoinColumn(name = "users_group_id")
            , inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions;

}
