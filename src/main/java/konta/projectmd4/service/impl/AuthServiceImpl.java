package konta.projectmd4.service.impl;

import konta.projectmd4.constants.RoleName;
import konta.projectmd4.model.dto.req.FormLogin;
import konta.projectmd4.model.dto.req.FormRegister;
import konta.projectmd4.model.dto.resp.JwtResponse;
import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.entity.admin.Roles;
import konta.projectmd4.model.entity.user.Users;
import konta.projectmd4.repository.admin.IRoleRepository;
import konta.projectmd4.repository.admin.IUserRepository;
import konta.projectmd4.security.jwt.JwtProvider;
import konta.projectmd4.security.principle.MyUserDetails;
import konta.projectmd4.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager manager;
    private final JwtProvider jwtProvider;

    @Override
    public void register(FormRegister formRegister) throws CustomException
    {
        // Check if the username already exists
        if (userRepository.findByUsername(formRegister.getUsername()).isPresent()) {
            throw new CustomException("Username already exists", HttpStatus.BAD_REQUEST);
        }

        // Check if the email already exists
        if (userRepository.findByEmail(formRegister.getEmail()).isPresent()) {
            throw new CustomException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        Set<Roles> roles = new HashSet<>();
        roles.add(findByRoleName(RoleName.ROLE_USER));
        Users users = Users.builder()
                .fullName(formRegister.getFullName())
                .username(formRegister.getUsername())
                .email(formRegister.getEmail())
                .password(passwordEncoder.encode(formRegister.getPassword()))
//                .avatar("")
//                .address("")
                .createdAt(new Date())
                .updatedAt(new Date())
                .roles(roles)
                .status(true)
                .build();
        userRepository.save(users);
    }

    @Override
    public JwtResponse login(FormLogin formLogin) throws CustomException
    {
        Authentication authentication;
        try
        {
            authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.getUsername(), formLogin.getPassword()));
        }
        catch (AuthenticationException e)
        {
            throw new CustomException("Username or password is incorrect", HttpStatus.BAD_REQUEST);
        }

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        //check if user status is false
        if(!userDetails.getUsers().getStatus())
        {
            throw new CustomException("Your account has blocked", HttpStatus.BAD_REQUEST);
        }



        return JwtResponse.builder()
                .accessToken(jwtProvider.generateToken(userDetails.getUsername()))
                .fullName(userDetails.getUsers().getFullName())
                .email(userDetails.getUsers().getEmail())
                .phone(userDetails.getUsers().getPhone())
                .username(userDetails.getUsers().getUsername())
                .avatar(userDetails.getUsers().getAvatar())
                .address(userDetails.getUsers().getAddress())
                .createdAt(userDetails.getUsers().getCreatedAt())
                .updatedAt(userDetails.getUsers().getUpdatedAt())
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .status(userDetails.getUsers().getStatus())
                .build();
    }

    public Roles findByRoleName(RoleName roleName) throws CustomException
    {
        return roleRepository.findByRoleName(roleName).orElseThrow(() -> new CustomException("role not found", HttpStatus.NOT_FOUND));
    }
}
