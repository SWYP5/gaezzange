package com.swyp.gaezzange.util.S3;

import com.swyp.gaezzange.exception.customException.InvalidFileException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class S3FileStorage implements FileStorage {

  private final S3Client s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  public S3FileStorage(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  private static final List<String> IMAGE_MIME_TYPES = List.of("image/jpeg", "image/png", "image/gif");

  private void validateImageFile(MultipartFile file) {
    if (!IMAGE_MIME_TYPES.contains(file.getContentType())) {
      throw new InvalidFileException("Invalid file type. Only image files are allowed.");
    }
  }

  @Override
  public String upload(FileType fileType, MultipartFile file) throws IOException {
    validateImageFile(file);
    String key = fileType.getBasePath() + "/" + UUID.randomUUID() + "_" + LocalDateTime.now();
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .contentType(file.getContentType())
        .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromByteBuffer(ByteBuffer.wrap(file.getBytes())));
    return key;
  }

  @Override
  public boolean checkFileExists(String key) {
    try {
      HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .build();
      s3Client.headObject(headObjectRequest);
      return true;
    } catch (NoSuchKeyException e) {
      return false;
    }
  }

  @Override
  public String updateFile(FileType fileType, String oldKey, MultipartFile file) throws IOException {
    validateImageFile(file);
    deleteFile(oldKey);
    return upload(fileType, file);
  }

  @Override
  public void deleteFile(String key) {
    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();
    s3Client.deleteObject(deleteObjectRequest);
  }

  @Override
  public boolean checkFileDeleted(String key) {
    return !checkFileExists(key);
  }
}
