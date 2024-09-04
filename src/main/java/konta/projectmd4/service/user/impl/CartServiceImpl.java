package konta.projectmd4.service.user.impl;

import jakarta.transaction.Transactional;
import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormCart;
import konta.projectmd4.model.entity.CartItem;
import konta.projectmd4.model.entity.Product;
import konta.projectmd4.model.entity.Users;
import konta.projectmd4.repository.ICartRepository;
import konta.projectmd4.repository.ICategoryRepository;
import konta.projectmd4.service.admin.impl.ProductService;
import konta.projectmd4.service.user.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Override
    public List<CartItem> getAllCartItems(Users user) {
        return cartRepository.getCartItemsByUser(user);
    }

    @Override
    public CartItem save(Users user, FormCart formCart) throws CustomException {
        // Step 1: check quantity
        if (formCart.getQuantity()<=0){
            throw new CustomException("Quantity is <= 0. Try again!", HttpStatus.BAD_REQUEST);
        }

        // Step 2: Check product
        Product product = productService.findById(formCart.getProductId());

        // Step 3: Check product is Exist in cart
        CartItem cartItemExist = cartRepository.getCartItemByUserAndProduct(user,product);

        // Step 4: if exist -> add more quantity
        if (cartItemExist!=null){
            // Step 5: check quantity before add
            if (cartItemExist.getQuantity()+formCart.getQuantity()<=product.getQuantity()){
                cartItemExist.setQuantity(cartItemExist.getQuantity()+formCart.getQuantity());
                return cartRepository.save(cartItemExist);
            }else {
                throw new CustomException("Quantity is greater than product quantity! Try again!", HttpStatus.BAD_REQUEST);
            }
        }

        // Step 6: check quantity again
        if (formCart.getQuantity()> product.getQuantity()){
            throw new CustomException("Quantity is greater than product quantity! Try again!", HttpStatus.BAD_REQUEST);
        }

        // Step 7: if not -> create new to save
        CartItem cartItem = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(formCart.getQuantity())
                .build();

        return cartRepository.save(cartItem);
    }

    @Override
    public CartItem getCartItemsByUserAndId(Users user, Integer id) throws CustomException {
        CartItem cartItem = cartRepository.getCartItemsByUserAndId(user,id);
        if (cartItem==null){
            throw new CustomException("CartItem not found!", HttpStatus.NOT_FOUND);
        }
        return cartItem;
    }

    @Override
    public CartItem update(Users users, Integer cartId, Integer quantity) throws CustomException{
        // Step 1: check cart items
        CartItem cartItem = getCartItemsByUserAndId(users, cartId);

        // Step 2: check quantity <=0
        if (quantity<=0){
            throw new CustomException("Quantity is <= 0. Try again!", HttpStatus.BAD_REQUEST);
        }

        // Step 3: check quantity + exist quantity <= product quantity
        if (quantity>cartItem.getProduct().getQuantity()){
            throw new CustomException("Quantity is greater than product quantity! Try again!", HttpStatus.BAD_REQUEST);
        }

        // Step 4: set new quantity
        cartItem.setQuantity(quantity);

        // Step 5: Save
        return cartRepository.save(cartItem);
    }

    @Override
    public void delete(Users users, Integer cartId) throws CustomException {
        // Step 1: check cart items
        CartItem cartItem = getCartItemsByUserAndId(users, cartId);

        cartRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void deleteAllByUser(Users user) throws CustomException {
        List<CartItem> cartItemList = cartRepository.getCartItemsByUser(user);

        if (cartItemList==null || cartItemList.size()==0){
            throw new CustomException("CartItem's user not found!", HttpStatus.NOT_FOUND);
        }

        cartRepository.deleteByUser(user);
    }

}
