package konta.projectmd4.service.impl;

import konta.projectmd4.constants.RoleName;
import konta.projectmd4.model.dto.req.FormLogin;
import konta.projectmd4.model.dto.req.FormRegister;
import konta.projectmd4.model.dto.resp.JwtResponse;
import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.entity.Roles;
import konta.projectmd4.model.entity.Users;
import konta.projectmd4.repository.IRoleRepository;
import konta.projectmd4.repository.IUserRepository;
import konta.projectmd4.security.jwt.JwtProvider;
import konta.projectmd4.security.principle.MyUserDetails;
import konta.projectmd4.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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

    @Autowired
    private UploadFileImpl uploadFile;

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

        //default image
        // Set a default image
        String defaultImagePath = "default_avatar.webp"; // Path to the default image in resources
        Resource resource = new ClassPathResource(defaultImagePath);

        String defaultImageLink = "";
        try (InputStream inputStream = resource.getInputStream()) {
            // Create a temp file from the InputStream
            File tempFile = Files.createTempFile("default_avatar", ".webp").toFile();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                StreamUtils.copy(inputStream, out);
            }

            // Upload the default image to Firebase and get the link
            defaultImageLink = uploadFile.uploadFirebase(tempFile.getAbsolutePath());

            // Delete the temp file after upload
            tempFile.delete();
        } catch (IOException e) {
            throw new CustomException("Failed to upload default image", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Set<Roles> roles = new HashSet<>();
        roles.add(findByRoleName(RoleName.ROLE_USER));
        Users users = Users.builder()
                .fullName(formRegister.getFullName())
                .username(formRegister.getUsername())
                .email(formRegister.getEmail())
                .password(passwordEncoder.encode(formRegister.getPassword()))
                .avatar(defaultImageLink)
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
