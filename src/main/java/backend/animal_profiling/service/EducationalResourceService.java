package backend.animal_profiling.service;

import backend.animal_profiling.domain.EducationalResource;
import backend.animal_profiling.model.EducationalResourceDTO;
import backend.animal_profiling.repos.EducationalResourceRepository;
import backend.animal_profiling.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class EducationalResourceService {

    private final EducationalResourceRepository educationalResourceRepository;

    public EducationalResourceService(
            final EducationalResourceRepository educationalResourceRepository) {
        this.educationalResourceRepository = educationalResourceRepository;
    }

    public List<EducationalResourceDTO> findAll() {
        final List<EducationalResource> educationalResources = educationalResourceRepository.findAll(Sort.by("resourceId"));
        return educationalResources.stream()
                .map(educationalResource -> mapToDTO(educationalResource, new EducationalResourceDTO()))
                .toList();
    }

    public EducationalResourceDTO get(final Integer resourceId) {
        return educationalResourceRepository.findById(resourceId)
                .map(educationalResource -> mapToDTO(educationalResource, new EducationalResourceDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final EducationalResourceDTO educationalResourceDTO) {
        final EducationalResource educationalResource = new EducationalResource();
        mapToEntity(educationalResourceDTO, educationalResource);
        return educationalResourceRepository.save(educationalResource).getResourceId();
    }

    public void update(final Integer resourceId,
            final EducationalResourceDTO educationalResourceDTO) {
        final EducationalResource educationalResource = educationalResourceRepository.findById(resourceId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(educationalResourceDTO, educationalResource);
        educationalResourceRepository.save(educationalResource);
    }

    public void delete(final Integer resourceId) {
        educationalResourceRepository.deleteById(resourceId);
    }

    private EducationalResourceDTO mapToDTO(final EducationalResource educationalResource,
            final EducationalResourceDTO educationalResourceDTO) {
        educationalResourceDTO.setResourceId(educationalResource.getResourceId());
        educationalResourceDTO.setResourceTitle(educationalResource.getResourceTitle());
        educationalResourceDTO.setResourceType(educationalResource.getResourceType());
        educationalResourceDTO.setResourceLink(educationalResource.getResourceLink());
        educationalResourceDTO.setCreatedAt(educationalResource.getCreatedAt());
        educationalResourceDTO.setUpdatedAt(educationalResource.getUpdatedAt());
        return educationalResourceDTO;
    }

    private EducationalResource mapToEntity(final EducationalResourceDTO educationalResourceDTO,
            final EducationalResource educationalResource) {
        educationalResource.setResourceTitle(educationalResourceDTO.getResourceTitle());
        educationalResource.setResourceType(educationalResourceDTO.getResourceType());
        educationalResource.setResourceLink(educationalResourceDTO.getResourceLink());
        educationalResource.setCreatedAt(educationalResourceDTO.getCreatedAt());
        educationalResource.setUpdatedAt(educationalResourceDTO.getUpdatedAt());
        return educationalResource;
    }

}
