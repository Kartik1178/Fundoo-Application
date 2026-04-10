package com.example.fundoo.service;

import com.example.fundoo.dto.request.LoginRequestDto;
import com.example.fundoo.dto.request.UserRegisterRequestDto;
import com.example.fundoo.dto.response.LoginResponseDto;
import com.example.fundoo.dto.response.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegisterRequestDto requestDto);
    LoginResponseDto login(LoginRequestDto requestDto);
}
