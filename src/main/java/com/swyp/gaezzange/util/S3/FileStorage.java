package com.swyp.gaezzange.util.S3;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
  String upload(FileType fileType, MultipartFile file) throws IOException;
  String updateFile(FileType fileType, String oldKey, MultipartFile file) throws IOException;
  boolean checkFileExists(String key);
  void deleteFile(String key);
  boolean checkFileDeleted(String key);
}
