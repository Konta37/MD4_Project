package konta.projectmd4.model.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;
    private Integer quantity;
}
