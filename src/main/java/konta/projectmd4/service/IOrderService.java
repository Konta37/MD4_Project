package konta.projectmd4.service;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormOrder;
import konta.projectmd4.model.entity.Order;
import konta.projectmd4.model.entity.Users;

import java.util.List;

public interface IOrderService {
    Order addToOrder(Users users, FormOrder formOrder) throws CustomException;
    Order findOrderByCode(String code,Users users) throws CustomException;
    List<Order> findAllOrders(Users users) throws CustomException;
    Order cancelOrder(Users users, Integer orderId) throws CustomException;
    List<Order> findAllOrdersByStatus(Users users, String status) throws CustomException;
    //admin
    List<Order> findAll();
    List<Order> findAllByStatus(String status) throws CustomException;
    Order findOrderById(Integer orderId) throws CustomException;
    Order updateStatusByAdmin(Integer orderId, String status) throws CustomException;
    void returnQuantityToProduct(Order order);
}
