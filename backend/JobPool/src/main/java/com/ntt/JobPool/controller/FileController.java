package com.ntt.JobPool.controller;

import com.ntt.JobPool.domain.response.RestResponse;
import com.ntt.JobPool.domain.response.file.ResUploadFileDTO;
import com.ntt.JobPool.service.FileService;
import com.ntt.JobPool.utils.annotations.ApiMessage;
import com.ntt.JobPool.utils.exception.StorageException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/")
public class FileController {

  @Value("${trucnguyen.upload-file.base-path}")
  private String baseURI;

  @Autowired
  private FileService fileService;

  @PostMapping("/files")
  @ApiMessage("Upload single file")
  public ResponseEntity<ResUploadFileDTO> upload(
      @RequestParam(name = "file", required = false) MultipartFile file,
      @RequestParam("folder") String folder)
      throws URISyntaxException, IOException, StorageException {

    if (file.isEmpty() || file == null) {
      throw new StorageException("File is empty. Please upload a file !");
    }

    String fileName = file.getOriginalFilename();
    List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
    boolean isValid = allowedExtensions.stream()
        .anyMatch(item -> fileName.toString().endsWith(item));

    if (!isValid) {
      throw new StorageException(
          "Invalid file, only allow file with extension " + allowedExtensions.toString());
    }

    this.fileService.createDirectory(baseURI + folder);

    String uploadfileName = this.fileService.store(file, folder);

    ResUploadFileDTO res = new ResUploadFileDTO();
    res.setFileName(uploadfileName);
    res.setUploadedAt(Instant.now());

    return ResponseEntity.ok().body(res);
  }

  @GetMapping("/files")
  @ApiMessage("Down load a file")
  public ResponseEntity<Resource> downloadFile(
      @RequestParam(name = "fileName", required = false) String fileName,
      @RequestParam(name = "folder", required = false) String folder)
      throws StorageException, URISyntaxException, FileNotFoundException {
    if (fileName == null || fileName.isEmpty()) {
      throw new StorageException("Missing required params: (fileName or folder in query params)");
    }

    // check file exist and not a directory
    long fileLength = this.fileService.getFileLength(fileName, folder);

    if (fileLength == 0) {
      throw new StorageException("File with name = " + fileName + " not found !");
    }

    //down file
    InputStreamResource resource = this.fileService.getResource(fileName, folder);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
        .contentLength(fileLength)
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }


}
