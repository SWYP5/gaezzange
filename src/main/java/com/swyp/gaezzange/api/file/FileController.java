package com.swyp.gaezzange.api.file;

import com.swyp.gaezzange.api.file.dto.FileUploadResultDto;
import com.swyp.gaezzange.exception.customException.CustomSystemException;
import com.swyp.gaezzange.util.ApiResponse;
import com.swyp.gaezzange.util.S3.FileStorage;
import com.swyp.gaezzange.util.S3.FileType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "FileController", description = "FILE API")
@RestController
@RequestMapping(("/v1/file"))
@Slf4j
@RequiredArgsConstructor
public class FileController {

  private final FileStorage fileStorage;

  @PostMapping("/{fileType}")
  public ApiResponse<FileUploadResultDto> uploadFile(
      @PathVariable FileType fileType, @RequestPart(value = "file") MultipartFile file
      ) {
    try {
      return ApiResponse.success(new FileUploadResultDto(fileStorage.upload(fileType, file)));
    } catch (Exception e) {
      log.error("[UPLOAD] fails to upload file. type: {}", file, e);
      throw new CustomSystemException("FAIL_TO_UPLOAD_FILE", "파일 업로드에 실패했습니다.");
    }
  }

}
