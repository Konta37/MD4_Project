package konta.projectmd4.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FormProduct {
    @NotBlank(message = "name is blank")
    private String name;
    private String description;
    @NotNull(message = "unit price is null")
    private Double unitPrice;
    @NotNull(message = "quantity is null")
    private Integer quantity;
    @NotBlank(message = "image is blank")
    private String image;
    @NotNull(message = "category id is blank")
    private Integer categoryId;
}
