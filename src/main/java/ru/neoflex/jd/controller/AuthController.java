package ru.neoflex.jd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.neoflex.jd.dto.AuthRequestDto;
import ru.neoflex.jd.dto.AuthResponseDto;
import ru.neoflex.jd.dto.UserDto;
import ru.neoflex.jd.entity.User;
import ru.neoflex.jd.mapping.UserMapper;
import ru.neoflex.jd.repository.UserRepository;
import ru.neoflex.jd.security.CustomPrincipal;
import ru.neoflex.jd.security.SecurityService;
import ru.neoflex.jd.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final SecurityService securityService;
    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto dto) {
        User entity = userMapper.toEntity(dto);
        return userService.registerUser(entity)
                .map(userMapper::toDto);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto dto) {
        return securityService.authenticate(dto.getUsername(), dto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiredAt(tokenDetails.getExpiresAt())
                                .build()
                ));
    }

    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();

        return userService.getUserById(customPrincipal.getId())
                .map(userMapper::toDto);
    }
}