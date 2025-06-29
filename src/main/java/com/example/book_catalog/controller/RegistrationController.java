package com.example.book_catalog.controller;

import com.example.book_catalog.dto.RegistrationRequest;
import com.example.book_catalog.model.entity.Role;
import com.example.book_catalog.model.entity.User;
import com.example.book_catalog.repository.RoleRepository;
import com.example.book_catalog.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository staffRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository staffRepository,
                                  RoleRepository roleRepository,
                                  PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Регистрация нового пользователя", description = "Создаёт нового пользователя в системе и назначает ему роль USER.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован."),
            @ApiResponse(responseCode = "400", description = "Ошибка регистрации, пользователь уже существует.")
    })
    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        if (staffRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        Role userRole = roleRepository.findByRoleName("ADMIN")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName("USER");
                    newRole.setAccessLevel(1);
                    return roleRepository.save(newRole);
                });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(userRole);
        staffRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
}