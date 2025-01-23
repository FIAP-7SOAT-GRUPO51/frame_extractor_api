package fiap.grupo51.fase5.frame_extractor_api.core.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.key.path}")
    private String jwtKeyPath;

    @Value("${jwt.key.name-private}")
    private String jwtKeyNamePrivate;

    @Value("${jwt.key.name-public}")
    private String jwtKeyNamePublic;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(
                auth -> auth.requestMatchers(HttpMethod.POST,"/v1/authenticate").permitAll()
                        .requestMatchers("*", "/socket/**").permitAll()
                        .requestMatchers("*","/swagger-ui/**").permitAll()
                        .requestMatchers("*","/v3/api-docs/**").permitAll()
                        .requestMatchers("*","/docs/**").permitAll()
                        .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .oauth2ResourceServer(
                conf -> conf.jwt(Customizer.withDefaults())
            );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {

        File publicKeyFile = null;
        try {
            if (jwtKeyPath.contains("classpath")) {
                var fileJwtKeyPublic = jwtKeyPath + ":" + jwtKeyNamePublic;

                fileJwtKeyPublic = fileJwtKeyPublic.replace("::", ":");

                publicKeyFile = ResourceUtils.getFile(fileJwtKeyPublic);
            }
            else {
                publicKeyFile = new File(jwtKeyPath + "/" + jwtKeyNamePublic);
            }
            String publicKeyContent = FileUtils.readFileToString(publicKeyFile, StandardCharsets.UTF_8);
            RSAPublicKey rsaPublicKey = (RSAPublicKey) PemUtils.readPublicKey(publicKeyContent);
            return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key from: " + jwtKeyPath+"/"+jwtKeyNamePublic, e);
        }
    }

    @Bean
    JwtEncoder jwtEncoder() {

        File publicKeyFile = null;
        File privateKeyFile = null;
        try {

            if (jwtKeyPath.contains("classpath")) {
                //jwtKeyPath = jwtKeyPath.replace("classpath:", "src/main/resources/");
                var fileJwtKeyPrivate = jwtKeyPath + ":" + jwtKeyNamePrivate;
                var fileJwtKeyPublic = jwtKeyPath + ":" + jwtKeyNamePublic;

                fileJwtKeyPrivate = fileJwtKeyPrivate.replace("::", ":");
                fileJwtKeyPublic = fileJwtKeyPublic.replace("::", ":");

                publicKeyFile = ResourceUtils.getFile(fileJwtKeyPublic);
                privateKeyFile = ResourceUtils.getFile(fileJwtKeyPrivate);

            }
            else {
                publicKeyFile = new File(jwtKeyPath + "/" + jwtKeyNamePublic);
                privateKeyFile = new File(jwtKeyPath + "/" + jwtKeyNamePrivate);
            }

            String publicKeyContent = FileUtils.readFileToString(publicKeyFile, StandardCharsets.UTF_8);
            String privateKeyContent = FileUtils.readFileToString(privateKeyFile, StandardCharsets.UTF_8);

            RSAPublicKey rsaPublicKey = (RSAPublicKey) PemUtils.readPublicKey(publicKeyContent);
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) PemUtils.readPrivateKey(privateKeyContent);

            JWK jwk = new RSAKey.Builder(rsaPublicKey)
                    .privateKey(rsaPrivateKey)
                    .build();



            JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));

            return new NimbusJwtEncoder(jwkSource);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load keys. Public key path: " + jwtKeyPath+"/"+jwtKeyNamePublic +
                    ", Private key path: " + jwtKeyPath+"/"+jwtKeyNamePrivate, e);
        }
    }

    private static class PemUtils {
        private static PublicKey readPublicKey(String key) throws Exception {
            String publicKeyPEM = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return keyFactory.generatePublic(keySpec);
        }

        private static PrivateKey readPrivateKey(String key) throws Exception {
            String privateKeyPEM = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return keyFactory.generatePrivate(keySpec);
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
