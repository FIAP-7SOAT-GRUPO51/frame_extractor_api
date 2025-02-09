package fiap.grupo51.fase5.frame_extractor_api.domain.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import fiap.grupo51.fase5.frame_extractor_api.api.model.ListFilesS3BucketModel;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorUploadInput;
import fiap.grupo51.fase5.frame_extractor_api.domain.components.AWSProperties;
import fiap.grupo51.fase5.frame_extractor_api.domain.exception.DomainException;
import fiap.grupo51.fase5.frame_extractor_api.domain.exception.RequestFrameExtractorNotFindException;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.RequestFrameExtractorRepository;
import fiap.grupo51.fase5.frame_extractor_api.utils.FrameExtractorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Service
public class BucketFileService {
    private final RequestFrameExtractorRepository requestFrameExtractorRepository;
    private final AWSProperties awsProperties;
    private AmazonS3 s3Client;

    public BucketFileService(
            RequestFrameExtractorRepository requestFrameExtractorRepository,
            AWSProperties awsProperties) {
        this.requestFrameExtractorRepository = requestFrameExtractorRepository;
        this.awsProperties = awsProperties;

        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.awsProperties.getAccessKeyId(), this.awsProperties.getSecretAccessKey());
            this.s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.fromName(this.awsProperties.getRegion()))
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .build();
        } catch (Exception e) {
            this.s3Client = null;
            log.error("Erro ao carregar a classe AmazonS3ClientBuilder",e);
        }

    }

    public String uploadFile(MultipartFile file, RequestFrameExtractorUploadInput requestFrameExtractorUploadInput) throws IOException {

        String accessKey = requestFrameExtractorUploadInput.getAccessKey();

        String uniqueFileName = FrameExtractorUtils.generateNameToBucketFromRequestFrameExtractor(file.getOriginalFilename(), accessKey);

        // Faz o upload para o S3
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        if (s3Client == null) {
            throw new DomainException("Erro ao enviar arquivo para o S3. Client não instanciado.");
        }
        log.info("Enviando arquivo para o S3: " + uniqueFileName);
        s3Client.putObject(this.awsProperties.getS3BucketName(), uniqueFileName, file.getInputStream(), metadata);

        log.info("Arquivo enviado com sucesso: " + uniqueFileName);
        return "Arquivo enviado com sucesso: " + uniqueFileName;
    }


    public byte[] downloadFile(String accessKey) throws IOException {

        RequestFrameExtractor fileEntity = requestFrameExtractorRepository.findByAccessKey(accessKey)
                .orElseThrow(() -> new RequestFrameExtractorNotFindException("Arquivo com chave de acesso [" + accessKey + "] não encontrado no banco"));

        String fileName = FrameExtractorUtils.generateNameToBucketFromRequestFrameExtractor(fileEntity.getFileName(), accessKey);
        String fileNameS3BucketWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        String fileNameS3Bucket = fileNameS3BucketWithoutExtension + ".zip";

        S3Object s3Object = s3Client.getObject(this.awsProperties.getS3BucketName(), fileNameS3Bucket);
        return s3Object.getObjectContent().readAllBytes();
    }

    public void deleteFile(String fileName) {

        // Deleta do S3
        s3Client.deleteObject(this.awsProperties.getS3BucketName(), fileName);

    }

    public ListFilesS3BucketModel listFilesFromBucket() {

        ListFilesS3BucketModel listFilesS3BucketModel = new ListFilesS3BucketModel();
        listFilesS3BucketModel.setFiles(new ArrayList<>());
        s3Client.listObjects(this.awsProperties.getS3BucketName()).getObjectSummaries().forEach(s3ObjectSummary -> listFilesS3BucketModel.getFiles().add(s3ObjectSummary.getKey()));

        return listFilesS3BucketModel;

    }
}