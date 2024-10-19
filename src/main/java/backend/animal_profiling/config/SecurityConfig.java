package backend.animal_profiling.config;

import backend.animal_profiling.Security.JwtAuthenticationEntryPoint;
import backend.animal_profiling.Security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Inject the JwtAuthenticationFilter directly
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //@Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF for stateless JWT tokens
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/","/api/auth/login","/api/auth/register").permitAll()
//                        .anyRequest().authenticated()  // Protect other endpoints
//                )
//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Set session to stateless
//                );
//
//        // Add JWT filter before the UsernamePasswordAuthenticationFilter
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring()
//                .requestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**");  // Paths to ignore for security
//    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF for stateless JWT tokens
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints
                        .requestMatchers("/", "/api/auth/login", "/api/auth/forgetpassword").permitAll()
                        //allow all users to view the current user details
                        .requestMatchers("/api/users/current/details").hasAnyAuthority("ADMIN", "VETERINARIAN", "CARETAKER", "NUTRITIONIST")
                        .requestMatchers("/api/users/current/role").hasAnyAuthority("ADMIN", "VETERINARIAN", "CARETAKER", "NUTRITIONIST")
                        // User Resource - Restricted to ADMIN
                        .requestMatchers("/api/users/**").hasAuthority("ADMIN")

                        // Species Resource - ADMIN and VETERINARIAN
                        .requestMatchers(HttpMethod.GET, "/api/speciess/**").hasAnyAuthority("ADMIN", "VETERINARIAN")
                        .requestMatchers(HttpMethod.POST, "/api/speciess/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/speciess/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/speciess/**").hasAuthority("ADMIN")

                        // Role Resource - Only accessible by ADMIN
                        .requestMatchers("/api/roles/**").hasAuthority("ADMIN")

                        // Report Type Resource - ADMIN and NUTRITIONIST
                        .requestMatchers(HttpMethod.GET, "/api/reportTypes/**").hasAnyAuthority("ADMIN", "NUTRITIONIST")
                        .requestMatchers(HttpMethod.POST, "/api/reportTypes/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/reportTypes/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/reportTypes/**").hasAuthority("ADMIN")

                        // Nutrition Plan Resource - NUTRITIONIST only
                        .requestMatchers("/api/nutritionPlans/**").hasAuthority("NUTRITIONIST")

                        // Nutrient Resource - NUTRITIONIST and ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/nutrients/**").hasAnyAuthority("NUTRITIONIST", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/nutrients/**").hasAuthority("NUTRITIONIST")
                        .requestMatchers(HttpMethod.PUT, "/api/nutrients/**").hasAuthority("NUTRITIONIST")
                        .requestMatchers(HttpMethod.DELETE, "/api/nutrients/**").hasAuthority("ADMIN")

                        // Medical History Resource - VETERINARIAN only
                        .requestMatchers("/api/medicalHistories/**").hasAuthority("VETERINARIAN")

                        // Health Monitoring Resource - VETERINARIAN and ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/healthMonitorings/**").hasAnyAuthority("VETERINARIAN", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/healthMonitorings/**").hasAuthority("VETERINARIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/healthMonitorings/**").hasAuthority("VETERINARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/healthMonitorings/**").hasAuthority("ADMIN")

                        // Feeding Schedule Resource - CARETAKER
                        .requestMatchers("/api/feedingSchedules/**").hasAuthority("CARETAKER")

                        // Feed Inventory Resource - CARETAKER and ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/feedInventories/**").hasAnyAuthority("CARETAKER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/feedInventories/**").hasAuthority("CARETAKER")
                        .requestMatchers(HttpMethod.PUT, "/api/feedInventories/**").hasAuthority("CARETAKER")
                        .requestMatchers(HttpMethod.DELETE, "/api/feedInventories/**").hasAuthority("ADMIN")

                        // Educational Resource - ADMIN and NUTRITIONIST
                        .requestMatchers(HttpMethod.GET, "/api/educationalResources/**").hasAnyAuthority("ADMIN", "NUTRITIONIST")
                        .requestMatchers(HttpMethod.POST, "/api/educationalResources/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/educationalResources/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/educationalResources/**").hasAuthority("ADMIN")

                        // Data Analysis Reports - ADMIN only
                        .requestMatchers("/api/dataAnalysisReports/**").hasAuthority("ADMIN")

                        // Availability Status - ADMIN only
                        .requestMatchers("/api/availabilityStatuses/**").hasAuthority("ADMIN")

                        // Animal Resource - VETERINARIAN and CARETAKER
                        .requestMatchers(HttpMethod.GET, "/api/animals/**").hasAnyAuthority("VETERINARIAN", "CARETAKER")
                        .requestMatchers(HttpMethod.POST, "/api/animals/**").hasAuthority("VETERINARIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/animals/**").hasAuthority("VETERINARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/animals/**").hasAuthority("ADMIN")

                        // Alert Resource - VETERINARIAN and ADMIN
                        .requestMatchers("/api/alerts/**").hasAnyAuthority("VETERINARIAN", "ADMIN")

                        // Alert Type Resource - ADMIN only
                        .requestMatchers("/api/alertTypes/**").hasAuthority("ADMIN")



                        // Default rule for all other endpoints
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Stateless session for JWT
                );

        // Add JWT filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**");  // Paths to ignore for security
    }

}