package konta.projectmd4.model.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormRegister {
    @NotBlank(message = "Name is blank")
    private String fullName;
    @NotBlank(message = "UserName is blank")
    private String username;
    @NotBlank(message = "Email is blank")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "Email must be a valid Gmail address")
    private String email;
    @NotBlank(message = "Password is blank")
    private String password;
}
