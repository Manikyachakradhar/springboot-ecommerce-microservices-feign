package com.ecommerce.authservice.service;

import com.ecommerce.authservice.dto.LoginRequest;
import com.ecommerce.authservice.dto.LoginResponse;
import com.ecommerce.authservice.entity.Role;
import com.ecommerce.authservice.entity.User;
import com.ecommerce.authservice.exception.UserAlreadyExistsException;
import com.ecommerce.authservice.dto.RegisterRequest;
import com.ecommerce.authservice.dto.RegisterResponse;
import com.ecommerce.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public RegisterResponse register(RegisterRequest request){

        if(userRepository.existsByEmail(request.email())){
            throw  new UserAlreadyExistsException("Email already exists");
        }

        String encodedPassword= passwordEncoder.encode(request.password());

        User user= User.builder()
                .email(request.email())
                .password(encodedPassword)
                .role(Role.ROLE_USER)

                .build();

        userRepository.save(user);
        return new RegisterResponse( "User Registered Successfully! ");
    }

    public LoginResponse login(LoginRequest request){

        User user= userRepository.findByEmail(request.email())
                .orElseThrow(()-> new UsernameNotFoundException("Invalid Email or Password"));

        if(!passwordEncoder.matches(request.password(),user.getPassword())){
            throw  new BadCredentialsException("Invalid Email or password");
        }

        String token= jwtService.generateToken(user);
        return new LoginResponse(token);
    }
}
