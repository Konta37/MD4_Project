package konta.projectmd4.repository;

import konta.projectmd4.constants.RoleName;
import konta.projectmd4.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRoleName(RoleName roleName);
}
