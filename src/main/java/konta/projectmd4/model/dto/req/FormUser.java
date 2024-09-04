package konta.projectmd4.model.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormUser {
    private String fullName;
//    private String email;
//    private String password;
//    private String avatar;
    private String address;
//    private Date updatedAt;
    private String phone;
}
