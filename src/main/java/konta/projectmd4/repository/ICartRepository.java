package konta.projectmd4.repository;

import konta.projectmd4.model.entity.CartItem;
import konta.projectmd4.model.entity.Product;
import konta.projectmd4.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICartRepository extends JpaRepository<CartItem,Integer> {
    List<CartItem> getCartItemsByUser(Users users);
    CartItem getCartItemsByUserAndId(Users users, Integer id);
    CartItem getCartItemByUserAndProduct(Users users, Product product);
    void deleteCartItemById(Integer id);
    void deleteByUser(Users users);
    List<CartItem> findCartItemsByProduct(Product product);
}
