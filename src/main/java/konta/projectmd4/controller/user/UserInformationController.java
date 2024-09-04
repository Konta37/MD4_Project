package konta.projectmd4.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.resp.DataResponse;
import konta.projectmd4.model.entity.Category;
import konta.projectmd4.model.entity.Users;
import konta.projectmd4.security.principle.MyUserDetails;
import konta.projectmd4.service.impl.UploadFileImpl;
import konta.projectmd4.service.user.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user/account")
@RequiredArgsConstructor
public class UserInformationController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping()
    public ResponseEntity<DataResponse<Users>> getInformation(@AuthenticationPrincipal MyUserDetails userDetails) throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(userDetails.getUsers(), HttpStatus.OK),HttpStatus.OK);
//        return null;
    }

    @PostMapping
    public ResponseEntity<DataResponse<String>> editInformation(@AuthenticationPrincipal MyUserDetails userDetails, @RequestParam("imageFile")MultipartFile imageFile,@RequestParam("data")String dataUser) throws JsonProcessingException {
        userService.updateUser(imageFile,dataUser,userDetails.getUsers().getId());
        return new ResponseEntity<>(new DataResponse<>("Update Success", HttpStatus.OK),HttpStatus.OK);
    }
}
