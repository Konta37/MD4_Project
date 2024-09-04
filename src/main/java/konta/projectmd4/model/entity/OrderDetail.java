package konta.projectmd4.model.entity;


import lombok.*;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;
//    @Column(name="product_name")
//    private String productName;
    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;
    @Column(name="quantity",nullable = false)
    private Integer quantity;
}
