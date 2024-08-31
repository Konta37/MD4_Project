package konta.projectmd4.controller.user;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.resp.DataResponse;
import konta.projectmd4.model.entity.Product;
import konta.projectmd4.model.entity.Roles;
import konta.projectmd4.security.principle.MyUserDetails;
import konta.projectmd4.service.user.impl.WishListServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/user/wishlist")
@RequiredArgsConstructor
public class WishListController {

    @Autowired
    private WishListServiceImpl wishListService;

    @GetMapping
    public ResponseEntity<DataResponse<Set<Product>>> getAllWishList(@AuthenticationPrincipal MyUserDetails userDetails) {
        return new ResponseEntity<>(new DataResponse<>(wishListService.listWishList(userDetails.getUsers().getId()),HttpStatus.OK),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DataResponse<String>> getRoles(@RequestParam("productId") Integer productId, @AuthenticationPrincipal MyUserDetails userDetails) throws CustomException {
//        return ResponseEntity.ok().body(roleRepository.findAll());
//        System.out.println(userDetails.getUsers().getId());
        wishListService.saveWishlist(userDetails.getUsers().getId(), productId);
        return new ResponseEntity<>(new DataResponse<>("add to wishlist", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<DataResponse<String>> deleteProInWishlist(@PathVariable("productId")Integer productId, @AuthenticationPrincipal MyUserDetails userDetails) throws CustomException {
        wishListService.deleteProductInWishlist(userDetails.getUsers().getId(), productId);
        return new ResponseEntity<>(new DataResponse<>("success delete product wishlist", HttpStatus.OK), HttpStatus.OK);
    }
}
