package konta.projectmd4.repository;

import konta.projectmd4.model.entity.Order;
import konta.projectmd4.model.entity.Status;
import konta.projectmd4.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<Order, Integer> {
    List<Order> getOrdersByUser(Users user);
    Order getOrderByCodeAndUser(String code, Users user);
    Order getOrderByIdAndUser(Integer id, Users user);
    List<Order> getOrdersByUserAndStatus(Users user, Status status);
    List<Order> getOrdersByStatus(Status status);
    Order getOrderById(Integer id);
}
