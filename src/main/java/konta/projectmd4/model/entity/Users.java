package konta.projectmd4.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String avatar;
    private String address;
    private Date createdAt;
    private Date updatedAt;
    private String phone;
    private Boolean status;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Roles> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="wishlist"
            ,joinColumns = @JoinColumn(name="user_id")
            ,inverseJoinColumns = @JoinColumn(name="product_id"))
    private Set<Product> products;
}
