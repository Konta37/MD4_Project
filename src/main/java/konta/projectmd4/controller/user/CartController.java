package konta.projectmd4.controller.user;

import jakarta.validation.Valid;
import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormCart;
import konta.projectmd4.model.dto.req.FormOrder;
import konta.projectmd4.model.dto.resp.DataResponse;
import konta.projectmd4.model.entity.Address;
import konta.projectmd4.model.entity.CartItem;
import konta.projectmd4.model.entity.Order;
import konta.projectmd4.security.principle.MyUserDetails;
import konta.projectmd4.service.impl.OrderServiceImpl;
import konta.projectmd4.service.user.impl.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/cart")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private OrderServiceImpl orderService;

    @GetMapping("/list")
    public ResponseEntity<DataResponse<List<CartItem>>> getAllCart(@AuthenticationPrincipal MyUserDetails userDetails) {
        return new ResponseEntity<>(new DataResponse<>(cartService.getAllCartItems(userDetails.getUsers()), HttpStatus.OK),HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<DataResponse<CartItem>> addToCart(@AuthenticationPrincipal MyUserDetails userDetails,@Valid @RequestBody FormCart formCart) throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(cartService.save(userDetails.getUsers(),formCart),HttpStatus.OK),HttpStatus.OK);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<DataResponse<CartItem>> updateQuantityCart(@AuthenticationPrincipal MyUserDetails userDetails,
                                                                     @PathVariable Integer cartItemId,
                                                                     @RequestParam Integer quantity) throws CustomException {

        return new ResponseEntity<>(new DataResponse<>(cartService.update(userDetails.getUsers(), cartItemId,quantity),HttpStatus.OK),HttpStatus.OK);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<DataResponse<String>> deleteCartItem(@AuthenticationPrincipal MyUserDetails userDetails,
                                                               @PathVariable Integer cartItemId) throws CustomException {
        cartService.delete(userDetails.getUsers(), cartItemId);
        return new ResponseEntity<>(new DataResponse<>("Delete successfully",HttpStatus.OK),HttpStatus.OK);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<DataResponse<String>> clearCart(@AuthenticationPrincipal MyUserDetails userDetails) throws CustomException {
        cartService.deleteAllByUser(userDetails.getUsers());
        return new ResponseEntity<>(new DataResponse<>("Delete successfully",HttpStatus.OK),HttpStatus.OK);
    }

    @GetMapping("/checkout")
    public ResponseEntity<DataResponse<Order>> addToOrder(@AuthenticationPrincipal MyUserDetails userDetails,@Valid @RequestBody FormOrder formOrder) throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(orderService.addToOrder(userDetails.getUsers(), formOrder),HttpStatus.OK),HttpStatus.OK);
    }
}
