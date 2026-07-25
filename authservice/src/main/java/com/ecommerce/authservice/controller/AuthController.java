package com.ecommerce.authservice.controller;


import com.ecommerce.authservice.dto.LoginRequest;
import com.ecommerce.authservice.dto.LoginResponse;
import com.ecommerce.authservice.dto.RegisterRequest;
import com.ecommerce.authservice.dto.RegisterResponse;
import com.ecommerce.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private  final AuthService authService;

    @PostMapping("/register")
    public RegisterResponse registerUser( @Valid  @RequestBody RegisterRequest request){

       return  authService.register(request);
    }

    @GetMapping("/login")
    public LoginResponse login(@Valid  @RequestBody LoginRequest request){

        return  authService.login(request);
    }
}
