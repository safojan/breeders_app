package backend.animal_profiling.rest;

import backend.animal_profiling.Security.JwtHelper;
import backend.animal_profiling.model.AuthResponse;
import backend.animal_profiling.model.AuthRequest;
import backend.animal_profiling.service.AuthService;
import backend.animal_profiling.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor()
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtHelper jwtHelper;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticateUser(authRequest.getEmail(), authRequest.getPassword());
        System.out.println("Email: " + authRequest.getEmail());
        System.out.println("Password: " + authRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        String token = jwtHelper.generateToken(userDetails);
        AuthResponse authResponse = new AuthResponse(token);
        return ResponseEntity.ok(authResponse);
    }
    private Authentication authenticateUser(String username, String password) {

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    //forget password



}