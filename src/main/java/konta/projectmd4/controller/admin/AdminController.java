package konta.projectmd4.controller.admin;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.entity.user.Users;
import konta.projectmd4.repository.admin.IRoleRepository;
import konta.projectmd4.repository.admin.IUserRepository;
import konta.projectmd4.service.user.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok().body(roleRepository.findAll());
    }

    //search by full name
    //default is all user
    @GetMapping("/user/find")
    public ResponseEntity<?> getUser(@RequestParam(name = "fullName", required = false) String userFullName) {
        if (userFullName == null || userFullName.isEmpty()) {
            return ResponseEntity.ok().body(userRepository.findAll());
        } else {
            return ResponseEntity.ok().body(userRepository.findByFullNameContains(userFullName));
        }
    }

    //change status of user id
    @PutMapping("/user/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId) throws CustomException {
        Users updatedUser = userService.changeStatus(userId);  // Get the updated user directly
        return ResponseEntity.ok().body(updatedUser);  // Return the updated user in the response
    }


    /**
     * @return list user
     * @param pageable import from domain.Pageable
     * */
    @GetMapping("/user")
    public ResponseEntity<?> getAllUsers(@PageableDefault(page = 0, size = 2) Pageable pageable) {
        // Use the pageable object to fetch paginated data
        Page<Users> usersPage = userRepository.findAll(pageable);

        return ResponseEntity.ok().body(usersPage);
    }
}
