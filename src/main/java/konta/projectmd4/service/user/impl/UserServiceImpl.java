package konta.projectmd4.service.user.impl;

import konta.projectmd4.constants.RoleName;
import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.entity.user.Users;
import konta.projectmd4.repository.admin.IUserRepository;
import konta.projectmd4.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
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
}
