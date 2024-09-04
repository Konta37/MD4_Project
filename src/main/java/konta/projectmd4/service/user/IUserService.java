package konta.projectmd4.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.entity.Users;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {
    public Users changeStatus(Long userId) throws CustomException;
    Users getUserById(Long userId);
    Users updateUser(MultipartFile fileUpload, String data,Long userId) throws JsonProcessingException;
}
