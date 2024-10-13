package backend.animal_profiling.model;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DataAnalysisReportDTO {

    private Integer reportId;
    private String reportContent;
    private OffsetDateTime reportDate;
    private Integer animal;
    private Integer reportType;
    private Integer createdByUser;

}
