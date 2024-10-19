package backend.animal_profiling.repos;

import backend.animal_profiling.domain.Role;
import backend.animal_profiling.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

    User findFirstByRole(Role role);
    User findUserByEmail(String emailAddress);

}
