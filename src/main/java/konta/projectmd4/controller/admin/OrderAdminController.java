package konta.projectmd4.controller.admin;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.resp.DataResponse;
import konta.projectmd4.model.entity.Order;
import konta.projectmd4.model.entity.OrderDetail;
import konta.projectmd4.security.principle.MyUserDetails;
import konta.projectmd4.service.impl.OrderServiceImpl;
import konta.projectmd4.service.user.impl.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/orders")
public class OrderAdminController {
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping
    public ResponseEntity<DataResponse<List<Order>>> getAllOrder() throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(orderService.findAll(), HttpStatus.OK),HttpStatus.OK);
    }

    @GetMapping("/{orderStatus}")
    public ResponseEntity<DataResponse<List<Order>>> getAllOrderStatus(@PathVariable String orderStatus) throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(orderService.findAllByStatus(orderStatus), HttpStatus.OK),HttpStatus.OK);
    }

    @GetMapping("/detail/{orderId}")
    public ResponseEntity<DataResponse<List<OrderDetail>>> getOrderDetail(@PathVariable Integer orderId) throws CustomException {
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(new DataResponse<>(orderDetailService.getAllOrderDetail(order),HttpStatus.OK),HttpStatus.OK);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<DataResponse<Order>> updateOrderStatus(@PathVariable Integer orderId, @RequestParam String status) throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(orderService.updateStatusByAdmin(orderId, status), HttpStatus.OK),HttpStatus.OK);
    }
}
