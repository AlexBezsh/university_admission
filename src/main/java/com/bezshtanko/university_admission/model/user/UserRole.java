package com.bezshtanko.university_admission.model.user;


import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ADMIN,
    ENTRANT;

    @Override
    public String getAuthority() {
        return name();
    }
}
