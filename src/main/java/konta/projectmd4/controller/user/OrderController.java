package konta.projectmd4.controller.user;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.resp.DataResponse;
import konta.projectmd4.model.entity.CartItem;
import konta.projectmd4.model.entity.Order;
import konta.projectmd4.model.entity.OrderDetail;
import konta.projectmd4.security.principle.MyUserDetails;
import konta.projectmd4.service.impl.OrderServiceImpl;
import konta.projectmd4.service.user.impl.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/history")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping
    public ResponseEntity<DataResponse<List<Order>>> getAllOrder(@AuthenticationPrincipal MyUserDetails userDetails) throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(orderService.findAllOrders(userDetails.getUsers()), HttpStatus.OK),HttpStatus.OK);
    }

    @GetMapping("/orderCode/{code}")
    public ResponseEntity<DataResponse<Order>> getOrder(@AuthenticationPrincipal MyUserDetails userDetails,@PathVariable String code) throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(orderService.findOrderByCode(code, userDetails.getUsers()), HttpStatus.OK),HttpStatus.OK);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<DataResponse<Order>> cancelOrder(@AuthenticationPrincipal MyUserDetails userDetails,@PathVariable Integer orderId) throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(orderService.cancelOrder(userDetails.getUsers(),orderId), HttpStatus.OK),HttpStatus.OK);
    }

    @GetMapping("/{orderStatus}")
    public ResponseEntity<DataResponse<List<Order>>> ordersByStatus(@AuthenticationPrincipal MyUserDetails userDetails,@PathVariable String orderStatus) throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(orderService.findAllOrdersByStatus(userDetails.getUsers(), orderStatus), HttpStatus.OK),HttpStatus.OK);
    }

    @GetMapping("/detail/{code}")
    public ResponseEntity<DataResponse<List<OrderDetail>>> getOrdersByCode(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable String code) throws CustomException {
        Order order = orderService.findOrderByCode(code, userDetails.getUsers());
        return new ResponseEntity<>(new DataResponse<>(orderDetailService.getAllOrderDetail(order, userDetails.getUsers()), HttpStatus.OK),HttpStatus.OK);
    }
}
