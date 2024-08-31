package konta.projectmd4.repository;

import konta.projectmd4.model.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    List<Users> findByFullNameContains(String fullName);
    Optional<Users> findById(Long id);
    Page<Users> findAll(Pageable pageable);
}
