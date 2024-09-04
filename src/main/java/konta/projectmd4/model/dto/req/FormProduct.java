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
//    @NotBlank(message = "name is blank")
//    private String name;
//    private String description;
//    @NotNull(message = "unit price is null")
//    private Double unitPrice;
//    @NotNull(message = "quantity is null")
//    private Integer quantity;
////    @NotBlank(message = "image is blank")
////    private String image;
//    @NotNull(message = "category id is blank")
//    private Integer categoryId;
    @NotBlank(message = "Name is required and cannot be blank")
    private String name;

    private String description;

    @NotNull(message = "Unit price is required")
    private Double unitPrice;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Category ID is required")
    private Integer categoryId;
}
