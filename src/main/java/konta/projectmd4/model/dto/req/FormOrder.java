package konta.projectmd4.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormOrder {
    private String note;
    @NotNull(message = "address can't be null")
    private Integer addressId;
    @NotBlank(message = "payment can't be blank")
    private String payment;
}
