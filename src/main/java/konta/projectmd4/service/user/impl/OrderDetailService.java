package konta.projectmd4.service.user.impl;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.entity.Order;
import konta.projectmd4.model.entity.OrderDetail;
import konta.projectmd4.model.entity.Product;
import konta.projectmd4.model.entity.Users;
import konta.projectmd4.repository.IOrderDetailRepository;
import konta.projectmd4.service.user.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService implements IOrderDetailService {

    @Autowired
    private IOrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetail> getAllOrderDetail(Order order, Users user) {
        return orderDetailRepository.getOrderDetailsByOrder(order);
    }

    @Override
    public List<OrderDetail> getAllOrderDetail(Order order) {
        return orderDetailRepository.getOrderDetailsByOrder(order);
    }


}
