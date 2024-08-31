package konta.projectmd4.service.user;

import konta.projectmd4.model.entity.Address;
import konta.projectmd4.model.entity.Users;

import java.util.List;

public interface IAddressService {
    List<Address> getAllAddressByUserId(Users user);
}
