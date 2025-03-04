package ru.neoflex.jd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.neoflex.jd.dto.enumerated.Role;
import ru.neoflex.jd.entity.User;
import ru.neoflex.jd.repository.UserRepository;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<User> registerUser(User user) {
        return userRepository.save(
                user.toBuilder()
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(Role.USER)
                        .enabled(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).doOnSuccess(u -> {
            log.info("IN registerUser - user: {} created", u);
        });
    }

    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}