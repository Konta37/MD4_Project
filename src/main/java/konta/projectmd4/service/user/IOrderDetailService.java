package konta.projectmd4.service.user;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.entity.Order;
import konta.projectmd4.model.entity.OrderDetail;
import konta.projectmd4.model.entity.Product;
import konta.projectmd4.model.entity.Users;

import java.util.List;

public interface IOrderDetailService {
    List<OrderDetail> getAllOrderDetail(Order order, Users user);
    List<OrderDetail> getAllOrderDetail(Order order);
}
