package konta.projectmd4.repository;

import konta.projectmd4.model.entity.Order;
import konta.projectmd4.model.entity.OrderDetail;
import konta.projectmd4.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> getOrderDetailsByOrder(Order order);
    List<OrderDetail> findOrderDetailsByProduct(Product product);
}
