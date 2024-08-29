package konta.projectmd4.model.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JwtResponse {
    private String accessToken;
    private final String type = "Bearer";
    private String fullName;
    private String username;
    private String email;
    private String avatar;
    private String address;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isDeleted;
    private String phone;
    private Boolean status;
    private Set<String> roles;
}
