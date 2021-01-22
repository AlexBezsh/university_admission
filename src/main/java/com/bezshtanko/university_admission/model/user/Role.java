package com.bezshtanko.university_admission.model.user;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    ENTRANT;

    @Override
    public String getAuthority() {
        return name();
    }
}
