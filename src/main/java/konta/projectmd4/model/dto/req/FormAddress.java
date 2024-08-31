package konta.projectmd4.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FormAddress {
    @NotBlank(message = "address is blank")
    private String address;
    @NotBlank(message = "phone is blank")
    private String phone;
    @NotBlank(message = "receive name is blank")
    private String receiveName;
}
