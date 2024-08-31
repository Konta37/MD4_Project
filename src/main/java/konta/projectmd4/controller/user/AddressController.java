package konta.projectmd4.controller.user;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/user/address")
@RequiredArgsConstructor
public class AddressController {
    @Autowired
    private AddressServiceImpl addressService;

    @GetMapping
    public ResponseEntity<DataResponse<List<Address>>> getAllAddress(@AuthenticationPrincipal MyUserDetails userDetails) {
        return new ResponseEntity<>(new DataResponse<>(addressService.getAllAddressByUserId(userDetails.getUsers()), HttpStatus.OK),HttpStatus.OK);
    }
}
