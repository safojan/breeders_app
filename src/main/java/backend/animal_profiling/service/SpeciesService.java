package backend.animal_profiling.service;

import backend.animal_profiling.domain.Animal;
import backend.animal_profiling.domain.Species;
import backend.animal_profiling.model.SpeciesDTO;
import backend.animal_profiling.repos.AnimalRepository;
import backend.animal_profiling.repos.SpeciesRepository;
import backend.animal_profiling.util.NotFoundException;
import backend.animal_profiling.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SpeciesService {

    private final SpeciesRepository speciesRepository;
    private final AnimalRepository animalRepository;

    public SpeciesService(final SpeciesRepository speciesRepository,
            final AnimalRepository animalRepository) {
        this.speciesRepository = speciesRepository;
        this.animalRepository = animalRepository;
    }

    public List<SpeciesDTO> findAll() {
        final List<Species> species = speciesRepository.findAll(Sort.by("speciesId"));
        return species.stream()
                .map(speciesItem -> mapToDTO(speciesItem, new SpeciesDTO()))
                .toList();
    }

    public SpeciesDTO get(final Integer speciesId) {
        return speciesRepository.findById(speciesId)
                .map(species -> mapToDTO(species, new SpeciesDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final SpeciesDTO speciesDTO) {
        final Species species = new Species();
        mapToEntity(speciesDTO, species);
        return speciesRepository.save(species).getSpeciesId();
    }

    public void update(final Integer speciesId, final SpeciesDTO speciesDTO) {
        final Species species = speciesRepository.findById(speciesId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(speciesDTO, species);
        speciesRepository.save(species);
    }

    public void delete(final Integer speciesId) {
        speciesRepository.deleteById(speciesId);
    }

    private SpeciesDTO mapToDTO(final Species species, final SpeciesDTO speciesDTO) {
        speciesDTO.setSpeciesId(species.getSpeciesId());
        speciesDTO.setSpeciesName(species.getSpeciesName());
        return speciesDTO;
    }

    private Species mapToEntity(final SpeciesDTO speciesDTO, final Species species) {
        species.setSpeciesName(speciesDTO.getSpeciesName());
        return species;
    }

    public ReferencedWarning getReferencedWarning(final Integer speciesId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Species species = speciesRepository.findById(speciesId)
                .orElseThrow(NotFoundException::new);
        final Animal speciesAnimal = animalRepository.findFirstBySpecies(species);
        if (speciesAnimal != null) {
            referencedWarning.setKey("species.animal.species.referenced");
            referencedWarning.addParam(speciesAnimal.getAnimalId());
            return referencedWarning;
        }
        return null;
    }

}
