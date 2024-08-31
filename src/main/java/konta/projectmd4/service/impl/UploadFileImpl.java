package konta.projectmd4.service.impl;

import com.google.cloud.storage.*;

import jakarta.servlet.ServletContext;
import konta.projectmd4.service.IUploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Service
@PropertySource("classpath:upload_file.properties")
public class UploadFileImpl implements IUploadFile {
    @Autowired
    private ServletContext servletContext;

    @Value("${bucket_name}")
    String buck_name;

    @Autowired
    private Storage storage;

    @Override
    public String uploadLocal(MultipartFile fileUpload) {
        String path = servletContext.getRealPath("resources");
        File f = new File(path);
        if(!f.exists()){
            f.mkdir();
        }

        String localPath = f.getAbsolutePath()+File.separator+fileUpload.getOriginalFilename();
        File dest = new File(localPath);
        if(!dest.exists()){
            try {
                Files.write(dest.toPath(), fileUpload.getBytes(), StandardOpenOption.CREATE_NEW);
                return uploadFirebase(localPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }

    @Override
    public String uploadFirebase(String localPath) {
        Path path = Paths.get(localPath);
        String fileName = path.getFileName().toString();
        BlobId blobId = BlobId.of(buck_name,fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        List<Acl> acls = new ArrayList<>();
        acls.add(Acl.of(Acl.User.ofAllUsers(),Acl.Role.READER));
        blobInfo = blobInfo.toBuilder().setAcl(acls).build();
        try {
            Blob blob = storage.create(blobInfo, Files.readAllBytes(path));
            return blob.getMediaLink();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
