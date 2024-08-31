package konta.projectmd4.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import lombok.*;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="full_address")
    private String address;
    @Column(name="phone",length = 15)
    private String phone;
    @Column(name="receive_name",length = 50)
    private String receiveName;
    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore  // Ignore the user field during serialization
    private Users user;
}
