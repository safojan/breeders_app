package backend.animal_profiling.service;

import backend.animal_profiling.domain.Alert;
import backend.animal_profiling.domain.Animal;
import backend.animal_profiling.domain.DataAnalysisReport;
import backend.animal_profiling.domain.FeedingSchedule;
import backend.animal_profiling.domain.HealthMonitoring;
import backend.animal_profiling.domain.MedicalHistory;
import backend.animal_profiling.domain.NutritionPlan;
import backend.animal_profiling.domain.Species;
import backend.animal_profiling.model.AnimalDTO;
import backend.animal_profiling.repos.AlertRepository;
import backend.animal_profiling.repos.AnimalRepository;
import backend.animal_profiling.repos.DataAnalysisReportRepository;
import backend.animal_profiling.repos.FeedingScheduleRepository;
import backend.animal_profiling.repos.HealthMonitoringRepository;
import backend.animal_profiling.repos.MedicalHistoryRepository;
import backend.animal_profiling.repos.NutritionPlanRepository;
import backend.animal_profiling.repos.SpeciesRepository;
import backend.animal_profiling.util.NotFoundException;
import backend.animal_profiling.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final SpeciesRepository speciesRepository;
    private final AlertRepository alertRepository;
    private final DataAnalysisReportRepository dataAnalysisReportRepository;
    private final NutritionPlanRepository nutritionPlanRepository;
    private final FeedingScheduleRepository feedingScheduleRepository;
    private final HealthMonitoringRepository healthMonitoringRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;

    public AnimalService(final AnimalRepository animalRepository,
            final SpeciesRepository speciesRepository, final AlertRepository alertRepository,
            final DataAnalysisReportRepository dataAnalysisReportRepository,
            final NutritionPlanRepository nutritionPlanRepository,
            final FeedingScheduleRepository feedingScheduleRepository,
            final HealthMonitoringRepository healthMonitoringRepository,
            final MedicalHistoryRepository medicalHistoryRepository) {
        this.animalRepository = animalRepository;
        this.speciesRepository = speciesRepository;
        this.alertRepository = alertRepository;
        this.dataAnalysisReportRepository = dataAnalysisReportRepository;
        this.nutritionPlanRepository = nutritionPlanRepository;
        this.feedingScheduleRepository = feedingScheduleRepository;
        this.healthMonitoringRepository = healthMonitoringRepository;
        this.medicalHistoryRepository = medicalHistoryRepository;
    }

    public List<AnimalDTO> findAll() {
        final List<Animal> animals = animalRepository.findAll(Sort.by("animalId"));
        return animals.stream()
                .map(animal -> mapToDTO(animal, new AnimalDTO()))
                .toList();
    }

    public AnimalDTO get(final Integer animalId) {
        return animalRepository.findById(animalId)
                .map(animal -> mapToDTO(animal, new AnimalDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AnimalDTO animalDTO) {
        final Animal animal = new Animal();
        mapToEntity(animalDTO, animal);
        return animalRepository.save(animal).getAnimalId();
    }

    public void update(final Integer animalId, final AnimalDTO animalDTO) {
        final Animal animal = animalRepository.findById(animalId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(animalDTO, animal);
        animalRepository.save(animal);
    }

    public void delete(final Integer animalId) {
        animalRepository.deleteById(animalId);
    }

    private AnimalDTO mapToDTO(final Animal animal, final AnimalDTO animalDTO) {
        animalDTO.setAnimalId(animal.getAnimalId());
        animalDTO.setName(animal.getName());
        animalDTO.setBreed(animal.getBreed());
        animalDTO.setAge(animal.getAge());
        animalDTO.setWeight(animal.getWeight());
        animalDTO.setHealthStatus(animal.getHealthStatus());
        animalDTO.setActivityLevel(animal.getActivityLevel());
        animalDTO.setSpecialDietRequirement(animal.getSpecialDietRequirement());
        animalDTO.setCreatedAt(animal.getCreatedAt());
        animalDTO.setUpdatedAt(animal.getUpdatedAt());
        animalDTO.setSpecies(animal.getSpecies() == null ? null : animal.getSpecies().getSpeciesId());
        return animalDTO;
    }

    private Animal mapToEntity(final AnimalDTO animalDTO, final Animal animal) {
        animal.setName(animalDTO.getName());
        animal.setBreed(animalDTO.getBreed());
        animal.setAge(animalDTO.getAge());
        animal.setWeight(animalDTO.getWeight());
        animal.setHealthStatus(animalDTO.getHealthStatus());
        animal.setActivityLevel(animalDTO.getActivityLevel());
        animal.setSpecialDietRequirement(animalDTO.getSpecialDietRequirement());
        animal.setCreatedAt(animalDTO.getCreatedAt());
        animal.setUpdatedAt(animalDTO.getUpdatedAt());
        final Species species = animalDTO.getSpecies() == null ? null : speciesRepository.findById(animalDTO.getSpecies())
                .orElseThrow(() -> new NotFoundException("species not found"));
        animal.setSpecies(species);
        return animal;
    }

    public ReferencedWarning getReferencedWarning(final Integer animalId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Animal animal = animalRepository.findById(animalId)
                .orElseThrow(NotFoundException::new);
        final Alert animalAlert = alertRepository.findFirstByAnimal(animal);
        if (animalAlert != null) {
            referencedWarning.setKey("animal.alert.animal.referenced");
            referencedWarning.addParam(animalAlert.getAlertId());
            return referencedWarning;
        }
        final DataAnalysisReport animalDataAnalysisReport = dataAnalysisReportRepository.findFirstByAnimal(animal);
        if (animalDataAnalysisReport != null) {
            referencedWarning.setKey("animal.dataAnalysisReport.animal.referenced");
            referencedWarning.addParam(animalDataAnalysisReport.getReportId());
            return referencedWarning;
        }
        final NutritionPlan animalNutritionPlan = nutritionPlanRepository.findFirstByAnimal(animal);
        if (animalNutritionPlan != null) {
            referencedWarning.setKey("animal.nutritionPlan.animal.referenced");
            referencedWarning.addParam(animalNutritionPlan.getNutritionPlanId());
            return referencedWarning;
        }
        final FeedingSchedule animalFeedingSchedule = feedingScheduleRepository.findFirstByAnimal(animal);
        if (animalFeedingSchedule != null) {
            referencedWarning.setKey("animal.feedingSchedule.animal.referenced");
            referencedWarning.addParam(animalFeedingSchedule.getFeedingScheduleId());
            return referencedWarning;
        }
        final HealthMonitoring animalHealthMonitoring = healthMonitoringRepository.findFirstByAnimal(animal);
        if (animalHealthMonitoring != null) {
            referencedWarning.setKey("animal.healthMonitoring.animal.referenced");
            referencedWarning.addParam(animalHealthMonitoring.getHealthMonitoringId());
            return referencedWarning;
        }
        final MedicalHistory animalMedicalHistory = medicalHistoryRepository.findFirstByAnimal(animal);
        if (animalMedicalHistory != null) {
            referencedWarning.setKey("animal.medicalHistory.animal.referenced");
            referencedWarning.addParam(animalMedicalHistory.getMedicalHistoryId());
            return referencedWarning;
        }
        return null;
    }

}
