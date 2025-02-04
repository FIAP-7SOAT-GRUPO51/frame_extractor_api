package fiap.grupo51.fase5.frame_extractor_api.domain.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractorStatus;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.User;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.BucketFileRepository;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BucketFileService {
    private final BucketFileRepository bucketFileRepository;
    private final AmazonS3 s3Client;

    @Autowired
    private UserRepository userRepository;

    @Value("${aws.s3BucketName}")
    private String bucketName;


    @Autowired
    public BucketFileService(BucketFileRepository bucketFileRepository,
                             @Value("${aws.accessKeyId}") String accessKeyId,
                             @Value("${aws.secretAccessKey}") String secretKey,
                             @Value("${aws.region}") String region) {
        this.bucketFileRepository = bucketFileRepository;
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public String uploadFile(MultipartFile file, String description, int fps) throws IOException {
        // Recupera o usuário atual do contexto de segurança
        User currentUser = getCurrentUser();  // Obtém o usuário logado através do contexto de segurança

        // Criamos a entidade para garantir que o accessKey seja gerado
        RequestFrameExtractor fileEntity = RequestFrameExtractor.builder()
                .fileName("") // Inicialmente vazio, será atualizado depois
                .description(description)
                .fps(fps)
                .status(RequestFrameExtractorStatus.EM_PROCESSAMENTO)
                .build();

        fileEntity.setUserInsert(currentUser); // Define o usuário que está realizando a inserção

        // Salva temporariamente para que o @PrePersist gere o accessKey
        fileEntity = bucketFileRepository.save(fileEntity);

        // Agora que a entidade foi persistida, pegamos o accessKey gerado
        String accessKey = fileEntity.getAccessKey();

        // Gera o nome único do arquivo com base no accessKey e nome original
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = accessKey + "_" + originalFileName;

        // Faz o upload para o S3
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        s3Client.putObject(bucketName, uniqueFileName, file.getInputStream(), metadata);

        // Atualiza o nome do arquivo na entidade
        fileEntity.setFileName(uniqueFileName);
        bucketFileRepository.save(fileEntity); // Atualiza no banco

        return "Arquivo enviado com sucesso: " + uniqueFileName;
    }


    public byte[] downloadFile(String fileName) throws IOException {
        RequestFrameExtractor fileEntity = bucketFileRepository.findByFileName(fileName)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado no banco"));

        S3Object s3Object = s3Client.getObject(bucketName, fileEntity.getFileName());
        return s3Object.getObjectContent().readAllBytes();
    }

    public List<String> listFiles(String filename) {
        return bucketFileRepository.findByFileNameContaining(filename);
    }


    public void deleteFile(String fileName) {
        RequestFrameExtractor fileEntity = bucketFileRepository.findByFileName(fileName)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado no banco"));

        // Deleta do S3
        s3Client.deleteObject(bucketName, fileEntity.getFileName());

        // Deleta do banco
        bucketFileRepository.delete(fileEntity);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            Jwt jwt = (Jwt) ((JwtAuthenticationToken) authentication).getPrincipal();

            String username = jwt.getClaimAsString("sub"); // "sub" parece ser o login, não a access_key

            return userRepository.findByLogin(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + username));
        }
        throw new UsernameNotFoundException("Authentication token is invalid");
    }

}