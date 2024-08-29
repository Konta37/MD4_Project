package konta.projectmd4.security.principle;

import konta.projectmd4.model.entity.user.Users;
import konta.projectmd4.repository.admin.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Users users = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
        return MyUserDetails.builder()
                .users(users)
                .authorities(users.getRoles().stream().map(roles -> new SimpleGrantedAuthority(roles.getRoleName().toString())).toList())
                .build();
    }
}
