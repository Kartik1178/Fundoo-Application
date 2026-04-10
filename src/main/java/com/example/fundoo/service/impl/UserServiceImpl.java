package com.example.fundoo.service.impl;

import com.example.fundoo.dto.request.LoginRequestDto;
import com.example.fundoo.dto.request.UserRegisterRequestDto;
import com.example.fundoo.dto.response.LoginResponseDto;
import com.example.fundoo.dto.response.UserResponseDto;
import com.example.fundoo.entity.User;
import com.example.fundoo.exception.InvalidCredentialsException;
import com.example.fundoo.exception.UserAlreadyExistsException;
import com.example.fundoo.exception.UserNotFoundException;
import com.example.fundoo.repository.UserRepository;
import com.example.fundoo.service.UserService;
import com.example.fundoo.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           TokenUtil tokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public UserResponseDto register(UserRegisterRequestDto requestDto) {
        log.info("Registering new user with email: {}", requestDto.getEmail());
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("Registration failed - email already exists: {}", requestDto.getEmail());
            throw new UserAlreadyExistsException("Email already registered: " + requestDto.getEmail());
        }
        User user = new User();
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());
        return new UserResponseDto(savedUser.getId(), savedUser.getFirstName(),
                savedUser.getLastName(), savedUser.getEmail(), "User registered successfully");
    }

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        log.info("Login attempt for email: {}", requestDto.getEmail());
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + requestDto.getEmail()));
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            log.warn("Login failed - invalid credentials for email: {}", requestDto.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }
        String token = tokenUtil.generateToken(user.getId());
        log.info("Login successful for user id: {}", user.getId());
        return new LoginResponseDto(token, "Login successful");
    }
}
