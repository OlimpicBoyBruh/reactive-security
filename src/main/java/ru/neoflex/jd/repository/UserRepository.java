package ru.neoflex.jd.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.neoflex.jd.entity.User;

public interface UserRepository extends R2dbcRepository<User, Long> {

    Mono<User> findByUsername(String username);
}
