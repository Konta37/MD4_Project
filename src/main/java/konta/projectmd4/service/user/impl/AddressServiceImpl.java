package konta.projectmd4.service.user.impl;

import konta.projectmd4.model.entity.Address;
import konta.projectmd4.model.entity.Users;
import konta.projectmd4.repository.IAddressRepository;
import konta.projectmd4.service.user.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private IAddressRepository addressRepository;

    @Override
    public List<Address> getAllAddressByUserId(Users user) {

        return addressRepository.findAddressesByUser(user);
    }
}
