package backend.animal_profiling.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoleDTO {

    private Integer roleId;

    @NotNull
    @Size(max = 50)
    private String roleName;

}
