package fiap.grupo51.fase5.frame_extractor_api.domain.model;

import fiap.grupo51.fase5.frame_extractor_api.utils.FrameExtractorUtils;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "user_app")
public class User {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String email;
	private String cpf;
	private String login;
	private String password;
	private int active;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_users_group", joinColumns = @JoinColumn(name = "user_id")
			, inverseJoinColumns = @JoinColumn(name = "users_group_id"))
	private List<UsersGroup> usersGroups;

	@Column(name = "access_key")
	private String accessKey;

	@PrePersist
    protected void prePersist() {
		generateAccessKey();
	}

	public void generateAccessKey() {

		if (this.accessKey == null)
			setAccessKey(FrameExtractorUtils.getNewAccessKey());
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		this.getUsersGroups().forEach(g -> {
			g.getPermissions().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getDescription().toUpperCase())));
		});
		return authorities;
	}

	public void encryptPassWord() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		this.setPassword( encoder.encode( this.getPassword() ) );
	}

}