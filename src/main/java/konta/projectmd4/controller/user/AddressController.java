package konta.projectmd4.controller.user;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormAddress;
import konta.projectmd4.model.dto.resp.DataResponse;
import konta.projectmd4.model.entity.Address;
import konta.projectmd4.model.entity.Product;
import konta.projectmd4.model.entity.Users;
import konta.projectmd4.security.principle.MyUserDetails;
import konta.projectmd4.service.user.impl.AddressServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/user/account/address")
@RequiredArgsConstructor
public class AddressController {
    @Autowired
    private AddressServiceImpl addressService;

    @GetMapping
    public ResponseEntity<DataResponse<List<Address>>> getAllAddress(@AuthenticationPrincipal MyUserDetails userDetails) {
        return new ResponseEntity<>(new DataResponse<>(addressService.getAllAddressByUserId(userDetails.getUsers()), HttpStatus.OK),HttpStatus.OK);
    }

    //add
    @PostMapping
    public ResponseEntity<DataResponse<String>> addNewAddress(@AuthenticationPrincipal MyUserDetails userDetails, @Validated @RequestBody FormAddress formAddress) throws CustomException {
        addressService.addAddress(formAddress,userDetails.getUsers().getId());
        return new ResponseEntity<>(new DataResponse<>("OK",HttpStatus.CREATED),HttpStatus.CREATED);
    }

    //get by id
    @GetMapping("/{addressId}")
    public ResponseEntity<DataResponse<Address>> getAddress(@AuthenticationPrincipal MyUserDetails userDetails,@PathVariable Integer addressId) throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(addressService.getAddressById(addressId,userDetails.getUsers().getId()),HttpStatus.OK),HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{addressId}")
    public ResponseEntity<DataResponse<String>> deleteAddress(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Integer addressId) throws CustomException {
        addressService.deleteAddressById(userDetails.getUsers().getId(),addressId);
        return new ResponseEntity<>(new DataResponse<>("Delete successfully",HttpStatus.OK),HttpStatus.OK);
    }
}
