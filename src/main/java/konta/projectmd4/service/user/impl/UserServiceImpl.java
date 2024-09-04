package konta.projectmd4.service.user.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import konta.projectmd4.constants.RoleName;
import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormUser;
import konta.projectmd4.model.entity.Users;
import konta.projectmd4.repository.IUserRepository;
import konta.projectmd4.service.impl.UploadFileImpl;
import konta.projectmd4.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UploadFileImpl uploadFile;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public Users changeStatus(Long userId) throws CustomException {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User with id: " + userId + " not found.", HttpStatus.NOT_FOUND));

        // Check if the user has the ROLE_ADMIN role
        boolean isAdmin = users.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals(RoleName.ROLE_ADMIN));

        if (isAdmin) {
            throw new CustomException("Cannot change status of an Admin user with id: " + userId, HttpStatus.FORBIDDEN);
        }

        // Toggle the status if the user is not an admin
        users.setStatus(!users.getStatus());
        users.setUpdatedAt(new Date());
        return userRepository.save(users);  // Return the updated user
    }

    @Override
    public Users getUserById(Long userId) {
        return userRepository.getReferenceById(userId);
    }

    @Override
    public Users updateUser(MultipartFile fileUpload, String data, Long userId) throws JsonProcessingException {

        //do things with image
        String imageName = uploadFile.uploadLocal(fileUpload);
//        imageTest = uploadFile.uploadFirebase(imageTest);
//        System.out.println("check link image: " +imageTest);

//        List<String> imageNames = new ArrayList<>();
//        // Loop through each file and upload them
//        for (MultipartFile fileUpload : fileUploads) {
//            String imageName = uploadFile.uploadLocal(fileUpload);
//            imageNames.add(imageName);
//        }

        // change data from string to formUser
        FormUser formUser = mapper.readValue(data,FormUser.class);

        //change data formUser to users
        Users users = userRepository.getReferenceById(userId);
        users.setFullName(formUser.getFullName());
        users.setAvatar(imageName);
        // Assuming you want to set the first image as the avatar.
//        if (!imageNames.isEmpty()) {
//            users.setAvatar(imageNames.get(0)); // Or handle differently if needed
//        }
        users.setAddress(formUser.getAddress());
        users.setPhone(formUser.getPhone());
        users.setUpdatedAt(new Date());
        userRepository.save(users);

        return users;
    }
}
