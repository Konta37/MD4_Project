package konta.projectmd4.service.user;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormAddress;
import konta.projectmd4.model.entity.Address;
import konta.projectmd4.model.entity.Users;

import java.util.List;

public interface IAddressService {
    List<Address> getAllAddressByUserId(Users user);
    Address addAddress(FormAddress address,Long UserId) throws CustomException;
    Address getAddressById(Integer Id,Long UserId) throws CustomException;
    void deleteAddressById(Long userId,Integer addressId) throws CustomException;
}
