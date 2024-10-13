package backend.animal_profiling.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Animals")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Animal {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Integer animalId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String breed;

    @Column
    private Integer age;

    @Column
    private Double weight;

    @Column
    private String healthStatus;

    @Column(length = 50)
    private String activityLevel;

    @Column(columnDefinition = "text")
    private String specialDietRequirement;

    @Column
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id")
    private Species species;

    @OneToMany(mappedBy = "animal")
    private Set<Alert> animalAlerts;

    @OneToMany(mappedBy = "animal")
    private Set<DataAnalysisReport> animalDataAnalysisReports;

    @OneToMany(mappedBy = "animal")
    private Set<NutritionPlan> animalNutritionPlans;

    @OneToMany(mappedBy = "animal")
    private Set<FeedingSchedule> animalFeedingSchedules;

    @OneToMany(mappedBy = "animal")
    private Set<HealthMonitoring> animalHealthMonitorings;

    @OneToMany(mappedBy = "animal")
    private Set<MedicalHistory> animalMedicalHistories;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
