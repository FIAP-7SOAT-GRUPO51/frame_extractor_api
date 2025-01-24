package fiap.grupo51.fase5.frame_extractor_api.domain.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class BucketFileService {
    private AmazonS3 s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    // Método setter para testes
    public void setS3Client(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public BucketFileService(@Value("${aws.accessKeyId}") String accessKeyId,
                                   @Value("${aws.secretKey}") String secretKey,
                                   @Value("${aws.region}") String region) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }



    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        s3Client.putObject(bucketName, fileName, file.getInputStream(), null);
        return "File uploaded successfully: " + fileName;
    }

    public byte[] downloadFile(String fileName) throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        return s3Object.getObjectContent().readAllBytes();
    }

    public List<String> listFiles(String filename) {
        ObjectListing objectListing = s3Client.listObjects(bucketName);
        List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();

        return s3ObjectSummaries.stream()
                .map(S3ObjectSummary::getKey)
                .filter(key -> filename == null || key.toLowerCase().contains(filename.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }

}
