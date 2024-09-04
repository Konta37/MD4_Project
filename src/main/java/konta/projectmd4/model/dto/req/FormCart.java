package konta.projectmd4.model.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormCart {
    @NotNull(message = "product id is null")
    private Integer productId;
    @NotNull(message = "quantity is null")
    private Integer quantity;
}
