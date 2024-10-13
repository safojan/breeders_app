package backend.animal_profiling.service;

import backend.animal_profiling.domain.DataAnalysisReport;
import backend.animal_profiling.domain.Role;
import backend.animal_profiling.domain.User;
import backend.animal_profiling.model.UserDTO;
import backend.animal_profiling.repos.DataAnalysisReportRepository;
import backend.animal_profiling.repos.RoleRepository;
import backend.animal_profiling.repos.UserRepository;
import backend.animal_profiling.util.NotFoundException;
import backend.animal_profiling.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DataAnalysisReportRepository dataAnalysisReportRepository;

    public UserService(final UserRepository userRepository, final RoleRepository roleRepository,
            final DataAnalysisReportRepository dataAnalysisReportRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.dataAnalysisReportRepository = dataAnalysisReportRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("userId"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Integer userId) {
        return userRepository.findById(userId)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getUserId();
    }

    public void update(final Integer userId, final UserDTO userDTO) {
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Integer userId) {
        userRepository.deleteById(userId);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setContactNumber(user.getContactNumber());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setRole(user.getRole() == null ? null : user.getRole().getRoleId());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setContactNumber(userDTO.getContactNumber());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setUpdatedAt(userDTO.getUpdatedAt());
        final Role role = userDTO.getRole() == null ? null : roleRepository.findById(userDTO.getRole())
                .orElseThrow(() -> new NotFoundException("role not found"));
        user.setRole(role);
        return user;
    }

    public ReferencedWarning getReferencedWarning(final Integer userId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        final DataAnalysisReport createdByUserDataAnalysisReport = dataAnalysisReportRepository.findFirstByCreatedByUser(user);
        if (createdByUserDataAnalysisReport != null) {
            referencedWarning.setKey("user.dataAnalysisReport.createdByUser.referenced");
            referencedWarning.addParam(createdByUserDataAnalysisReport.getReportId());
            return referencedWarning;
        }
        return null;
    }

}
