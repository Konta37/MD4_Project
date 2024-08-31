package konta.projectmd4.controller.user;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.resp.DataResponse;
import konta.projectmd4.model.entity.Category;
import konta.projectmd4.model.entity.Users;
import konta.projectmd4.security.principle.MyUserDetails;
import konta.projectmd4.service.user.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/account")
@RequiredArgsConstructor
public class UserInformationController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping()
    public ResponseEntity<DataResponse<Users>> getCategory(@AuthenticationPrincipal MyUserDetails userDetails) throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(userDetails.getUsers(), HttpStatus.OK),HttpStatus.OK);
//        return null;
    }
}
