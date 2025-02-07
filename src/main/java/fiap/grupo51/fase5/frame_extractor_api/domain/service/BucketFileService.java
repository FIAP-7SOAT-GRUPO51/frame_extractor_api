package fiap.grupo51.fase5.frame_extractor_api.domain.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import fiap.grupo51.fase5.frame_extractor_api.api.model.RequestFrameExtractorModel;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorInput;
import fiap.grupo51.fase5.frame_extractor_api.domain.exception.DomainException;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.User;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.RequestFrameExtractorRepository;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.UserRepository;
import fiap.grupo51.fase5.frame_extractor_api.utils.FrameExtractorUtils;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class BucketFileService {
    private final RequestFrameExtractorRepository requestFrameExtractorRepository;
    private final RequestFrameExtractorService requestFrameExtractorService;
    private AmazonS3 s3Client;

    private final UserRepository userRepository;

    @Value("${aws.s3BucketName}")
    private String bucketName;

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    public BucketFileService(RequestFrameExtractorRepository requestFrameExtractorRepository, RequestFrameExtractorService requestFrameExtractorService, UserRepository userRepository) {
        this.requestFrameExtractorRepository = requestFrameExtractorRepository;
        this.requestFrameExtractorService = requestFrameExtractorService;
        this.userRepository = userRepository;

        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, secretKey);
            this.s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.fromName(region))
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .build();
        } catch (Exception e) {
            this.s3Client = null;
            log.error("Erro ao carregar a classe AmazonS3ClientBuilder",e);
        }

    }

    public String uploadFile(MultipartFile file, RequestFrameExtractorInput requestFrameExtractorInput, Authentication authentication) throws IOException {

        RequestFrameExtractorModel requestFrameExtractorModel = requestFrameExtractorService.save(requestFrameExtractorInput, authentication);
        String accessKey = requestFrameExtractorModel.getAccessKey();

        String uniqueFileName = FrameExtractorUtils.generateNameToBucketFromRequestFrameExtractor(file.getOriginalFilename(), accessKey);

        // Faz o upload para o S3
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        if (s3Client == null) {
            throw new DomainException("Erro ao enviar arquivo para o S3. Client não instanciado.");
        }

        s3Client.putObject(bucketName, uniqueFileName, file.getInputStream(), metadata);

        return "Arquivo enviado com sucesso: " + uniqueFileName;
    }


    public byte[] downloadFile(String fileName) throws IOException {
        RequestFrameExtractor fileEntity = requestFrameExtractorRepository.findByFileName(fileName)
                .orElseThrow(() -> new DomainException("Arquivo [" + fileName + "] não encontrado no banco"));

        S3Object s3Object = s3Client.getObject(bucketName, fileEntity.getFileName());
        return s3Object.getObjectContent().readAllBytes();
    }

    public List<String> listFiles(String filename) {
        return requestFrameExtractorRepository.findByFileNameContaining(filename);
    }


    public void deleteFile(String fileName) {
        RequestFrameExtractor fileEntity = requestFrameExtractorRepository.findByFileName(fileName)
                .orElseThrow(() -> new DomainException("Arquivo [" + fileName + "]não encontrado no banco"));

        // Deleta do S3
        s3Client.deleteObject(bucketName, fileEntity.getFileName());

        // Deleta do banco
        requestFrameExtractorRepository.delete(fileEntity);
    }
git statu
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