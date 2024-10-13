package backend.animal_profiling.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthResponse {

    @Size(max = 255)
    private String token;

}
