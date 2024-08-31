package konta.projectmd4.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;
    //random code
    @Column(name = "code")
    private String code;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "note")
    private String note;
    @Column(name = "receive_name")
    private String receiveName;
    @Column(name = "receive_address")
    private String receiveAddress;
    @Column(name = "receive_phone")
    private String receivePhone;
    @Column(name = "payment")
    private String payment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "received_at")
    private Date receivedAt;

//    //add coupon
//    @ManyToOne
//    @JoinColumn(name = "coupon_id")
//    private Coupon coupon;

//    //need origin price before using coupon
//    private Double originalPrice;
}
