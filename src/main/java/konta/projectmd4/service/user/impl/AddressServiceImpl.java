package konta.projectmd4.service.user.impl;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormAddress;
import konta.projectmd4.model.entity.Address;
import konta.projectmd4.model.entity.Users;
import konta.projectmd4.repository.IAddressRepository;
import konta.projectmd4.repository.IUserRepository;
import konta.projectmd4.service.user.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private IAddressRepository addressRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<Address> getAllAddressByUserId(Users user) {

        return addressRepository.findAddressesByUser(user);
    }

    @Override
    public Address addAddress(FormAddress formAddress,Long userId) throws CustomException{
        Address addressUpdate = Address.builder()
                .address(formAddress.getAddress())
                .phone(formAddress.getPhone())
                .receiveName(formAddress.getReceiveName())
                .user(userRepository.getReferenceById(userId))
                .build();

        //check if there are any address duplicate in list
        List<Address> addressList = addressRepository.findAddressesByUser(userRepository.getReferenceById(userId));

        boolean isExist = false;

        for (Address address : addressList) {
            if (address.getAddress().equals(addressUpdate.getAddress())
            && address.getPhone().equals(addressUpdate.getPhone())
            && address.getReceiveName().equals(addressUpdate.getReceiveName())
            && address.getUser() == addressUpdate.getUser()) {
                isExist = true;
                break;
            }
        }

        if (isExist) {
            throw new CustomException("There is already address in your account", HttpStatus.CONFLICT);
        }

        return addressRepository.save(addressUpdate);
    }

    @Override
    public void deleteAddressById(Long userId,Integer addressId) throws CustomException {
        List<Address> addressList = addressRepository.findAddressesByUser(userRepository.getReferenceById(userId));
        boolean isExist = false;
        for (Address address : addressList) {
            if (address.getId().equals(addressId)) {
                isExist = true;
                addressRepository.delete(address);
                break;
            }
        }
        if (!isExist) {
            throw new CustomException("There is no address with id:" + addressId+ " in your account", HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Address getAddressById(Integer addressId, Long userId) throws CustomException {
        List<Address> addressList = addressRepository.findAddressesByUser(userRepository.getReferenceById(userId));
        boolean isExist = false;
        for (Address address : addressList) {
            if (address.getId().equals(addressId)) {
                isExist = true;
                return address;
            }
        }
        if (!isExist) {
            throw new CustomException("There is no address with id:" + addressId+ " in your account", HttpStatus.NOT_FOUND);
        }
        return null;
    }
}
