package ru.neoflex.jd.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.security.auth.Subject;
import java.security.Principal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrincipal implements Principal {

    private Long id;
    private String name;

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
