package ru.neoflex.jd.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class AuthResponseDto {
    private Long userId;
    private String token;
    private Date issuedAt;
    private Date expiredAt;
}
