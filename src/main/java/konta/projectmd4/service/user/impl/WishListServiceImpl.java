package konta.projectmd4.service.user.impl;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.entity.Product;
import konta.projectmd4.model.entity.Users;
import konta.projectmd4.repository.IProductRepository;
import konta.projectmd4.repository.IUserRepository;
import konta.projectmd4.security.principle.MyUserDetails;
import konta.projectmd4.service.admin.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ProductService productRepository;

    public Set<Product> listWishList(Long userId){
//        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users exitsUsers = userRepository.getReferenceById(userId);

        Set<Product> productList = exitsUsers.getProducts();
        return productList;
    }

    public void saveWishlist(Long userId,Integer productId) throws CustomException {
        Users exitsUsers = userRepository.getReferenceById(userId);

        Set<Product> productList = exitsUsers.getProducts();

        productList.add(productRepository.findById(productId));
        exitsUsers.setProducts(productList);
        userRepository.save(exitsUsers);

    }

    public void deleteProductInWishlist(Long userId,Integer productId) throws CustomException {
        Users exitsUsers = userRepository.getReferenceById(userId);
        Set<Product> productList = exitsUsers.getProducts();
        boolean isExist = false;
        for (Product product : productList) {
            if (product.getId() == productId) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            productList.remove(productRepository.findById(productId));
            exitsUsers.setProducts(productList);
            userRepository.save(exitsUsers);
        }else {
            throw new CustomException("There is no product id: " + productId + " in wishlist", HttpStatus.OK);
        }
    }
}
