package konta.projectmd4.service;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadFile {
    String uploadLocal(MultipartFile fileUpload);
    String uploadFirebase(String localPath);
}
