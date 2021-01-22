package com.bezshtanko.university_admission.transfer;

import com.bezshtanko.university_admission.model.user.Role;
import com.bezshtanko.university_admission.model.user.Status;
import com.bezshtanko.university_admission.model.user.User;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDTO {

    private String fullName;
    private String email;
    private Status status;
    private Set<Role> roles;
    private String city;
    private String region;
    private String education;
    private byte[] certificate_scan;

    public UserDTO(User user) {
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.roles = user.getRoles();
        this.city = user.getCity();
        this.region = user.getRegion();
        this.education = user.getEducation();
    }

}
