package konta.projectmd4.repository;

import konta.projectmd4.model.entity.Address;
import konta.projectmd4.model.entity.Category;
import konta.projectmd4.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findAddressesByUser(Users users);
}
