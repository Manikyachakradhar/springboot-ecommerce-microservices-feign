package com.ecommerce.authservice.controller;


import com.ecommerce.authservice.dto.LoginRequest;
import com.ecommerce.authservice.dto.LoginResponse;
import com.ecommerce.authservice.dto.RegisterRequest;
import com.ecommerce.authservice.dto.RegisterResponse;
import com.ecommerce.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication APIS" ,description = "Authentication Management APIS")
public class AuthController {
    private  final AuthService authService;

    @Operation(summary = "Register User",
    description = "Creates a new User")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User  Registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid useEmail"),
    })
    @PostMapping("/register")
    public RegisterResponse registerUser( @Valid  @RequestBody RegisterRequest request){

       return  authService.register(request);
    }

    @Operation(
            summary = "Login User",
            description = "Authenticates a registered user and returns a JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/login")
    public LoginResponse login(@Valid  @RequestBody LoginRequest request){

        return  authService.login(request);
    }
}
