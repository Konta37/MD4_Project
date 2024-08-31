package konta.projectmd4.controller.admin;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.resp.DataResponse;
import konta.projectmd4.model.entity.Roles;
import konta.projectmd4.model.entity.Users;
import konta.projectmd4.repository.IRoleRepository;
import konta.projectmd4.repository.IUserRepository;
import konta.projectmd4.service.user.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok().body("Welcome to admin");
    }

    @GetMapping("/role")
    public ResponseEntity<DataResponse<List<Roles>>> getRoles() {
//        return ResponseEntity.ok().body(roleRepository.findAll());
        return new ResponseEntity<>(new DataResponse<>(roleRepository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    //search by full name
    //default is all user
    @GetMapping("/user/find")
    public ResponseEntity<DataResponse<List<Users>>> getUser(@RequestParam(name = "fullName", required = false) String userFullName) {
        if (userFullName == null || userFullName.isEmpty()) {
            return new ResponseEntity<>(new DataResponse<>(userRepository.findAll(), HttpStatus.OK), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new DataResponse<>(userRepository.findByFullNameContains(userFullName), HttpStatus.OK), HttpStatus.OK);
        }
    }

    //change status of user id
    @PutMapping("/user/{userId}")
    public ResponseEntity<DataResponse<Users>> updateUser(@PathVariable Long userId) throws CustomException {
        Users updatedUser = userService.changeStatus(userId);  // Get the updated user directly
        return new ResponseEntity<>(new DataResponse<>(updatedUser, HttpStatus.OK), HttpStatus.OK); // Return the updated user in the response
    }


    /**
     * @return list user
     * @param pageable import from domain.Pageable
     * */
    @GetMapping("/user")
    public ResponseEntity<DataResponse<Page<Users>>> getAllUsers(@PageableDefault(page = 0, size = 2) Pageable pageable) {
        // Use the pageable object to fetch paginated data
        Page<Users> usersPage = userRepository.findAll(pageable);

        return new ResponseEntity<>(new DataResponse<>(usersPage, HttpStatus.OK), HttpStatus.OK);
    }
}
