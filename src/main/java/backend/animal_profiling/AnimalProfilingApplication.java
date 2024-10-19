package backend.animal_profiling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "backend.animal_profiling")
public class AnimalProfilingApplication {

    public static void main(final String[] args) {
        SpringApplication.run(AnimalProfilingApplication.class, args);
    }

}
