package konta.projectmd4.service.user;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormCart;
import konta.projectmd4.model.entity.CartItem;
import konta.projectmd4.model.entity.Product;
import konta.projectmd4.model.entity.Users;

import java.util.List;

public interface ICartService {
    List<CartItem> getAllCartItems(Users user);
    CartItem save(Users user, FormCart formCart) throws CustomException;
    CartItem getCartItemsByUserAndId(Users user, Integer id) throws CustomException;
    CartItem update(Users users, Integer cartId, Integer quantity) throws CustomException;
    void delete(Users users, Integer cartId) throws CustomException;
    void deleteAllByUser(Users user) throws CustomException;
}
