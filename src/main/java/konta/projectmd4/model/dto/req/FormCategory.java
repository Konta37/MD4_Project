package konta.projectmd4.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FormCategory {
    @NotBlank(message = "name is blank")
    private String name;
    private String description;
}
