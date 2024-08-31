package konta.projectmd4.repository;

public interface IWishlistRepository {
    Boolean productExists(Integer userId, Integer productDetailId);
    void addToWishlist(Integer userId, Integer productId);
    Boolean removeFromWishlist(Integer userId, Integer productId);
}
