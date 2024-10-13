package backend.animal_profiling.service;

import backend.animal_profiling.domain.Animal;
import backend.animal_profiling.domain.MedicalHistory;
import backend.animal_profiling.model.MedicalHistoryDTO;
import backend.animal_profiling.repos.AnimalRepository;
import backend.animal_profiling.repos.MedicalHistoryRepository;
import backend.animal_profiling.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MedicalHistoryService {

    private final MedicalHistoryRepository medicalHistoryRepository;
    private final AnimalRepository animalRepository;

    public MedicalHistoryService(final MedicalHistoryRepository medicalHistoryRepository,
            final AnimalRepository animalRepository) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.animalRepository = animalRepository;
    }

    public List<MedicalHistoryDTO> findAll() {
        final List<MedicalHistory> medicalHistories = medicalHistoryRepository.findAll(Sort.by("medicalHistoryId"));
        return medicalHistories.stream()
                .map(medicalHistory -> mapToDTO(medicalHistory, new MedicalHistoryDTO()))
                .toList();
    }

    public MedicalHistoryDTO get(final Integer medicalHistoryId) {
        return medicalHistoryRepository.findById(medicalHistoryId)
                .map(medicalHistory -> mapToDTO(medicalHistory, new MedicalHistoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final MedicalHistoryDTO medicalHistoryDTO) {
        final MedicalHistory medicalHistory = new MedicalHistory();
        mapToEntity(medicalHistoryDTO, medicalHistory);
        return medicalHistoryRepository.save(medicalHistory).getMedicalHistoryId();
    }

    public void update(final Integer medicalHistoryId, final MedicalHistoryDTO medicalHistoryDTO) {
        final MedicalHistory medicalHistory = medicalHistoryRepository.findById(medicalHistoryId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(medicalHistoryDTO, medicalHistory);
        medicalHistoryRepository.save(medicalHistory);
    }

    public void delete(final Integer medicalHistoryId) {
        medicalHistoryRepository.deleteById(medicalHistoryId);
    }

    private MedicalHistoryDTO mapToDTO(final MedicalHistory medicalHistory,
            final MedicalHistoryDTO medicalHistoryDTO) {
        medicalHistoryDTO.setMedicalHistoryId(medicalHistory.getMedicalHistoryId());
        medicalHistoryDTO.setIllness(medicalHistory.getIllness());
        medicalHistoryDTO.setTreatment(medicalHistory.getTreatment());
        medicalHistoryDTO.setDateOfRecord(medicalHistory.getDateOfRecord());
        medicalHistoryDTO.setComments(medicalHistory.getComments());
        medicalHistoryDTO.setAnimal(medicalHistory.getAnimal() == null ? null : medicalHistory.getAnimal().getAnimalId());
        return medicalHistoryDTO;
    }

    private MedicalHistory mapToEntity(final MedicalHistoryDTO medicalHistoryDTO,
            final MedicalHistory medicalHistory) {
        medicalHistory.setIllness(medicalHistoryDTO.getIllness());
        medicalHistory.setTreatment(medicalHistoryDTO.getTreatment());
        medicalHistory.setDateOfRecord(medicalHistoryDTO.getDateOfRecord());
        medicalHistory.setComments(medicalHistoryDTO.getComments());
        final Animal animal = medicalHistoryDTO.getAnimal() == null ? null : animalRepository.findById(medicalHistoryDTO.getAnimal())
                .orElseThrow(() -> new NotFoundException("animal not found"));
        medicalHistory.setAnimal(animal);
        return medicalHistory;
    }

}
